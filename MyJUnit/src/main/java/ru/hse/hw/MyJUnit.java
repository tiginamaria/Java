package ru.hse.hw;

import org.jetbrains.annotations.NotNull;
import ru.hse.hw.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    private Map<Class<?>, List<Method>> beforeMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> afterMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> beforeClassMethods = new HashMap<>();
    private Map<Class<?>, List<Method>> afterClassMethods = new HashMap<>();
    private List<TestReport> tests = new ArrayList<>();
    private List<TestReport> reports = new ArrayList<>();

    public List<TestReport> runAll(List<Class<?>> testClasses) {

        for (var testClass : testClasses) {
            beforeClassMethods.put(testClass, classifyMethods(testClass, BeforeClass.class));
            afterClassMethods.put(testClass, classifyMethods(testClass, AfterClass.class));
            beforeMethods.put(testClass, classifyMethods(testClass, Before.class));
            afterMethods.put(testClass, classifyMethods(testClass, After.class));
            tests.addAll(classifyMethods(testClass, Test.class)
                    .stream().map(test -> new TestReport(test, testClass)).collect(Collectors.toList()));
        }

        beforeClassMethods.entrySet().forEach(e -> {
            var report = new TestReport(e.getKey());
            invokeMethods(report, BEFORE_CLASS_FAIL);
            if (report.getStatus() == BEFORE_CLASS_FAIL) {
                reports.add(report);
            }
        });

        reports.addAll(tests.parallelStream().map(this::buildReport).collect(Collectors.toList()));

        afterClassMethods.entrySet().forEach(e -> {
            var report = new TestReport(e.getKey());
            invokeMethods(report, AFTER_CLASS_FAIL);
            if (report.getStatus() == AFTER_CLASS_FAIL) {
                reports.add(report);
            }
        });

        return reports;
    }


    public TestReport buildReport(TestReport report) {
        var test = report.getTest();
        var testAnnotation = test.getAnnotation(Test.class);

        if (!testAnnotation.ignore().equals(Test.IGNORE)) {
            report.setStatus(IGNORE);
            report.setReason(testAnnotation.ignore());
        }

        invokeMethods(report, BEFORE_FAIL);

        invokeTest(report);

        invokeMethods(report, AFTER_FAIL);

        return report;
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
    }


    private void invokeMethods(TestReport report, @NotNull TestReport.TestStatus checkStatus) {
        if (report.getStatus() != SUCCESS) {
            return;
        }

        var testClass = report.getTestClass();
        List<Method> methods;

        switch (checkStatus) {
            case BEFORE_CLASS_FAIL:
                methods = beforeClassMethods.get(testClass);
                break;
            case AFTER_CLASS_FAIL:
                methods = afterClassMethods.get(testClass);
                break;
            case BEFORE_FAIL:
                methods = beforeMethods.get(testClass);
                break;
            case AFTER_FAIL:
                methods = afterMethods.get(testClass);
                break;
            default:
                return;
        }

        for (Method method : methods) {
            try {
                if (method.getParameters().length != 0) {
                    report.setStatus(checkStatus);
                    report.setMethodName(method.getName());
                    report.setReason("Invocation exception: before/after methods should not have parameters.");
                }
                method.invoke(report.getInstance());
            } catch (Exception e) {
                report.setStatus(checkStatus);
                report.setMethodName(method.getName());
                report.setReason("Invocation exception: \"" + e.getCause().getMessage() + "\"");
                report.setException(e.getCause().getClass());
            }
        }
    }
}
