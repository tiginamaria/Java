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
     * @throws InstantiationException when exception occur in testing given
     * @throws IllegalAccessException when exception occur in testing given
     */
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        var pathName = args[0];
        var path = Paths.get(pathName);
        var reports = new ArrayList<TestReport>();
        if (!Files.exists(path)) {
            System.out.format("Wrong parameters: path %s does not exist", pathName);
        } else {
            if (pathName.endsWith(".class")) {
                reports.addAll(MyJUnit.run(getClass(path)));
            } else if (pathName.endsWith(".jar")) {
                try {
                    JarInputStream jarFile = new JarInputStream(new FileInputStream(pathName));
                    JarEntry entry;

                    while ((entry = jarFile.getNextJarEntry()) != null) {
                        if (entry.getName().endsWith(".class")) {
                            var entryPath = Paths.get(entry.getName());
                            reports.addAll(MyJUnit.run(getClass(entryPath)));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.format("Wrong parameters: no .class or .jar files was found on path: %s", pathName);
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
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { path.toFile().toURI().toURL()});
        String fileName = path.getFileName().toString().replaceFirst("\\.class$", "");
        var testClass = classLoader.loadClass(fileName);
        return testClass;
    }

    private static void printReport(TestReport report) {
        switch (report.getStatus()) {
            case FAIL:
                System.out.println("Test " + report.getTestName() + " failed. " +  "Report: <<" + report.getReason() + ">>");
                break;
            case IGNORE:
                System.out.println("Test " + report.getTestName() + " ignored. " + "Reason: " + report.getReason() + ">");
                break;
            case SUCCESS:
                System.out.println("Test " + report.getTestName() + " succeeded. " + "Time: " + report.getTime() + "ms");
                break;
            case BEFORE_CLASS_FAIL:
                System.out.println("All test failed. " + "Before class method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case AFTER_CLASS_FAIL:
                System.out.println("All test failed. " + "After class method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case BEFORE_FAIL:
                System.out.println("Test " + report.getTestName() + " failed. " + "Before test method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
            case AFTER_FAIL:
                System.out.println("Test " + report.getTestName() + " failed. " + "After test method " + report.getMethodName() + " terminated with report: <<" + report.getReason() + ">>");
                break;
        }
    }
}
