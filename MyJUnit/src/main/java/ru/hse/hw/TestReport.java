package ru.hse.hw;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestReport {

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

    private TestStatus status;

    private String testName;

    private String methodName;

    private String reason;

    private long time;
}
