package com.facebook.buck.testrunner;

import com.facebook.buck.test.result.type.ResultType;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class Junit5TestListener implements TestExecutionListener {

  private final List<TestResult> results;
  private final Level stdErrLogLevel;
  private final Level stdOutLogLevel;
  /* @Nullable */ private PrintStream originalOut, originalErr, stdOutStream, stdErrStream;
  /* @Nullable */ private ByteArrayOutputStream rawStdOutBytes, rawStdErrBytes;
  /* @Nullable */ private ByteArrayOutputStream julLogBytes, julErrLogBytes;
  /* @Nullable */ private Handler julLogHandler;
  /* @Nullable */ private Handler julErrLogHandler;
  /* @Nullable */ private TestIdentifier testIdentifier;
  private final SummaryGeneratingListener listener = new SummaryGeneratingListener();
  private final Class<?> testClass;

  // To help give a reasonable (though imprecise) guess at the runtime for unpaired failures
  private long testStartTime;
  private long testPlanStartTime;

  public Junit5TestListener(List<TestResult> results, Level stdErrLogLevel, Level stdOutLogLevel, Class<?> testClass) {
    this.results = results;
    this.stdErrLogLevel = stdErrLogLevel;
    this.stdOutLogLevel = stdOutLogLevel;
    this.testClass = testClass;
    this.testStartTime = System.currentTimeMillis();
    this.testPlanStartTime = System.currentTimeMillis();
  }

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    this.testPlanStartTime = System.currentTimeMillis();
    listener.testPlanExecutionStarted(testPlan);
  }

  // compare to Junit4TestListener.testIgnored
  @Override
  public void executionSkipped(TestIdentifier testIdentifier, String reason) {
    listener.executionSkipped(testIdentifier, reason);
  }

  // compare to Junit4TestListener.testStarted
  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    // Create an intermediate stdout/stderr to capture any debugging statements (usually in the
    // form of System.out.println) the developer is using to debug the test.
    originalOut = System.out;
    originalErr = System.err;
    rawStdOutBytes = new ByteArrayOutputStream();
    rawStdErrBytes = new ByteArrayOutputStream();
    julLogBytes = new ByteArrayOutputStream();
    julErrLogBytes = new ByteArrayOutputStream();
    try {
      stdOutStream = new PrintStream(rawStdOutBytes, true /* autoFlush */, BaseRunner.ENCODING);
      stdErrStream = new PrintStream(rawStdErrBytes, true /* autoFlush */, BaseRunner.ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
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

    this.testStartTime = System.currentTimeMillis();
    listener.executionStarted(testIdentifier);
  }

  // compare to Junit4TestListener.testFinished
  @Override
  public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    // capture testIdentifier
    this.testIdentifier = testIdentifier;

    // Shutdown single-test result.
    listener.executionFinished(testIdentifier, testExecutionResult);
    listener.testPlanExecutionFinished(null);

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

    TestExecutionSummary summary = listener.getSummary();
    TestExecutionSummary.Failure failure = null;
    ResultType type = ResultType.SUCCESS;
    for(TestExecutionSummary.Failure f : summary.getFailures()) {
      if(f.getTestIdentifier().equals(testIdentifier)) {
        failure = f;
        type = ResultType.FAILURE;
        break;
      }
    }

    StringBuilder stdOut = new StringBuilder();
    StringBuilder stdErr = new StringBuilder();
    try {
      stdOut.append(rawStdOutBytes.toString(BaseRunner.ENCODING));
      if (type == ResultType.FAILURE && julLogBytes.size() > 0) {
        stdOut.append('\n');
        stdOut.append(JUnitRunner.JUL_DEBUG_LOGS_HEADER);
        stdOut.append(julLogBytes.toString(BaseRunner.ENCODING));
      }
      stdErr.append(rawStdErrBytes.toString(BaseRunner.ENCODING));
      if (type == ResultType.FAILURE && julErrLogBytes.size() > 0) {
        stdErr.append('\n');
        stdErr.append(JUnitRunner.JUL_ERROR_LOGS_HEADER);
        stdErr.append(julErrLogBytes.toString(BaseRunner.ENCODING));
      }
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    String className = testClass.getCanonicalName();
    String methodName =
      testIdentifier.isTest() ? testIdentifier.getLegacyReportingName().replace("()", "") : "";
    long runTime = System.currentTimeMillis() - this.testStartTime;
    results.add(
      new TestResult(
        className,
        methodName,
        runTime,
        type,
        failure == null ? null : failure.getException(),
        stdOut.length() == 0 ? null : stdOut.toString(),
        stdErr.length() == 0 ? null : stdErr.toString()));
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
