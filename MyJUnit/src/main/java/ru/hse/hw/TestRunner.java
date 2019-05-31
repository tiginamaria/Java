package ru.hse.hw;

import ru.hse.hw.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.hse.hw.TestReport.TestStatus.*;

public class TestRunner {

    public static List<TestReport> run(Class testClass) {

        var beforeClassMethods = classifyMethods(testClass, BeforeClass.class);
        var afterClassMethods = classifyMethods(testClass, AfterClass.class);
        var beforeMethods = classifyMethods(testClass, Before.class);
        var afterMethods = classifyMethods(testClass, After.class);
        var tests = classifyMethods(testClass, Test.class);

        var reports = new ArrayList<TestReport>();

        var beforeClassReport = invokeMethods(beforeClassMethods, BEFORE_CLASS_FAIL);
        if (beforeClassReport != null) {
            reports.add(beforeClassReport);
        }

        for (var test : tests) {
            reports.add(invokeTest(test, beforeMethods, afterMethods));
        }

        var afterClassReport = invokeMethods(afterClassMethods, AFTER_CLASS_FAIL);
        if (afterClassReport != null) {
            reports.add(afterClassReport);
        }
        return reports;
    }

    private static List<Method> classifyMethods(Class testClass, Class<? extends Annotation> annotation) {
        return Arrays
                .stream(testClass.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    public static TestReport invokeTest(Method test, List<Method> beforeMethods, List<Method> afterMethods) {
        TestReport report = new TestReport(test.getName());
        var testAnnotation = test.getAnnotation(Test.class);
        var expectedException = testAnnotation.expected();

        if (!testAnnotation.ignore().equals(Test.IGNORE)) {
            report.setStatus(IGNORE);
            report.setReason(testAnnotation.ignore());
            return report;
        }

        var beforeReport = invokeMethods(beforeMethods, BEFORE_FAIL);
        if (beforeReport != null) {
            beforeReport.setTestName(test.getName());
            return beforeReport;
        }

        long start = System.currentTimeMillis();
        try {
            test.invoke(null);
            report.setTime(System.currentTimeMillis() - start);
            if (expectedException.equals(Test.EXPECTED)) {
                report.setStatus(SUCCESS);
            } else {
                report.setStatus(FAIL);
            }
        } catch (IllegalAccessException e) {
            report.setTime(System.currentTimeMillis() - start);
            report.setStatus(FAIL);
            report.setReason(e.getCause().getMessage());
        } catch (InvocationTargetException e) {
            report.setTime(System.currentTimeMillis() - start);
            var receivedException = e.getCause().getClass();
            if (expectedException.equals(receivedException)) {
                report.setStatus(SUCCESS);
            } else {
                report.setStatus(FAIL);
                report.setReason(e.getCause().getMessage());
            }
        }

        var afterReport = invokeMethods(beforeMethods, AFTER_FAIL);
        if (afterReport != null) {
            afterReport.setTestName(test.getName());
            return afterReport;
        }

        return report;
    }

    public static TestReport invokeMethods(List<Method> methods, TestReport.TestStatus failStatus) {
        for (Method method : methods) {
            try {
                method.invoke(null);
            } catch (Exception e) {
                TestReport report = new TestReport();
                report.setStatus(failStatus);
                report.setMethodName(method.getName());
                report.setReason(e.getCause().getMessage());
                return report;
            }
        }
        return null;
    }
}


