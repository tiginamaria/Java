package ru.hse.hw;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;

public class Reflector {

    private static FileWriter out;

    private static int tabs;

    public static void printStructure(Class<?> someClass) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( someClass.getSimpleName() + ".java", false)) {
            cascadePrintStructure(someClass, fileOutputStream, 0);
        }
    }

    public static void cascadePrintStructure(@NotNull Class<?> someClass, FileWriter fileOutputStream, int tabulations) throws IOException {
        out = fileOutputStream;
        tabs = tabulations;
        printModifiers(someClass.getModifiers());
        printClassName(someClass);
        printGenericTypeParameters(someClass);
        printExtendedClass(someClass);
        printImplementedInterfaces(someClass);
        out.write("{\n");
        for (var subclass : someClass.getDeclaredClasses()) {
            cascadePrintStructure(subclass, out, tabulations + 1);
        }
        printFields(someClass);
        printConstructors(someClass);
        printMethods(someClass);
        out.write("}");
    }

    private static void printFields(@NotNull Class<?> someClass) throws IOException {
        Field[] fields = someClass.getDeclaredFields();
        for (var field : fields) {
            printModifiers(field.getModifiers());
            out.write(getTypeName(field.getGenericType()) + " ");
            out.write(field.getName());
            if (Modifier.isFinal(field.getModifiers())) {
                out.write(" = " + defaultValue(field.getGenericType()));
            }
            out.write(";\n");
        }
    }

    private static String defaultValue(Type type) {
        if (type instanceof Class) {
            var typeClass = (Class<?>) type;
            if (typeClass.isPrimitive()) {
                var value = Array.newInstance((Class<?>) type, 1);
                return Array.get(value, 0).toString();
            }
        }
        return null;
    }

    private static void printMethods(@NotNull Class<?> someClass) throws IOException {
        Method[] methods = someClass.getDeclaredMethods();
        for (var method : methods) {
            printModifiers(method.getModifiers());
            out.write(method.getReturnType().getName() + " ");
            out.write(method.getName() + "(");
            printParameters(method.getGenericParameterTypes(), method.getParameters());
            if (method.getReturnType() == void.class) {
                out.write(") " + "{ }" + "\n");
            } else {
                out.write(") " + "{\n");
                out.write("return " + defaultValue(method.getReturnType()) + ";");
                out.write("\n}\n");
            }
        }
    }

    private static void printConstructors(@NotNull Class<?> someClass) throws IOException {
        Constructor[] constructors = someClass.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            printModifiers(constructor.getModifiers());
            out.write(constructor.getDeclaringClass().getSimpleName());
            out.write("(");
            printParameters(constructor.getGenericParameterTypes(), constructor.getParameters());
            out.write(") " + "{ }" + "\n");
        }
    }

    private static void printClassName(@NotNull Class<?> someClass) throws IOException {
        out.write("class " + someClass.getSimpleName());
    }

    private static void printGenericTypeParameters(@NotNull Class<?> someClass) throws IOException {
        var typeParameters = someClass.getTypeParameters();
        if (typeParameters.length > 0) {
            out.write("<");
            out.write(typeParameters[0].getName());
            for (int i = 1; i < typeParameters.length; i++) {
                out.write(", " + typeParameters[i].getName());
            }
            out.write(">");
            out.write(" ");
        }
    }

    private static void printImplementedInterfaces(@NotNull Class<?> someClass) throws IOException {
        Type[] interfaces = someClass.getGenericInterfaces();
        if (interfaces.length > 0) {
            out.write("implements ");
            out.write(getTypeName(interfaces[0]));
            for (int i = 1; i < interfaces.length; i++) {
                out.write(", " + getTypeName(interfaces[i]));
            }
            out.write(" ");
        }
    }

    private static void printExtendedClass(@NotNull Class<?> someClass) throws IOException {
        Type ancestor = someClass.getGenericSuperclass();
        if (ancestor != null) {
            out.write("extends " + getTypeName(ancestor));
            out.write(" ");
        }
    }

    private static void printModifiers(@NotNull int mods) throws IOException {
        out.write(Modifier.toString(mods));
        if (mods != 0) {
            out.write(" ");
        }
    }

    private static void printParameters(@NotNull Type[] types,@NotNull Parameter[] parameters) throws IOException {
        if (types.length > 0) {
            out.write(getTypeName(types[0]) + " " + parameters[0].getName());
            for (int i = 1; i < types.length; i++) {
                out.write(", " + getTypeName(types[i]) + " " + parameters[i].getName());
            }
        }
    }

    @NotNull
    private static String getTypeName(@NotNull Type type) {
        return type.getTypeName().replace('$', '.');
    }

    void diffClasses(Class<?> a, Class<?> b) {

    }
}