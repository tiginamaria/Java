package ru.hse.hw;

import org.junit.jupiter.api.Test;
import ru.hse.hw.testClasses.*;

import javax.tools.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectorTest {

    /**
     * path for directory with tests
     */
    private final static String answerPath = "src/test/java/ru/hse/hw/answersForTests/";

    private Reflector reflector = new Reflector();

    /**
     * Compile class with given name
     * @param fileName name of class to compile
     */
    private void compileSource(String fileName) {
        var filePath = FileSystems.getDefault().getPath(fileName + ".java").toAbsolutePath();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, filePath.toString());
    }

    /**
     * Load class with given name
     * @param fileName name of class to load
     * @return leaded class
     * @throws ClassNotFoundException when there is no file with given name
     * @throws MalformedURLException when problems accrued in getting URL of file
     */
    private Class<?> loadClass(String fileName)
            throws ClassNotFoundException, MalformedURLException {
        var classesDir = Paths.get("").toAbsolutePath();
        var classLoader = URLClassLoader.newInstance(new URL[]{classesDir.toFile().toURI().toURL()});
        return Class.forName(fileName, true, classLoader);
    }

    /**
     * Compare file with program answer and expected file
     * @param answerFile name of file with received answer
     * @param expectedAnswerFile name of file with expected answer
     * @return true, if file are equal, false otherwise
     * @throws IOException when problems accrued in getting lines of file
     */
    private boolean checkAnswer(String answerFile, String expectedAnswerFile) throws IOException {
        var answerLines = Files.readAllLines(Paths.get(answerFile), StandardCharsets.UTF_8);
        Collections.sort(answerLines);
        var expectedAnswerLines = Files.readAllLines(Paths.get(expectedAnswerFile), StandardCharsets.UTF_8);
        Collections.sort(expectedAnswerLines);
        return answerLines.equals(expectedAnswerLines);
    }

    /**
     * Get file with different methods and fields of two given classes and compere it with expected file
     * @param a first class to find difference
     * @param b second file to find difference
     * @param expectedAnswerFile file with expected set of different methods and fields
     * @return true, if file are equal, false otherwise
     * @throws IOException IOException when problems accrued in getting lines of file
     */
    private boolean testClassDiff(Class<?> a, Class<?> b, String expectedAnswerFile) throws IOException {
        reflector.diffClasses(a, b);
        return checkAnswer("Diff_" + a.getSimpleName() + "_" + b.getSimpleName(),
                answerPath + expectedAnswerFile);
    }

    /**
     * Get file with structure of class and compere it with expected file
     * @param testClass class to get structure
     * @return true, if file are equal, false otherwise
     * @throws IOException IOException when problems accrued in getting lines of file
     */
    private boolean testClassStructure(Class<?> testClass) throws IOException, ClassNotFoundException {
        Reflector.printStructure(testClass);
        compileSource(testClass.getSimpleName());
        return checkAnswer(loadClass(testClass.getSimpleName()).getSimpleName() + ".java", answerPath + "Expected" + testClass.getSimpleName());
    }

    @Test
    void differentMethodsTest() throws IOException {
        reflector.diffClasses(MethodsClassA.class, MethodsClassB.class);
        assertTrue(testClassDiff(MethodsClassA.class, MethodsClassB.class, "ExpectedDifferentMethods"));
    }

    @Test
    void differentFieldTest() throws IOException {
        reflector.diffClasses(FieldsClassA.class, FieldsClassB.class);
        assertTrue(testClassDiff(FieldsClassA.class, FieldsClassB.class, "ExpectedDifferentFields"));
    }

    @Test
    void equalMethodsFieldsTest() throws IOException {
        reflector.diffClasses(MethodsFieldsClassA.class, MethodsFieldsClassB.class);
        assertTrue(testClassDiff(MethodsFieldsClassA.class, MethodsFieldsClassB.class, "EmptyFile"));
    }

    @Test
    void simpleGenericClassTest() throws IOException, ClassNotFoundException {
        var testClass = SimpleGenericClass.class;
        assertTrue(testClassStructure(testClass));
        assertTrue(testClassDiff(testClass, loadClass(testClass.getSimpleName()), "EmptyFile"));
    }

    @Test
    void genericClassWithInterfaceAndAncestorTest() throws IOException, ClassNotFoundException {
        var testClass = ClassWithInterfaceAncestor.class;
        assertTrue(testClassStructure(testClass));
        assertTrue(testClassDiff(testClass, loadClass(testClass.getSimpleName()), "EmptyFile"));
    }

    @Test
    void classWithInnerAndNestedSubclassTest() throws IOException, ClassNotFoundException {
        var testClass = ClassWithSubclasses.class;
        assertTrue(testClassStructure(ClassWithSubclasses.class));
        assertTrue(testClassDiff(testClass,  loadClass(testClass.getSimpleName()), "EmptyFile"));
    }

    @Test
    void complexGenericClassTest() throws IOException, ClassNotFoundException {
        var testClass = ComplexGenericClass.class;
        assertTrue(testClassStructure(testClass));
        assertTrue(testClassDiff(testClass, loadClass(testClass.getSimpleName()), "EmptyFile"));
    }
}