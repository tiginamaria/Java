package ru.hse.hw;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Console utility to run tests and print reports about test invocation
 */
public class Main {

    /**
     * Start test invocation from parsing path with .jar and .class path
     * @param args argument of recipe
     * @throws MalformedURLException when exception occur in getting loaded class
     * @throws ClassNotFoundException when exception occur in getting loaded class
     */
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.format("Wrong parameters: only one parameter required");
            return;
        }
        var pathName = args[0];
        var path = Paths.get(pathName);
        var reports = new ArrayList<TestReport>();
        var myJUnit = new MyJUnit();

        if (!Files.exists(path)) {
            System.out.format("Wrong parameters: path %s does not exist", pathName);
            return;
        }

        if (!(pathName.endsWith(".class") || pathName.endsWith(".jar"))) {
            System.out.format("Wrong parameters: no .class or .jar files was found on path: %s", pathName);
            return;
        }

        if (pathName.endsWith(".class")) {
            reports.addAll(myJUnit.runAll(Arrays.asList(getClass(path))));
        }

        if (pathName.endsWith(".jar")) {
            try {
                var jarFile = new JarInputStream(new FileInputStream(pathName));
                JarEntry entry;
                var testClasses = new ArrayList<Class<?>>();
                while ((entry = jarFile.getNextJarEntry()) != null) {
                    if (entry.getName().endsWith(".class")) {
                        var entryPath = Paths.get(entry.getName());
                        testClasses.add(getClass(entryPath));
                    }
                }
                reports.addAll(myJUnit.runAll(testClasses));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (var report : reports) {
            printReport(report);
        }
    }

    /**
     * Get class from path
     * @param path given path
     * @return class from path
     * @throws ClassNotFoundException when exception occur in getting loaded class
     * @throws MalformedURLException when exception occur in getting loaded class
     */
    private static Class<?> getClass(Path path) throws ClassNotFoundException, MalformedURLException {
        var classLoader = URLClassLoader.newInstance(new URL[] { path.toFile().toURI().toURL()});
        var fileName = path.getFileName().toString().replaceFirst("\\.class$", "");
        var testClass = classLoader.loadClass(fileName);
        return testClass;
    }

    private static void printReport(TestReport report) {
        switch (report.getTag()) {
            case BEFORE_CLASS:
                System.out.println(report.getTestClass().getName() + ": All test failed. " + "Before class method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case AFTER_CLASS:
                System.out.println(report.getTestClass().getName() + ": All test failed. " + "After class method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case BEFORE:
                System.out.println(report.getTestClass().getName() + ": Test " + report.getTestName() + " failed. " + "Before test method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case AFTER:
                System.out.println(report.getTestClass().getName() + ": Test " + report.getTestName() + " failed. " + "After test method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case TEST:
                switch (report.getStatus()) {
                    case FAIL:
                        System.out.println(report.getTestClass().getName() + ": Test " + report.getTestName() + " failed. " + "Report: <<" + report.getReason() + ">>");
                        break;
                    case IGNORE:
                        System.out.println(report.getTestClass().getName() + ": Test " + report.getTestName() + " ignored. " + "Reason: " + report.getReason() + ">");
                        break;
                    case SUCCESS:
                        System.out.println(report.getTestClass().getName() + ": Test " + report.getTestName() + " succeeded. " + "Time: " + report.getTime() + "ms");
                        break;
                }
        }
    }
}
