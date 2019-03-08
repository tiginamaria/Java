package ru.hse.hw;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;


/*
Реализовать класс Reflector с методами

    printStructure(Class<?> someClass) -- создаёт файл с именем someClass.java.
        В нём описан класс SomeClass со всеми полями, методами, внутренними и вложенными классами и интерфейсами
        Методы без реализации
        Модификаторы видимости и static должны быть такими, как в переданном классе
        Объявления полей, методов и вложенных классов должны сохранить генериковость
    diffClasses(Class<?> a, Class<?> b)
        Выводит все поля и методы, различающиеся в двух классах

Реализовать тест, печатающий класс, компилирующий полученный файл, грузящий то, что получилось и сравнивающий с исходным

При этом:

    Работаем только с классами – интерфейсы, enum-ы и т.д. можно не обрабатывать
    Можно считать, что внутри нет enum-ов и аннотаций
    При сравнении двух классов учтите, что <E extends Object> и <E> – это одно и то же

 */
public class Reflector {

    private static FileWriter out;

    public static void printStructure(Class<?> someClass) throws IOException {

        try(FileWriter fileOutputStream = new FileWriter( someClass.getSimpleName() + ".java", false)) {
            out = fileOutputStream;
            printModifiers(someClass);
            printClassName(someClass);
            printTypeParameters(someClass);
            printExtendedClass(someClass);
            printImplementedInterfaces(someClass);
            out.write("{\n");
            for (var subclass : someClass.getDeclaredClasses()) {
                printStructure(subclass);
            }
            printFields(someClass);
            printMethods(someClass);
        }
    }

    private static void printFields(Class<?> someClass) throws IOException {
        Field[] fields = someClass.getDeclaredFields();
        for (var field : fields) {
            out.write(Modifier.toString(field.getModifiers()) + " ");
            out.write(getTypeName(field.getGenericType()) + " ");
            out.write(field.getName()+ "\n");
        }
    }

    private static void printMethods(Class<?> someClass) throws IOException {
        Method[] methods = someClass.getMethods();
        for (var method : methods) {
            out.write(method.getReturnType().getName() + " ");
            out.write(method.getName() + "(");
            Parameter[] parameters = method.getParameters();
            if (parameters.length > 0) {
                out.write(getTypeName(parameters[0].getType()) + " " + parameters[0].getName());
                for (int i = 1; i < parameters.length; i++) {
                    out.write(", " + getTypeName(parameters[i].getType()) + " " + parameters[i].getName());
                }
            }
            out.write(") " + "{ }" + "\n");
        }
    }

    private static void printModifiers(Class<?> someClass) throws IOException {
        var mods = someClass.getModifiers();
        out.write(Modifier.toString(mods));
        if (mods != 0) {
            out.write(" ");
        }
    }

    private static void printClassName(Class<?> someClass) throws IOException {
        out.write(someClass.getSimpleName());
    }

    private static void printTypeParameters(Class<?> someClass) throws IOException {
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

    private static String getTypeName(Type type) {
        return type.getTypeName().replace('$', '.');
    }

    private static void printImplementedInterfaces(Class<?> someClass) throws IOException {
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

    private static void printExtendedClass(Class<?> someClass) throws IOException {
        Type ancestor = someClass.getGenericSuperclass();
        if (ancestor != null) {
            out.write("extends " + getTypeName(ancestor));
            out.write(" ");
        }
    }



        void help(Class<?> someClass) {
            //интерфейсы
            Class[] interfaces = someClass.getInterfaces();
            for(Class cInterface : interfaces) {
                System.out.println( cInterface.getName()+ "\n");
            }


            var classes = someClass.getClasses();
            for (var c : classes) {
                System.out.println(c.getSimpleName()+ "\n");
            }

            //поля
            Class c = someClass;
            Field[] publicFields = c.getFields();
            for (Field field : publicFields) {
                Class fieldType = field.getType();
                System.out.println("Имя: " + field.getName()+ "\n");
                System.out.println("Тип: " + fieldType.getName()+ "\n");
            }

            //field.set(2, "New name\n");


            //конструктор
            c = someClass;
            Constructor[] constructors = c.getConstructors();
            for (Constructor constructor : constructors) {
                Class[] paramTypes = constructor.getParameterTypes();
                for (Class paramType : paramTypes) {
                    System.out.println(paramType.getName() + " \n");
                }
            }

            var fields = someClass.getDeclaredFields();
            for (var f : fields) {
                System.out.println(f.getName()+ "\n");
            }
            System.out.flush();

            //методы
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                System.out.println("Имя: " + method.getName()+ "\n");
                System.out.println("Возвращаемый тип: " + method.getReturnType().getName() + "\n");

                Class[] paramTypes = method.getParameterTypes();
                System.out.println("Типы параметров: \n");
                for (Class paramType : paramTypes) {
                    System.out.println(" " + paramType.getName()+ "\n");
                }
            }
        }

    void diffClasses(Class<?> a, Class<?> b) {

    }
}