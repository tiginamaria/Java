package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.StrictMath.abs;

public class Reflector {

    private static FileWriter out;

    private static int tabs;

    public static void printStructure(Class<?> someClass) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( someClass.getSimpleName() + ".java", false)) {
            PrintClass(someClass, fileOutputStream, 0);
        }
    }

    public static void PrintClass(@NotNull Class<?> someClass, FileWriter fileOutputStream, int tabulations) throws IOException {
        out = fileOutputStream;
        tabs = tabulations;
        printModifiers(someClass.getModifiers());
        printClassName(someClass);
        printGenericTypeParameters(someClass);
        printExtendedClass(someClass);
        printImplementedInterfaces(someClass);
        out.write("{\n");
        for (var subclass : someClass.getDeclaredClasses()) {
            PrintClass(subclass, out, tabulations + 1);
        }
        printFields(someClass);
        printConstructors(someClass);
        printMethods(someClass);
        --tabs;
        printTabs();
        out.write("}");
    }

    private static void printFields(@NotNull Class<?> someClass) throws IOException {
        Field[] fields = someClass.getDeclaredFields();
        for (var field : fields) {
            printField(field);
        }
    }

    private static void printField(@NotNull Field field) throws IOException {
        printTabs();
        printModifiers(field.getModifiers());
        out.write(getTypeName(field.getGenericType()) + " ");
        out.write(field.getName());
        if (Modifier.isFinal(field.getModifiers())) {
            out.write(" = " + defaultValue(field.getGenericType()));
        }
        out.write(";\n");
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
            printMethod(method);
        }
    }

    private static void printMethod(@NotNull Method method) throws IOException {
        printTabs();
        printModifiers(method.getModifiers());
        out.write(method.getGenericReturnType() + " ");
        out.write(method.getName() + "(");
        printParameters(method.getGenericParameterTypes(), method.getParameters());
        if (method.getReturnType() == void.class) {
            out.write(") " + "{ }" + "\n");
        } else {
            out.write(") " + "{\n");
            printTabs();
            out.write("\treturn " + defaultValue(method.getReturnType()) + ";\n");
            printTabs();
            out.write("}\n");
        }
    }


    private static void printConstructors(@NotNull Class<?> someClass) throws IOException {
        Constructor[] constructors = someClass.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            printTabs();
            printModifiers(constructor.getModifiers());
            out.write(constructor.getDeclaringClass().getSimpleName());
            out.write("(");
            printParameters(constructor.getGenericParameterTypes(), constructor.getParameters());
            out.write(") " + "{ }" + "\n");
        }
    }

    private static void printClassName(@NotNull Class<?> someClass) throws IOException {
        printTabs();
        out.write("class " + someClass.getSimpleName());
        ++tabs;
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

    private static void printTabs() throws IOException {
        for (int i = 0; i < tabs; i++) {
            out.write('\t');
        }
    }

    @NotNull
    private static String getTypeName(@NotNull Type type) {
        return type.getTypeName().replace('$', '.');
    }

    private static <T> List<T> diff(T[] aList, T[] bList, Comparator<T> comparator) {
        var listDiff = new ArrayList<T>();
        for (var a : aList) {
            boolean found = false;
            for (var b : bList) {
                if (comparator.compare(a, b) != 0) {
                    found = true;
                }
            }
            if (!found) {
                listDiff.add(a);
            }
        }
        return listDiff;
    }

    private static int compareName(String a, String b) {
        return a.equals(b) ? 0 : 1;
    }

    private static int compareModifiers(int a, int b) {
        return abs(a - b);
    }

    private static int compareType(Type a, Type b) {
        return a.equals(b) ? 0 : 1;
    }

    private static int compareReturnType(Type a, Type b) {
        return compareType(a, b);
    }

    private static int compareParametersType(Type[] aTypes, Type[] bTypes) {
        return aTypes.equals(bTypes) ? 0 : 1;
    }

    private static int compareExceptions(Type[] aTypes, Type[] bTypes) {
        return aTypes.equals(bTypes) ? 0 : 1;
    }


    private static List<Field> diffClassFields(Class<?> a, Class<?> b) {
        var diffFields = new ArrayList<Field>();
        Comparator<Field> comparator = (first, second) ->
                compareName(first.getName(), second.getName())
                        + compareModifiers(first.getModifiers(), second.getModifiers())
                        + compareType(first.getGenericType(), second.getGenericType());
        diffFields.addAll(diff(a.getDeclaredFields(), b.getDeclaredFields(), comparator));
        diffFields.addAll(diff(b.getDeclaredFields(), a.getDeclaredFields(), comparator));
        return diffFields;
    }

    private List<Method> diffClassMethods(Class<?> a, Class<?> b) {
        var diffMethods = new ArrayList<Method>();
        Comparator<Method> comparator = (first, second) ->
                compareName(first.getName(), second.getName())
                        + compareModifiers(first.getModifiers(), second.getModifiers())
                        + compareParametersType(first.getGenericParameterTypes(), second.getGenericParameterTypes())
                        + compareReturnType(first.getGenericReturnType(), second.getGenericReturnType())
                        + compareExceptions(first.getGenericExceptionTypes(), second.getGenericExceptionTypes());
        diffMethods.addAll(diff(a.getDeclaredMethods(), b.getDeclaredMethods(), comparator));
        diffMethods.addAll(diff(b.getDeclaredMethods(), a.getDeclaredMethods(), comparator));
        return diffMethods;
    }

    void diffClasses(Class<?> a, Class<?> b) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( "diff" + a.getSimpleName() + b.getSimpleName() + ".txt", false)) {
            out = fileOutputStream;
            for (var field : diffClassFields(a, b)) {
                printField(field);
            }
            for (var method : diffClassMethods(a, b)) {
                printMethod(method);
            }
        }
    }
}