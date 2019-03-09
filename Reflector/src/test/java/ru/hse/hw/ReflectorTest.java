package ru.hse.hw;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

class ReflectorTest {

    Reflector reflector = new Reflector();

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
}