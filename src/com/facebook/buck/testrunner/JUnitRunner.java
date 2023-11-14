/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.buck.testrunner;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import com.facebook.buck.test.selectors.TestDescription;
import com.facebook.buck.test.selectors.TestSelector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.builders.AnnotatedBuilder;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.RunnerBuilder;

/**
 * Class that runs a set of JUnit tests and writes the results to a directory.
 *
 * <p>IMPORTANT! This class limits itself to types that are available in both the JDK and Android
 * Java API. The objective is to limit the set of files added to the ClassLoader that runs the test,
 * as not to interfere with the results of the test.
 */
public final class JUnitRunner extends BaseRunner {

  static final String JUL_DEBUG_LOGS_HEADER = "====DEBUG LOGS====\n\n";
  static final String JUL_ERROR_LOGS_HEADER = "====ERROR LOGS====\n\n";

  private static final String STD_OUT_LOG_LEVEL_PROPERTY = "com.facebook.buck.stdOutLogLevel";
  private static final String STD_ERR_LOG_LEVEL_PROPERTY = "com.facebook.buck.stdErrLogLevel";

  public JUnitRunner() {}

  @Override
  public void run() throws Throwable {
    Level stdOutLogLevel = Level.INFO;
    Level stdErrLogLevel = Level.WARNING;

    String unparsedStdOutLogLevel = System.getProperty(STD_OUT_LOG_LEVEL_PROPERTY);
    String unparsedStdErrLogLevel = System.getProperty(STD_ERR_LOG_LEVEL_PROPERTY);

    if (unparsedStdOutLogLevel != null) {
      stdOutLogLevel = Level.parse(unparsedStdOutLogLevel);
    }

    if (unparsedStdErrLogLevel != null) {
      stdErrLogLevel = Level.parse(unparsedStdErrLogLevel);
    }

    for (String className : testClassNames) {
      Class<?> testClass = Class.forName(className);
      List<TestResult> results = new ArrayList<>();
      RecordingFilter filter = new RecordingFilter();
      if (mightBeJunit4TestClass(testClass)) {
        JUnitCore jUnitCore = new JUnitCore();
        Runner suite = new Computer().getSuite(createRunnerBuilder(), new Class<?>[] {testClass});
        Request request = Request.runner(suite);
        request = request.filterWith(filter);
        jUnitCore.addListener(new Junit4TestListener(results, stdOutLogLevel, stdErrLogLevel, isDryRun));
        jUnitCore.run(request);
      } else if (mightBeJunit5TestClass(testClass)) {
        LauncherDiscoveryRequest request =
          LauncherDiscoveryRequestBuilder.request()
            .selectors(selectClass(testClass))
            .build();
        Launcher launcher = LauncherFactory.create();
        Junit5TestListener listener = new Junit5TestListener(results, stdOutLogLevel, stdErrLogLevel, testClass);
        launcher.execute(request, listener);
      }
      // Combine the results with the tests we filtered out
      List<TestResult> actualResults = combineResults(results, filter.filteredOut);
      writeResult(className, actualResults);
    }
  }

  /** Guessing whether a class is a test class is an imperfect art form. */
  private boolean mightBeJunit4TestClass(Class<?> klass) {
    if (klass.getAnnotation(RunWith.class) != null) {
      return true; // If the class is explicitly marked with @RunWith, it's a test class.
    }
    // Since no RunWith annotation, using standard runner, which requires
    // test classes to be non-abstract/non-interface
    int klassModifiers = klass.getModifiers();
    if (Modifier.isInterface(klassModifiers) || Modifier.isAbstract(klassModifiers)) {
      return false;
    }

    // Classes that extend junit.framework.TestCase are JUnit3-style test classes.
    if (TestCase.class.isAssignableFrom(klass)) {
      return true;
    }

    // Since no RunWith annotation, using standard runner, which requires
    // test classes to have exactly one public constructor (that has no args).
    // Classes may have (non-public) constructors (with or without args).
    boolean foundPublicNoArgConstructor = false;
    for (Constructor<?> c : klass.getConstructors()) {
      if (Modifier.isPublic(c.getModifiers())) {
        if (c.getParameterCount() != 0) {
          return false;
        }
        foundPublicNoArgConstructor = true;
      }
    }
    if (!foundPublicNoArgConstructor) {
      return false;
    }
    // If the class has a JUnit4 @Test-annotated method, it's a test class.
    boolean hasAtLeastOneTest = false;
    for (Method m : klass.getMethods()) {
      if (Modifier.isPublic(m.getModifiers())
          && m.getParameters().length == 0
          && m.getAnnotation(Test.class) != null) {
        hasAtLeastOneTest = true;
        break;
      }
    }
    return hasAtLeastOneTest;
  }

  private boolean mightBeJunit5TestClass(Class<?> klass) {
    for (Method m : klass.getMethods()) {
      if (m.getAnnotation(org.junit.jupiter.api.Test.class) != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method filters a list of test results prior to writing results to a file. null is returned
   * to indicate "don't write anything", which is different to writing a file containing 0 results.
   *
   * <p>JUnit handles classes-without-tests in different ways. If you are not using the
   * org.junit.runner.Request.filterWith facility then JUnit ignores classes-without-tests. However,
   * if you are using a filter then a class-without-tests will cause a NoTestsRemainException to be
   * thrown, which is propagated back as an error.
   */
  /* @Nullable */
  private List<TestResult> combineResults(
      List<TestResult> results, List<TestResult> filteredResults) {
    List<TestResult> combined = new ArrayList<>(filteredResults);
    if (!isSingleResultCausedByNoTestsRemainException(results)) {
      combined.addAll(results);
    }
    return combined;
  }

  /**
   * JUnit doesn't normally consider encountering a testless class an error. However, when using
   * org.junit.runner.manipulation.Filter, testless classes *are* considered an error, throwing
   * org.junit.runner.manipulation.NoTestsRemainException.
   *
   * <p>If we are using test-selectors then it's possible we will run a test class but never run any
   * of its test methods, because they'd all get filtered out. When this happens, the results will
   * contain a single failure containing the error from the NoTestsRemainException.
   *
   * <p>However, there is another reason why the test class may have a single failure -- if the
   * class fails to instantiate, then it doesn't get far enough to detect whether or not there were
   * any tests. In that case, JUnit4 returns a single failure result with the testMethodName set to
   * "initializationError".
   *
   * <p>(NB: we can't decide at the class level whether we need to run a test class or not; we can
   * only run the test class and all its test methods and handle the erroneous exception JUnit
   * throws if no test-methods were actually run.)
   */
  private boolean isSingleResultCausedByNoTestsRemainException(List<TestResult> results) {
    if (results.size() != 1) {
      return false;
    }
    TestResult singleResult = results.get(0);
    return !singleResult.isSuccess()
        && "initializationError".equals(singleResult.testMethodName)
        && "org.junit.runner.manipulation.Filter".equals(singleResult.testClassName);
  }

  /**
   * Creates an {@link AllDefaultPossibilitiesBuilder} that returns our custom {@link
   * BuckBlockJUnit4ClassRunner} when a {@link JUnit4Builder} is requested. This ensures that JUnit
   * 4 tests are executed using our runner whereas other types of tests are run with whatever JUnit
   * thinks is best.
   */
  private RunnerBuilder createRunnerBuilder() {
    JUnit4Builder jUnit4RunnerBuilder =
        new JUnit4Builder() {
          @Override
          public Runner runnerForClass(Class<?> testClass) throws Throwable {
            return new BuckBlockJUnit4ClassRunner(testClass, defaultTestTimeoutMillis);
          }
        };

    return new AllDefaultPossibilitiesBuilder(/* canUseSuiteMethod */ true) {
      @Override
      protected JUnit4Builder junit4Builder() {
        return jUnit4RunnerBuilder;
      }

      @Override
      protected AnnotatedBuilder annotatedBuilder() {
        // If there is no default timeout specified in .buckconfig, then use
        // the original behavior of AllDefaultPossibilitiesBuilder.
        //
        // Additionally, if we are using test selectors or doing a dry-run then
        // we should use the original behavior to use our
        // BuckBlockJUnit4ClassRunner, which provides the Descriptions needed
        // to do test selecting properly.
        if (defaultTestTimeoutMillis <= 0 || isDryRun || !testSelectorList.isEmpty()) {
          return super.annotatedBuilder();
        }

        return new AnnotatedBuilder(this) {
          @Override
          public Runner buildRunner(Class<? extends Runner> runnerClass, Class<?> testClass)
              throws Exception {
            Runner originalRunner = super.buildRunner(runnerClass, testClass);
            return new DelegateRunnerWithTimeout(originalRunner, defaultTestTimeoutMillis);
          }
        };
      }
    };
  }

  /** A JUnit Filter that records the tests it filters out. */
  private class RecordingFilter extends Filter {
    static final String FILTER_DESCRIPTION = "TestSelectorList-filter";

    List<TestResult> filteredOut = new ArrayList<>();

    @Override
    public boolean shouldRun(Description description) {
      String methodName = description.getMethodName();
      if (methodName == null) {
        // JUnit will give us an org.junit.runner.Description like this for the
        // test class
        // itself.  It's easier for our filtering to make decisions just at the method level,
        // however, so just always return true here.
        return true;
      }
      String className = description.getClassName();
      TestDescription testDescription = new TestDescription(className, methodName);
      TestSelector matchingSelector = testSelectorList.findSelector(testDescription);
      if (!matchingSelector.isInclusive()) {
        if (shouldExplainTestSelectors) {
          String reason = "Excluded by filter: " + matchingSelector.getExplanation();
          filteredOut.add(TestResult.forExcluded(className, methodName, reason));
        }
        return false;
      }
      if (description.getAnnotation(Ignore.class) != null) {
        filteredOut.add(TestResult.forDisabled(className, methodName));
        return false;
      }
      if (isDryRun) {
        filteredOut.add(TestResult.forDryRun(className, methodName));
        return false;
      }
      return true;
    }

    @Override
    public String describe() {
      return FILTER_DESCRIPTION;
    }
  }
}
