package com.facebook.buck.testrunner;

import com.facebook.buck.test.result.type.ResultType;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Creates RunListener that will prepare individual result for each test and store it to results
 * list afterward.
 */
final class Junit4TestListener extends RunListener {

  private final List<TestResult> results;
  private final Level stdErrLogLevel;
  private final Level stdOutLogLevel;
  private final boolean isDryRun;
  /* @Nullable */ private PrintStream originalOut, originalErr, stdOutStream, stdErrStream;
  /* @Nullable */ private ByteArrayOutputStream rawStdOutBytes, rawStdErrBytes;
  /* @Nullable */ private ByteArrayOutputStream julLogBytes, julErrLogBytes;
  /* @Nullable */ private Handler julLogHandler;
  /* @Nullable */ private Handler julErrLogHandler;
  /* @Nullable */ private Result result;
  /* @Nullable */ private RunListener resultListener;
  /* @Nullable */ private Failure assumptionFailure;

  // To help give a reasonable (though imprecise) guess at the runtime for unpaired failures
  private final long startTime = System.currentTimeMillis();

  Junit4TestListener(List<TestResult> results, Level stdOutLogLevel, Level stdErrLogLevel,
    boolean isDryRun) {
    this.results = results;
    this.stdOutLogLevel = stdOutLogLevel;
    this.stdErrLogLevel = stdErrLogLevel;
    this.isDryRun = isDryRun;
  }

  @Override
  public void testStarted(Description description) throws Exception {
    // Create an intermediate stdout/stderr to capture any debugging statements (usually in the
    // form of System.out.println) the developer is using to debug the test.
    originalOut = System.out;
    originalErr = System.err;
    rawStdOutBytes = new ByteArrayOutputStream();
    rawStdErrBytes = new ByteArrayOutputStream();
    julLogBytes = new ByteArrayOutputStream();
    julErrLogBytes = new ByteArrayOutputStream();
    stdOutStream = new PrintStream(rawStdOutBytes, true /* autoFlush */, BaseRunner.ENCODING);
    stdErrStream = new PrintStream(rawStdErrBytes, true /* autoFlush */, BaseRunner.ENCODING);
    System.setOut(stdOutStream);
    System.setErr(stdErrStream);

    // Listen to any java.util.logging messages reported by the test and write them to
    // julLogBytes / julErrLogBytes.
    Logger rootLogger = LogManager.getLogManager().getLogger("");

    if (rootLogger != null) {
      rootLogger.setLevel(Level.FINE);
    }

    JulLogFormatter formatter = new JulLogFormatter();
    julLogHandler = addStreamHandler(rootLogger, julLogBytes, formatter, stdOutLogLevel);
    julErrLogHandler = addStreamHandler(rootLogger, julErrLogBytes, formatter, stdErrLogLevel);

    // Prepare single-test result.
    result = new Result();
    resultListener = result.createListener();
    resultListener.testRunStarted(description);
    resultListener.testStarted(description);
  }

  @Override
  public void testFinished(Description description) throws Exception {
    // Shutdown single-test result.
    resultListener.testFinished(description);
    resultListener.testRunFinished(result);
    resultListener = null;

    // Restore the original stdout/stderr.
    System.setOut(originalOut);
    System.setErr(originalErr);

    // Flush any debug logs and remove the handlers.
    Logger rootLogger = LogManager.getLogManager().getLogger("");

    flushAndRemoveLogHandler(rootLogger, julLogHandler);
    julLogHandler = null;

    flushAndRemoveLogHandler(rootLogger, julErrLogHandler);
    julErrLogHandler = null;

    // Get the stdout/stderr written during the test as strings.
    stdOutStream.flush();
    stdErrStream.flush();

    int numFailures = result.getFailureCount();
    String className = description.getClassName();
    String methodName = description.getMethodName();

    Failure failure;
    ResultType type;
    if (assumptionFailure != null) {
      failure = assumptionFailure;
      type = ResultType.ASSUMPTION_VIOLATION;
      // Clear the assumption-failure field before the next test result appears.
      assumptionFailure = null;
    } else if (isDryRun) {
      if ("org.junit.runner.manipulation.Filter".equals(className)
        && "initializationError".equals(methodName)) {
        return; // don't record errors from failed class initialization during dry run
      }
      failure = numFailures == 0 ? null : result.getFailures().get(0);
      type = ResultType.DRY_RUN;
    } else if (numFailures == 0) {
      failure = null;
      type = ResultType.SUCCESS;
    } else {
      failure = result.getFailures().get(0);
      type = ResultType.FAILURE;
    }

    StringBuilder stdOut = new StringBuilder();
    stdOut.append(rawStdOutBytes.toString(BaseRunner.ENCODING));
    if (type == ResultType.FAILURE && julLogBytes.size() > 0) {
      stdOut.append('\n');
      stdOut.append(JUnitRunner.JUL_DEBUG_LOGS_HEADER);
      stdOut.append(julLogBytes.toString(BaseRunner.ENCODING));
    }
    StringBuilder stdErr = new StringBuilder();
    stdErr.append(rawStdErrBytes.toString(BaseRunner.ENCODING));
    if (type == ResultType.FAILURE && julErrLogBytes.size() > 0) {
      stdErr.append('\n');
      stdErr.append(JUnitRunner.JUL_ERROR_LOGS_HEADER);
      stdErr.append(julErrLogBytes.toString(BaseRunner.ENCODING));
    }

    results.add(
      new TestResult(
        className,
        methodName,
        result.getRunTime(),
        type,
        failure == null ? null : failure.getException(),
        stdOut.length() == 0 ? null : stdOut.toString(),
        stdErr.length() == 0 ? null : stdErr.toString()));
  }

  @Override
  public void testRunFinished(Result runResult) {
    if (resultListener != null) {
      // testStarted was called for latest test, but not the testFinished
      // report all failures as unbounded
      for (Failure failure : result.getFailures()) {
        recordUnpairedResult(failure, ResultType.FAILURE);
      }
    }
  }

  /**
   * The regular listener we created from the singular result, in this class, will not by default
   * treat assumption failures as regular failures, and will not store them. As a consequence, we
   * store them ourselves!
   *
   * <p>We store the assumption-failure in a temporary field, which we'll make sure we clear each
   * time we write results.
   */
  @Override
  public void testAssumptionFailure(Failure failure) {
    assumptionFailure = failure;
    if (resultListener == null) {
      recordUnpairedResult(failure, ResultType.ASSUMPTION_VIOLATION);
    } else {
      // Left in only to help catch future bugs -- right now this does nothing.
      resultListener.testAssumptionFailure(failure);
    }
  }

  @Override
  public void testFailure(Failure failure) throws Exception {
    if (resultListener == null) {
      recordUnpairedResult(failure, ResultType.FAILURE);
    } else {
      resultListener.testFailure(failure);
    }
  }

  @Override
  public void testIgnored(Description description) throws Exception {
    if (resultListener != null) {
      resultListener.testIgnored(description);
    }
  }

  /**
   * It's possible to encounter a Failure/Skip before we've started any tests (and therefore
   * before testStarted() has been called). The known example is a @BeforeClass that throws an
   * exception, but there may be others.
   *
   * <p>Recording these unexpected failures helps us propagate failures back up to the "buck test"
   * process.
   */
  private void recordUnpairedResult(Failure failure, ResultType resultType) {
    long runtime = System.currentTimeMillis() - startTime;
    Description description = failure.getDescription();
    results.add(
      new TestResult(
        description.getClassName(),
        description.getMethodName(),
        runtime,
        resultType,
        failure.getException(),
        null,
        null));
  }

  private Handler addStreamHandler(
    Logger rootLogger, OutputStream stream, Formatter formatter, Level level) {
    Handler result;
    if (rootLogger != null) {
      result = new StreamHandler(stream, formatter);
      result.setLevel(level);
      rootLogger.addHandler(result);
    } else {
      result = null;
    }
    return result;
  }

  private void flushAndRemoveLogHandler(Logger rootLogger, Handler handler) {
    if (handler != null) {
      handler.flush();
    }
    if (rootLogger != null && handler != null) {
      rootLogger.removeHandler(handler);
    }
  }
}
