package ru.hse.injector;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Injector {

    private static HashMap<Class<?>, Object> createdInstances = new HashMap<>();

    private static ArrayList<Class<?>> toCreateInstances = new ArrayList<>();

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

    private static Object create(Class<?> className) throws InjectionCycleException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if (createdInstances.containsKey(className)) {
            return createdInstances.get(className);
        }

        if (toCreateInstances.contains(className)) {
            throw new InjectionCycleException();
        }

        toCreateInstances.add(className);

        var constructor = className.getConstructor(); //Constructor<?> constructor = className.getDeclaredConstructors()[0];

        var newInstance = constructor.newInstance(getParameters(constructor));
        createdInstances.put(className, newInstance);
        toCreateInstances.remove(className);

    }

    private static Object[] getParameters(Constructor<?> constructor) throws InjectionCycleException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        var parameters = new ArrayList<Object>();
        for (var param : constructor.getParameterTypes()) {

            parameters.add(create(param));
        }
        return parameters.toArray();
    }
}
