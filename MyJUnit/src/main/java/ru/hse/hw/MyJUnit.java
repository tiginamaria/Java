package ru.hse.hw;

import org.jetbrains.annotations.NotNull;
import ru.hse.hw.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static ru.hse.hw.TestReport.Status.*;
import static ru.hse.hw.TestReport.Tag.*;

/**
 * MyJUnit class run test methods of given class.
 * MyJUnit do not run tests with annotations ignore.
 * MyJUnit consider test succeeded if it terminated with exception from annotation or without any exception if annotation does not contains exception.
 * MyJUnit consider test failed if it terminated with exception different from exception from annotation or without any exception when annotation contains exception.
 * Before and after every test MyJUnit runs methods of give class with annotations @Before and @After.
 * Before and after all tests MyJUnit runs methods of give class with annotations @BeforeClass and @AfterClass.
 */
public class MyJUnit {

    private Map<Class<?>, List<Method>> beforeMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> afterMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> beforeClassMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> afterClassMethods = new HashMap<>();
    private List<TestReport> tests = new ArrayList<>();
    private List<TestReport> reports = new ArrayList<>();

    /**
     * Build all reports about all tests in given class.
     * @param testClasses to build reports for them
     * @return list with reports
     */
    public List<TestReport> runAll(List<Class<?>> testClasses) {

        for (var testClass : testClasses) {
            beforeClassMethods.put(testClass, classifyMethods(testClass, BeforeClass.class));
            afterClassMethods.put(testClass, classifyMethods(testClass, AfterClass.class));
            beforeMethods.put(testClass, classifyMethods(testClass, Before.class));
            afterMethods.put(testClass, classifyMethods(testClass, After.class));
            tests.addAll(classifyMethods(testClass, Test.class)
                    .stream().map(test -> new TestReport(test, testClass, TEST)).collect(Collectors.toList()));
        }

        beforeClassMethods.entrySet().forEach(e -> {
            var report = new TestReport(e.getKey(), BEFORE_CLASS);
            invokeMethods(report, BEFORE_CLASS);
            if (report.getStatus() == FAIL) {
                reports.add(report);
            }
        });

        reports.addAll(tests.parallelStream().map(this::buildReport).collect(Collectors.toList()));

        afterClassMethods.entrySet().forEach(e -> {
            var report = new TestReport(e.getKey(), AFTER_CLASS);
            invokeMethods(report, AFTER_CLASS);
            if (report.getStatus() == FAIL) {
                reports.add(report);
            }
        });

        return reports;
    }


    /**
     * Builds report about test.
     * @param report to put information about test
     * @return report about test
     */
    public TestReport buildReport(TestReport report) {
        var test = report.getTest();
        var testAnnotation = test.getAnnotation(Test.class);

        if (!testAnnotation.ignore().equals(Test.IGNORE)) {
            report.setStatus(IGNORE);
            report.setReason(testAnnotation.ignore());
        }

        invokeMethods(report, BEFORE);

        invokeTest(report);

        invokeMethods(report, AFTER);

        return report;
    }

    /**
     * Get methods with given annotations from class.
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
     * Invoke test.
     * @param report to put information about test invoke
     */
    private void invokeTest(TestReport report) {
        if (report.getStatus() != SUCCESS) {
            return;
        }

        var test = report.getTest();
        var testAnnotation = test.getAnnotation(Test.class);
        var expectedException = testAnnotation.expected();

        long start = System.currentTimeMillis();
        try {
            test.invoke(report.getInstance());
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
            var receivedException = e.getCause().getClass() != null ? e.getCause().getClass() : e.getClass();
            var message = e.getCause().getMessage() != null ? e.getCause().getMessage() : e.getMessage();
            if (expectedException.equals(receivedException)) {
                report.setStatus(SUCCESS);
            } else {
                report.setStatus(FAIL);
                var exceptionName = expectedException.equals(Test.NoException.class) ? "no" : expectedException.getName();
                report.setException(receivedException);
                report.setReason("Expected " + exceptionName + " exception" + ", but found " + receivedException.getName() + " with message: \"" + message + "\"");
            }
        }
    }

    /**
     * Invoke method.
     * @param report to put information about method invoke
     * @param tag type of annotation om method
     */
    private void invokeMethods(TestReport report, TestReport.Tag tag) {
        if (report.getStatus() != SUCCESS) {
            return;
        }

        var testClass = report.getTestClass();
        List<Method> methods;

        switch (tag) {
            case BEFORE_CLASS:
                methods = beforeClassMethods.get(testClass);
                break;
            case AFTER_CLASS:
                methods = afterClassMethods.get(testClass);
                break;
            case BEFORE:
                methods = beforeMethods.get(testClass);
                break;
            case AFTER:
                methods = afterMethods.get(testClass);
                break;
            default:
                return;
        }

        for (Method method : methods) {
            try {
                if (method.getParameters().length != 0) {
                    setStatusFail(report, tag, method,
                            "Invocation exception: before/after methods should not have parameters.", null);
                }
                if (Modifier.isAbstract(method.getModifiers())) {
                    setStatusFail(report, tag, method,
                            "Invocation exception: before/after methods should not be abstract.", null);
                }
                if (method.getReturnType() != void.class) {
                    setStatusFail(report, tag, method,
                            "Invocation exception: before/after methods should have void return type.", null);
                }
                if ((tag == BEFORE_CLASS || tag == AFTER_CLASS) &&
                        !Modifier.isStatic(method.getModifiers())) {
                    setStatusFail(report, tag, method,
                            "Invocation exception: before/after class methods be static.", null);
                }
                method.setAccessible(true);
                method.invoke(report.getInstance());
            } catch (Exception e) {
                setStatusFail(report, tag, method,
                        "Invocation exception: \"" + e.getMessage() + "\"", e);
            }
        }
    }

    private void setStatusFail(TestReport report, TestReport.Tag tag, Method method, String reason, Exception e) {
        if (report.getStatus() != SUCCESS) {
            return;
        }
        report.setStatus(FAIL);
        report.setTag(tag);
        report.setMethodName(method.getName());
        report.setReason(reason);
        if (e != null) {
            report.setException(e.getCause().getClass());
        }
    }
}
