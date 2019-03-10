package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.Math.min;

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
    public static void printStructure(@NotNull Class<?> someClass) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( someClass.getSimpleName() + ".java", false)) {
            printClass(someClass, fileOutputStream, 0);
        }
    }

    /**
     * Recursively print the structure of given class
     * @param someClass given class to print
     * @param fileOutputStream file to write structure of file
     * @param tabulations current number of tabs to print pretty structure of class
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printClass(@NotNull Class<?> someClass, @NotNull FileWriter fileOutputStream, int tabulations) throws IOException {
        out = fileOutputStream;
        tabs = tabulations;
        printClassName(someClass);
        printGenericTypeParameters(someClass);
        printExtendedClass(someClass);
        printImplementedInterfaces(someClass);
        out.write("{\n");
        for (var subclass : someClass.getDeclaredClasses()) {
            printClass(subclass, out, tabulations + 1);
        }
        printFields(someClass);
        printConstructors(someClass);
        printMethods(someClass);
        --tabs;
        printTabs();
        out.write("}\n");
    }

    /**
     * Print name of given class in right format
     * @param someClass class to print name
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printClassName(@NotNull Class<?> someClass) throws IOException {
        printTabs();
        printModifiers(someClass.getModifiers());
        out.write("class " + someClass.getSimpleName());
        ++tabs;
    }

    /**
     * Print generic type parameters of given class
     * @param someClass class to print parameters
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printGenericTypeParameters(@NotNull Class<?> someClass) throws IOException {
        printTypeParameters(someClass.getTypeParameters(), "<", ", ", "> ");
    }

    /**
     * Print implemented interfaces of given class
     * @param someClass class to print interfaces
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printImplementedInterfaces(@NotNull Class<?> someClass) throws IOException {
        printTypeParameters(someClass.getGenericInterfaces(), "implements ", ", ", " ");
    }

    /**
     * Print superclass of given class
     * @param someClass class to print interfaces
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printExtendedClass(@NotNull Class<?> someClass) throws IOException {
        Type ancestor = someClass.getGenericSuperclass();
        if (ancestor != null && ancestor != Object.class) {
            out.write("extends " + getTypeName(ancestor));
            out.write(" ");
        }
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
        printArguments(method.getGenericParameterTypes(), method.getParameters());
        out.write(") ");
        printExceptions(method.getExceptionTypes());
        if (method.getReturnType() == void.class) {
            out.write("{ }\n");
        } else {
            out.write("{\n");
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
            printArguments(constructor.getGenericParameterTypes(), constructor.getParameters());
            out.write(") ");
            printExceptions(constructor.getGenericExceptionTypes());
            out.write("{ }" + "\n");
        }
    }

    /**
     * Print exceptions
     * @param exceptions array of exceptions to print
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printExceptions(@NotNull Type[] exceptions) throws IOException {
        printTypeParameters(exceptions, "throws ", ", ", " ");
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
     * Print given array types
     * @param parameters types to print
     * @param prefix to print before types
     * @param separator to print between types
     * @param suffix to print after types
     * @throws IOException when there are problems connecting to the output file
     */
    private static void printTypeParameters(@NotNull Type[] parameters, @NotNull String prefix, @NotNull String separator, @NotNull String suffix) throws IOException {
        if (parameters.length > 0) {
            out.write(prefix);
            out.write(parameters[0].getTypeName());
            for (int i = 1; i < parameters.length; i++) {
                out.write(separator + parameters[i].getTypeName());
            }
            out.write(suffix);
        }
    }

    /**
     * Print type and name of parameters
     * @param types array with types of parameters
     * @param parameters array with names of parameters
     * @throws IOException
     */
    private static void printArguments(@NotNull Type[] types,@NotNull Parameter[] parameters) throws IOException {
        if (types.length > 0) {
            out.write(getTypeName(types[0]) + " " + parameters[0].getName());
            for (int i = 1; i < types.length; i++) {
                out.write(", " + getTypeName(types[i]) + " " + parameters[i].getName());
            }
        }
    }

    /**
     * Get default value of given type
     * @param type type to get default value
     * @return default value
     */
    private static String defaultValue(@NotNull Type type) {
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
    private static <T> List<T> diff(@NotNull T[] aArray, @NotNull T[] bArray, @NotNull Comparator<T> comparator) {
        var listDiff = new ArrayList<T>();
        for (var a : aArray) {
            boolean found = false;
            for (var b : bArray) {
                var c = comparator.compare(a, b);
                if (c == 0) {
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
    private static int compareName(@NotNull String a, @NotNull String b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Comparator for modifiers
     * @param a first encoded set of modifiers
     * @param b second encoded set of modifiers
     * @return 0 if modifiers are equal, 1 if not
     */
    private static int compareModifiers(int a, int b) {
        return a == b ? 0 : 1;
    }

    /**
     * Comparator for array of types
     * @param aTypes first type
     * @param bTypes second type
     * @return 0 if all types is two arrays are equal, 1 if not
     */
    private static boolean compareTypes(@NotNull Type[] aTypes, @NotNull Type[] bTypes) {
        if (aTypes.length == 0 && bTypes.length == 0) {
            return true;
        }

        var isEqual = true;
        for (int i = 0; i < min(aTypes.length, bTypes.length); i++) {
            isEqual &= compareType(aTypes[i], bTypes[i]);
        }
        return isEqual && aTypes.length == bTypes.length;
    }

    /**
     * Comparator for types
     * @param a first type
     * @param b second type
     * @return 0 if types are equal, 1 if not
     */
    private static boolean compareType(@NotNull Type a, @NotNull Type b) {
        if (a instanceof Class && b instanceof Class) {
            var aType = (Class<?>)a;
            var bType = (Class<?>)b;
            if (aType.isArray() && bType.isArray()) {
                return compareType(aType.getComponentType(), bType.getComponentType());
            }
            return aType.getSimpleName().equals(bType.getSimpleName());
        } else if (a instanceof TypeVariable && b instanceof TypeVariable) {
            var aType = (TypeVariable<?>)a;
            var bType = (TypeVariable<?>)b;
            return aType.getName().equals(bType.getName());
        } else if (a instanceof WildcardType && ! (b instanceof WildcardType)) {
            var aType = (WildcardType) a;
            if (aType.getUpperBounds().length != 0) {
                return compareType(aType.getUpperBounds()[0], b);
            }
            return compareType(aType.getLowerBounds()[0], b);
        } else if (!(a instanceof WildcardType) && b instanceof WildcardType) {
            var bType = (WildcardType) b;
            if (bType.getUpperBounds().length != 0) {
                return compareType(a, bType.getUpperBounds()[0]);
            }
            return compareType(a, bType.getLowerBounds()[0]);
        } else if (a instanceof WildcardType && b instanceof WildcardType) {
            var aType = (WildcardType) a;
            var bType = (WildcardType) b;
            if (bType.getUpperBounds().length != 0 && aType.getUpperBounds().length != 0) {
                return compareType(aType.getUpperBounds()[0], bType.getUpperBounds()[0]);
            } else if (bType.getLowerBounds().length != 0 && aType.getLowerBounds().length != 0) {
                return compareType(aType.getLowerBounds()[0], bType.getLowerBounds()[0]);
            }
            return false;
        } else if (a instanceof ParameterizedType && b instanceof ParameterizedType) {
            var aType = (ParameterizedType)a;
            var bType = (ParameterizedType)b;
            return compareType(aType.getRawType(), bType.getRawType()) && compareTypes(aType.getActualTypeArguments(), bType.getActualTypeArguments());
        } else if (a instanceof GenericArrayType && b instanceof GenericArrayType) {
            var aType = (GenericArrayType)a;
            var bType = (GenericArrayType)b;
            return compareType(aType.getGenericComponentType(), bType.getGenericComponentType());
        }
        return false;
    }

    /**
     * Comparator for types
     * @param a first return type
     * @param b second return type
     * @return 0 if types are equal, 1 if not
     */
    private static int compareReturnType(@NotNull Type a, @NotNull Type b) {
        return compareType(a, b) ? 0 : 1;
    }

    /**
     * Comparator for parameters
     * @param a first types array
     * @param b second types array
     * @return 0 if types are equal, 1 if not
     */
    private static int compareParametersType(@NotNull Type[] a, @NotNull Type[] b) {
        for (int i = 0; i < min(a.length, b.length); i++) {
            if (!compareType(a[i], b[i])) {
                return 1;
            }
        }
        return a.length == b.length ? 0 : 1;
    }

    /**
     * Comparator for exceptions
     * @param a first types array
     * @param b second types array
     * @return 0 if types are equal, 1 if not
     */
    private static int compareExceptions(@NotNull Type[] a, @NotNull Type[] b) {
        return compareParametersType(a, b);
    }

    /**
     * Get list of fields, which are different in two given classes
     * @param a first class
     * @param b second class
     * @return list of different fields
     */
    private static List<Field> diffClassFields(@NotNull Class<?> a, @NotNull Class<?> b) {
        var diffFields = new ArrayList<Field>();
        Comparator<Field> comparator = (first, second) ->
                compareName(first.getName(), second.getName())
                        + compareModifiers(first.getModifiers(), second.getModifiers())
                        + (compareType(first.getGenericType(), second.getGenericType()) ? 0 : 1);
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
    private List<Method> diffClassMethods(@NotNull Class<?> a, @NotNull Class<?> b) {
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
    void diffClasses(@NotNull Class<?> a, @NotNull Class<?> b) throws IOException {
        try(FileWriter fileOutputStream = new FileWriter( "Diff" + "_" + a.getSimpleName() + "_" + b.getSimpleName(), false)) {
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