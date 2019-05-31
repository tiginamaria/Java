package ru.hse.hw;

import ru.hse.hw.samples.Sample;

public class Main {

    public static void main(String[] args) {
        var testClass = Sample.class;
        var reports = TestRunner.run(testClass);

        for (var report : reports) {
            printReport(report);
        }
    }

    private static void printReport(TestReport report) {
        switch (report.getStatus()) {
            case FAIL:
                System.out.println(report.getTestName() +  " "  + report.getStatus() + ": " + report.getReason());
                break;
            case IGNORE:
                System.out.println(report.getTestName() + " " + report.getStatus() + ": " + report.getReason());
                break;
            case SUCCESS:
                System.out.println(report.getTestName() + " " + report.getStatus() + ": " + report.getReason());
                break;
            case BEFORE_CLASS_FAIL:
                System.out.println(report.getTestName() + " " + report.getMethodName() + " " + report.getStatus() + ": " + report.getReason());
                break;
            case AFTER_CLASS_FAIL:
                System.out.println(report.getTestName() + " " + report.getMethodName() + " " + report.getStatus() + ": " + report.getReason());
                break;
            case BEFORE_FAIL:
                System.out.println(report.getTestName() + " " + report.getMethodName() + " " + report.getStatus() + ": " + report.getReason());
                break;
            case AFTER_FAIL:
                System.out.println(report.getTestName() + " " + report.getMethodName() + " " + report.getStatus() + ": " + report.getReason());
                break;
        }
    }
}
