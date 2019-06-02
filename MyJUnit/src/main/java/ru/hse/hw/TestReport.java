package ru.hse.hw;

import lombok.Getter;
import lombok.Setter;


/**
 * Stores information about test invocation
 */
@Getter
@Setter
public class TestReport {

    /**
     * status of test invocation
     */
    public enum TestStatus {
        SUCCESS, FAIL, IGNORE,
        BEFORE_CLASS_FAIL, AFTER_CLASS_FAIL,
        BEFORE_FAIL, AFTER_FAIL,
    }
    public TestReport() { }

    public TestReport( String testName) {
        this();
        this.testName = testName;
    }

    /**
     * status of test
     */
    private TestStatus status;

    /**
     * name of the tank
     */
    private String testName;

    /**
     * Name of class where innovation if test was failed
     */
    private String methodName;

    /**
     * Received exception when test was failed
     */
    private Class<? extends Throwable> exception;

    /**
     * Reason why test was failed
     */
    private String reason;

    /**
     * Time of test invocation
     */
    private long time;
}
