package ru.hse.hw;

import org.junit.jupiter.api.Test;
import ru.hse.hw.testClasses.*;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectorTest {

    private final static String answerPath = "src/test/java/ru/hse/hw/AnswersForTests/";

    private Reflector reflector = new Reflector();

    private void compileSource(String fileName) {
        var filePath = FileSystems.getDefault().getPath(fileName + ".java").toAbsolutePath();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, filePath.toString());
    }

    private void runClass(String fileName)
            throws IOException, ClassNotFoundException {
        var classesDir = Paths.get("").toAbsolutePath();
        var classLoader = URLClassLoader.newInstance(new URL[]{classesDir.toFile().toURI().toURL()});
        Class.forName(fileName, true, classLoader);
    }

    private boolean checkAnswer(String answerFile, String expectedAnswerFile) throws IOException {
        List<String> answerLines = Files.readAllLines(Paths.get(answerFile), StandardCharsets.UTF_8);
        List<String> expectedAnswerLines = Files.readAllLines(Paths.get(expectedAnswerFile), StandardCharsets.UTF_8);
        var isEqual = true;
        for (int i = 0; i < expectedAnswerLines.size(); i++) {
            if(!answerLines.equals(expectedAnswerLines)){
                isEqual = false;
            }
        }
        return isEqual && answerLines.size() == expectedAnswerLines.size();
    }

    /*
    @Test
    void printStructure() throws IOException, ClassNotFoundException {
        reflector.printStructure(PrettyPrinter.class);
        compileSource("PrettyPrinter");
        runClass("PrettyPrinter");
    }

    @Test
    void diffClasses() throws IOException {
        reflector.diffClasses(Printer.class, Printer.class);
        reflector.diffClasses(PrettyPrinter.class, PrettyPrinter.class);
        reflector.diffClasses(PrettyPrinter.class, Printer.class);
    }

    */
    @Test
    void differentFieldTest() throws IOException {
        reflector.diffClasses(DifferentFieldsFirstClass.class, DifferentFieldsSecondClass.class);
        assertTrue(checkAnswer("DiffDifferentFieldsFirstClassDifferentFieldsSecondClass",
                answerPath + "DifferentFieldsTest"));
    }

    @Test
    void differentMethodTest() throws IOException {
        reflector.diffClasses(DifferentMethodsFirstClass.class, DifferentMethodsSecondClass.class);
        assertTrue(checkAnswer("DiffDifferentMethodsFirstClassDifferentMethodsSecondClass",
                answerPath + "DifferentMethodsTest"));
    }

    @Test
    void equalMethodsFieldsTest() throws IOException {
        reflector.diffClasses(EqualMethodsFieldsFirstClass.class, EqualMethodsFieldsSecondClass.class);
        assertTrue(checkAnswer("DiffEqualMethodsFieldsFirstClassEqualMethodsFieldsSecondClass",
                answerPath + "EqualMethodsFieldsTest"));
    }

    @Test
    void fullOptionsGenericClassTest() throws IOException {

    }

    @Test
    void simpleGenericClassTest() throws IOException {

    }

    @Test
    void GenericClassWithInterfaceAndAncestorTest() throws IOException {

    }

    @Test
    void classWithInnerAndNestedSubclassTest() throws IOException {

    }
}