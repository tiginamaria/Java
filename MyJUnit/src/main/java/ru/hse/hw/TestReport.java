package ru.hse.hw;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;


/**
 * Stores information about test invocation
 */
@Getter
@Setter
public class TestReport {

    /** Status of test invocation. */
    public enum TestStatus {
        SUCCESS, FAIL, IGNORE,
        BEFORE_CLASS_FAIL, AFTER_CLASS_FAIL,
        BEFORE_FAIL, AFTER_FAIL,
    }

    public TestReport(Class<?> testClass) {
        this.status = TestStatus.SUCCESS;
        this.testClass = testClass;
        try {
            this.instance = testClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            status = TestStatus.FAIL;
            exception = e.getClass();
            reason = "creating instance exeption :" + e.getMessage();
        }
    }

    public TestReport(Method test, Class<?> testClass) {
        this(testClass);
        this.test = test;
        this.testName = test.getName();
    }

    /** Test. */
    private Method test;

    /** Class of test. */
    private Class<?> testClass;

    /** Class instance. */
    private Object instance;

    /** Status of test. */
    private TestStatus status;

    /** Test name. */
    private String testName;

    /** Name of class where innovation if test was failed. */
    private String methodName;

    /** Received exception when test was failed. */
    private Class<? extends Throwable> exception;

    /** Reason why test was failed. */
    private String reason;

    /** Time of test invocation. */
    private long time;
}
