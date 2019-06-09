package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyJUnitTest {

    @Test
    void testsWrongExceptionsTest() {
        var myJUnit = new MyJUnit();
        var reports = myJUnit.runAll(Arrays.asList(SuccessSample.class));
        for (var report : reports) {
            if (report.getTestName() == null) {
                assertEquals(TestReport.TestStatus.SUCCESS, report.getStatus());
                continue;
            }
            switch (report.getTestName()) {
                case "test1":
                    assertEquals(TestReport.TestStatus.SUCCESS, report.getStatus());
                    break;

                case "test2":
                    assertEquals(TestReport.TestStatus.FAIL, report.getStatus());
                    assertEquals(java.lang.RuntimeException.class, report.getException());
                    break;

                case "test3":
                    assertEquals(TestReport.TestStatus.IGNORE, report.getStatus());
                    assertEquals(report.getReason(), "ignore with exception");
                    break;

                case "test4":
                    assertEquals(TestReport.TestStatus.FAIL, report.getStatus());
                    break;

                case "test5":
                    assertEquals(TestReport.TestStatus.IGNORE, report.getStatus());
                    assertEquals(report.getReason(), "why not");
                    break;

                case "test6":
                    assertEquals(TestReport.TestStatus.SUCCESS, report.getStatus());
                    break;
            }
        }
    }

    @Test
    void testsBeforeFailTest() {
        var myJUnit = new MyJUnit();
        var reports = myJUnit.runAll(Arrays.asList(SuccessSample.class));
        for (var report : reports) {
            if (report.getTestName() == null) {
                assertEquals(TestReport.TestStatus.SUCCESS, report.getStatus());
                continue;
            }
            switch (report.getTestName()) {
                case "test1":
                    assertEquals(TestReport.TestStatus.BEFORE_FAIL, report.getStatus());
                    assertEquals(java.lang.IllegalAccessException.class, report.getException());
                    break;

                case "test2":
                    assertEquals(TestReport.TestStatus.BEFORE_FAIL, report.getStatus());
                    assertEquals(java.lang.IllegalAccessException.class, report.getException());
                    break;

                case "test3":
                    assertEquals(TestReport.TestStatus.IGNORE, report.getStatus());
                    assertEquals(report.getReason(), "ignore with exception");
                    break;

                case "test4":
                    assertEquals(TestReport.TestStatus.BEFORE_FAIL, report.getStatus());
                    assertEquals(java.lang.IllegalAccessException.class, report.getException());
                    break;

                case "test5":
                    assertEquals(TestReport.TestStatus.IGNORE, report.getStatus());
                    assertEquals(report.getReason(), "why not");
                    break;

                case "test6":
                    assertEquals(TestReport.TestStatus.BEFORE_FAIL, report.getStatus());
                    assertEquals(java.lang.IllegalAccessException.class, report.getException());
                    break;
            }
        }
    }
}