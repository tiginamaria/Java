package ru.hse.hw.testClasses;

public class ClassWithSubclasses<T> {
    private static class NestedClass<T> {
        NestedClass() { }
    }

    protected class InnerClass<T> {
        InnerClass() { }
    }
}
