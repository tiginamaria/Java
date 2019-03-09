package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * This class implements methods to obtain the full class structure by name and compare the two classes
 */
public class Reflector {

    /**
     * Output File Stream
     */
    private static FileWriter out;

    /**
     * Tabs to print pretty structure of class
     */
    private static int tabs;

    /**
     * Print the structure of given classes, including inner and nested classes, fields, constructor, methods
     * @param someClass given class to print
     * @throws IOException when there are problems connecting to the output file
     */
    public static void printStructure(Class<?> someClass) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( someClass.getSimpleName() + ".java", false)) {
            PrintClass(someClass, fileOutputStream, 0);
        }
    }

    /**
     * Recursively print the structure of given class
     * @param someClass given class to print
     * @param fileOutputStream file to write structure of file
     * @param tabulations current number of tabs to print pretty structure of class
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Print all declared fields of given class
     * @param someClass class to print fields
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printFields(@NotNull Class<?> someClass) throws IOException {
        Field[] fields = someClass.getDeclaredFields();
        for (var field : fields) {
            printField(field);
        }
    }

    /**
     * Print one field of given class, including modifiers, name and value(if the field is final)
     * @param field field to print
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Get default value of given type
     * @param type type to get default value
     * @return default value
     */
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

    /**
     * Print all declared methods of given class
     * @param someClass class to print methods
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printMethods(@NotNull Class<?> someClass) throws IOException {
        Method[] methods = someClass.getDeclaredMethods();
        for (var method : methods) {
            printMethod(method);
        }
    }

    /**
     * Print one method of given class, including modifiers, name and parameters
     * @param method method to print
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Print all declared constructors of given class
     * @param someClass class to print constructors
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Print name of given class in right format
     * @param someClass class to print name
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printClassName(@NotNull Class<?> someClass) throws IOException {
        printTabs();
        out.write("class " + someClass.getSimpleName());
        ++tabs;
    }

    /**
     * Print generic type parameters of given class
     * @param someClass class to print parameters
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Print implemented interfaces of given class
     * @param someClass class to print interfaces
     * @throws IOException when there are problems connecting to the output file
     */
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

    /**
     * Print superclass of given class
     * @param someClass class to print interfaces
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printExtendedClass(@NotNull Class<?> someClass) throws IOException {
        Type ancestor = someClass.getGenericSuperclass();
        if (ancestor != null) {
            out.write("extends " + getTypeName(ancestor));
            out.write(" ");
        }
    }

    /**
     * Print modifies from given code number
     * @param mods encoded modifiers
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printModifiers(@NotNull int mods) throws IOException {
        out.write(Modifier.toString(mods));
        if (mods != 0) {
            out.write(" ");
        }
    }

    /**
     * Print type and name of parameters
     * @param types array with types of parameters
     * @param parameters array with names of parameters
     * @throws IOException
     */
    private static void printParameters(@NotNull Type[] types,@NotNull Parameter[] parameters) throws IOException {
        if (types.length > 0) {
            out.write(getTypeName(types[0]) + " " + parameters[0].getName());
            for (int i = 1; i < types.length; i++) {
                out.write(", " + getTypeName(types[i]) + " " + parameters[i].getName());
            }
        }
    }

    /**
     * Print tabs
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printTabs() throws IOException {
        for (int i = 0; i < tabs; i++) {
            out.write("\t");
        }
    }

    /**
     * Get type name in right format
     * @param type type to get name
     * @return typename
     */
    @NotNull
    private static String getTypeName(@NotNull Type type) {
        return type.getTypeName().replace('$', '.');
    }

    /**
     * Get elements from first given array, which has no copy in second given array
     * @param aArray first given array
     * @param bArray second given array
     * @param comparator to compare element from arrays
     * @param <T> type of element
     * @return element from first array with no copy in second
     */
    private static <T> List<T> diff(T[] aArray, T[] bArray, Comparator<T> comparator) {
        var listDiff = new ArrayList<T>();
        for (var a : aArray) {
            boolean found = false;
            for (var b : bArray) {
                if (comparator.compare(a, b) == 0) {
                    found = true;
                }
            }
            if (!found) {
                listDiff.add(a);
            }
        }
        return listDiff;
    }

    /**
     * Comparator for names
     * @param a first name
     * @param b second name
     * @return 0 if elements are equal, 1 if not
     */
    private static int compareName(String a, String b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Comparator for modifiers
     * @param a first encoded set of modifiers
     * @param b second encoded set of modifiers
     * @return 0 if modifiers are equal, 1 if not
     */
    private static int compareModifiers(int a, int b) {
        return Modifier.toString(a).equals(Modifier.toString(b)) ? 0 : 1;
    }

    /**
     * Comparator for types
     * @param a first type
     * @param b second type
     * @return 0 if types are equal, 1 if not
     */
    private static int compareType(Type a, Type b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Comparator for types
     * @param a first return type
     * @param b second return type
     * @return 0 if types are equal, 1 if not
     */
    private static int compareReturnType(Type a, Type b) {
        return compareType(a, b);
    }

    /**
     * Comparator for parameters
     * @param a first types array
     * @param b second types array
     * @return 0 if types are equal, 1 if not
     */
    private static int compareParametersType(Type[] a, Type[] b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Comparator for exceptions
     * @param a first types array
     * @param b second types array
     * @return 0 if types are equal, 1 if not
     */
    private static int compareExceptions(Type[] a, Type[] b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Get list of fields, which are different in two given classes
     * @param a first class
     * @param b second class
     * @return list of different fields
     */
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

    /**
     * Get list of methods, which are different in two given classes
     * @param a first class
     * @param b second class
     * @return list of different methods
     */
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

    /**
     * Print fields and methods, which are different in two given classes
     * @param a first class
     * @param b second class
     * @throws IOException when there are problems connecting to the output file
     */
    void diffClasses(Class<?> a, Class<?> b) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( "Diff" + a.getSimpleName() + b.getSimpleName(), false)) {
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