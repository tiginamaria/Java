package ru.hse.hw.testClasses;

import java.util.List;

public class FieldsClassA<T> {

    public final int version = 10; //different modifier

    private final T t = null; //different name

    protected List<Integer> memory; // different type
}
