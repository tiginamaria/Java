package ru.hse.hw.testClasses;

import java.util.List;

public class DifferentFieldsFirstClass<T> {

    public final static int version = 10; //different modifier

    private final T p = null; //different name

    protected List<? extends T> memory; // different type
}
