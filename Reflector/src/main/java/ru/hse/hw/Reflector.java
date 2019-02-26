package ru.hse.hw;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


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

    void printStructure(Class<?> someClass) {
        try(FileWriter fout = new FileWriter( "someClass.java", false))
        {

            //исследоваие модификатора
            int mods = someClass.getModifiers();
            if (Modifier.isPublic(mods)) {
                System.out.println("public");
            }
            if (Modifier.isAbstract(mods)) {
                System.out.println("abstract");
            }
            if (Modifier.isFinal(mods)) {
                System.out.println("final");
            }

            //интерфейсы
            Class[] interfaces = someClass.getInterfaces();
            for(Class cInterface : interfaces) {
                System.out.println( cInterface.getName() );
            }


            var classes = someClass.getClasses();
            for (var c : classes) {
                fout.write(c.getSimpleName());
            }

            //поля
            Class c = someClass;
            Field[] publicFields = c.getFields();
            for (Field field : publicFields) {
                Class fieldType = field.getType();
                System.out.println("Имя: " + field.getName());
                System.out.println("Тип: " + fieldType.getName());
            }

            //field.set(2, "New name");


            //конструктор
            c = someClass;
            Constructor[] constructors = c.getConstructors();
            for (Constructor constructor : constructors) {
                Class[] paramTypes = constructor.getParameterTypes();
                for (Class paramType : paramTypes) {
                    System.out.print(paramType.getName() + " ");
                }
                System.out.println();
            }

            var fields = someClass.getDeclaredFields();
            for (var f : fields) {
                fout.write(f.getName());
            }
            fout.flush();

            //методы
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                System.out.println("Имя: " + method.getName());
                System.out.println("Возвращаемый тип: " + method.getReturnType().getName());

                Class[] paramTypes = method.getParameterTypes();
                System.out.print("Типы параметров: ");
                for (Class paramType : paramTypes) {
                    System.out.print(" " + paramType.getName());
                }
                System.out.println();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    void diffClasses(Class<?> a, Class<?> b) {

    }
}
