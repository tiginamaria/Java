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
    public enum Status {
        SUCCESS, FAIL, IGNORE
    }

    /** Status of test invocation. */
    public enum Tag {
        BEFORE_CLASS, AFTER_CLASS,
        BEFORE, AFTER,
        TEST
    }

    public TestReport(Class<?> testClass, Tag tag) {
        this.status = Status.SUCCESS;
        this.tag = tag;
        this.testClass = testClass;
        try {
            this.instance = testClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            status = Status.FAIL;
            exception = e.getClass();
            reason = "creating instance exeption :" + e.getMessage();
        }
    }

    public TestReport(Method test, Class<?> testClass, Tag tag) {
        this(testClass, tag);
        this.test = test;
        this.testName = test.getName();
    }

    /** Tested annotation tag. */
    private Tag tag;

    /** Test. */
    private Method test;

    /** Class of test. */
    private Class<?> testClass;

    /** Class instance. */
    private Object instance;

    /** Status of test. */
    private Status status;

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
