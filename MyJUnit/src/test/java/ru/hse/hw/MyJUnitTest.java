package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hse.hw.TestReport.Status.*;
import static ru.hse.hw.TestReport.Tag.BEFORE;

class MyJUnitTest {

    @Test
    void testsWrongExceptionsTest() {
        var myJUnit = new MyJUnit();
        var reports = myJUnit.runAll(Arrays.asList(SuccessSample.class, SuccessSample.class));
        for (var report : reports) {
            switch (report.getTag()) {
                case BEFORE_CLASS:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case AFTER_CLASS:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case BEFORE:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case AFTER:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case TEST:
                    switch (report.getTestName()) {
                        case "test1":
                            assertEquals(SUCCESS, report.getStatus());
                            break;

                        case "test2":
                            assertEquals(FAIL, report.getStatus());
                            assertEquals(java.lang.RuntimeException.class, report.getException());
                            break;

                        case "test3":
                            assertEquals(IGNORE, report.getStatus());
                            assertEquals(report.getReason(), "ignore with exception");
                            break;

                        case "test4":
                            assertEquals(FAIL, report.getStatus());
                            break;

                        case "test5":
                            assertEquals(IGNORE, report.getStatus());
                            assertEquals(report.getReason(), "why not");
                            break;

                        case "test6":
                            assertEquals(SUCCESS, report.getStatus());
                            break;
                    }
                    break;
            }
        }
    }

    @Test
    void testsBeforeFailTest() {
        var myJUnit = new MyJUnit();
        var reports = myJUnit.runAll(Arrays.asList(SuccessSample.class));
        for (var report : reports) {
            switch (report.getTag()) {
                case BEFORE_CLASS:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case AFTER_CLASS:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case BEFORE:
                    switch (report.getTestName()) {
                        case "test1":
                            assertEquals(FAIL, report.getStatus());
                            assertEquals(java.lang.IllegalAccessException.class, report.getException());
                            break;

                        case "test2":
                            assertEquals(FAIL, report.getStatus());
                            assertEquals(java.lang.IllegalAccessException.class, report.getException());
                            break;

                        case "test4":
                            assertEquals(FAIL, report.getStatus());
                            assertEquals(java.lang.IllegalAccessException.class, report.getException());
                            break;
                    }
                    break;
                case AFTER:
                    assertEquals(SUCCESS, report.getStatus());
                    break;
                case TEST:
                    switch (report.getTestName()) {
                        case "test3":
                            assertEquals(IGNORE, report.getStatus());
                            assertEquals(report.getReason(), "ignore with exception");
                            break;

                        case "test5":
                            assertEquals(IGNORE, report.getStatus());
                            assertEquals(report.getReason(), "why not");
                            break;

                    }
            }
        }
    }
}