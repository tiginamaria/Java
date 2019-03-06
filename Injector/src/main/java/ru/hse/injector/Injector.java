package ru.hse.injector;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Injector {

    private static HashMap<Class<?>, Object> createdClasses = new HashMap<>();

    private static ArrayList<Class<?>> toCreateClasses = new ArrayList<>();

    private static ArrayList<Class<?>> implementations = new ArrayList<>();

   // private HashMap<Class<?>>

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Class<?> className = Class.forName(rootClassName);

        for (var impl : implementationClassNames) {
            implementations.add(Class.forName(impl));
        }

        return create(className);
    }

    private static Object create(Class<?> className) throws InjectionCycleException, NoSuchMethodException {

        if (createdClasses.containsKey(className)) {
            return createdClasses.get(className);
        }

        if (toCreateClasses.contains(className)) {
            throw new InjectionCycleException();
        }

        toCreateClasses.add(className);

        var constructor = className.getConstructor();

    }
}
