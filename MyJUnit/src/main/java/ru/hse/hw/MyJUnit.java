package ru.hse.hw;

import org.jetbrains.annotations.NotNull;
import ru.hse.hw.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.hse.hw.TestReport.TestStatus.*;

/**
 * MyJUnit class run test methods of given class.
 * MyJUnit do not run tests with annotations ignore.
 * MyJUnit consider test succeeded if it terminated with exception from annotation or without any exception if annotation does not contains exception.
 * MyJUnit consider test failed if it terminated with exception different from exception from annotation or without any exception when annotation contains exception.
 * Before and after every test MyJUnit runs methods of give class with annotations @Before and @After.
 * Before and after all tests MyJUnit runs methods of give class with annotations @BeforeClass and @AfterClass.
 */
public class MyJUnit {

    /**
     * Get tests and required methods from given class and run them in order:
     * BeforeClass -> [Before -> Test -> After] ->  ... -> [Before -> Test -> After] -> AfterClass
     * @param testClass class to run tests in it
     * @return array of report for all done tests
     * @throws IllegalAccessException if instance construction terminated with error
     * @throws InstantiationException if instance construction terminated with erro
     */
    public static List<TestReport> run(Class<?> testClass) throws IllegalAccessException, InstantiationException {
        var classInstance = testClass.newInstance();
        var beforeClassMethods = classifyMethods(testClass, BeforeClass.class);
        var afterClassMethods = classifyMethods(testClass, AfterClass.class);
        var beforeMethods = classifyMethods(testClass, Before.class);
        var afterMethods = classifyMethods(testClass, After.class);
        var tests = classifyMethods(testClass, Test.class);

        var reports = new ArrayList<TestReport>();

        var beforeClassReport = invokeMethods(classInstance, beforeClassMethods, BEFORE_CLASS_FAIL);
        if (beforeClassReport != null) {
            reports.add(beforeClassReport);
        }

        for (var test : tests) {
            reports.add(invokeTest(classInstance, test, beforeMethods, afterMethods));
        }

        var afterClassReport = invokeMethods(classInstance, afterClassMethods, AFTER_CLASS_FAIL);
        if (afterClassReport != null) {
            reports.add(afterClassReport);
        }
        return reports;
    }

    /**
     * Get methods with given annotations from class
     * @param tesClass class to get methods from
     * @param annotation type of annotation
     * @return list of methods which has such annotations
     */
    private static List<Method> classifyMethods(@NotNull Class<?> tesClass, @NotNull Class<? extends Annotation> annotation) {
        return Arrays
                .stream(tesClass.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    /**
     * Run test (and before and after methods)
     * @param classInstance instance of tested class
     * @param test method to test
     * @param beforeMethods to run before test
     * @param afterMethods to run after test
     * @return
     */
    private static TestReport invokeTest(@NotNull Object classInstance, @NotNull Method test, @NotNull List<Method> beforeMethods, @NotNull List<Method> afterMethods) {
        TestReport report = new TestReport(test.getName());
        var testAnnotation = test.getAnnotation(Test.class);
        var expectedException = testAnnotation.expected();

        if (!testAnnotation.ignore().equals(Test.IGNORE)) {
            report.setStatus(IGNORE);
            report.setReason(testAnnotation.ignore());
            return report;
        }

        var beforeReport = invokeMethods(classInstance, beforeMethods, BEFORE_FAIL);
        if (beforeReport != null) {
            beforeReport.setTestName(test.getName());
            return beforeReport;
        }

        long start = System.currentTimeMillis();
        try {
            test.invoke(classInstance);
            report.setTime(System.currentTimeMillis() - start);
            if (expectedException.equals(Test.EXPECTED)) {
                report.setStatus(SUCCESS);
            } else {
                report.setStatus(FAIL);
                report.setReason("Expected exception" + expectedException.getName() + ", but have not found any.");
            }
        } catch (IllegalAccessException e) {
            report.setTime(System.currentTimeMillis() - start);
            report.setStatus(FAIL);
            report.setException(e.getClass());
            report.setReason("Invocation exception: " + e.getCause().getMessage());
        } catch (InvocationTargetException e) {
            report.setTime(System.currentTimeMillis() - start);
            var receivedException = e.getCause().getClass();
            if (expectedException.equals(receivedException)) {
                report.setStatus(SUCCESS);
            } else {
                report.setStatus(FAIL);
                var exceptionName = expectedException.equals(Test.NoException.class) ? "no" : expectedException.getName();
                report.setException(receivedException);
                report.setReason("Expected " + exceptionName + " exception" + ", but found " + receivedException.getName() + " with message: \"" + e.getCause().getMessage() + "\"");
            }
        }

        var afterReport = invokeMethods(classInstance, afterMethods, AFTER_FAIL);
        if (afterReport != null) {
            afterReport.setTestName(test.getName());
            return afterReport;
        }
        return report;
    }

    /**
     * Invoke all given methods
     * @param classInstance instance of tested class
     * @param methods method to test
     * @param failStatus status to set if one
     * @return report of method invocation
     */
    private static TestReport invokeMethods(@NotNull Object classInstance, @NotNull List<Method> methods, @NotNull TestReport.TestStatus failStatus) {
        TestReport report = new TestReport();
        for (Method method : methods) {
            try {
                if (method.getParameters().length != 0) {
                    report.setStatus(failStatus);
                    report.setMethodName(method.getName());
                    report.setReason("Invocation exception: before/after methods should not have parameters.");
                    return report;
                }
                method.invoke(classInstance);
            } catch (Exception e) {
                report.setStatus(failStatus);
                report.setMethodName(method.getName());
                report.setReason("Invocation exception: \"" + e.getCause().getMessage() + "\"");
                report.setException(e.getCause().getClass());
                return report;
            }
        }
        return null;
    }
}
