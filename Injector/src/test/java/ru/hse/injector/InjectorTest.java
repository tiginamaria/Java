package ru.hse.injector;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.hse.testClasses.ClassWithOneClassDependency;
import ru.hse.testClasses.ClassWithOneInterfaceDependency;
import ru.hse.testClasses.ClassWithoutDependencies;
import ru.hse.testClasses.InterfaceImpl;

import java.util.Collections;

public class InjectorTest  {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("ru.hse.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.hse.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("ru.hse.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.hse.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("ru.hse.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}