package ru.hse.hw.testClasses;

import java.util.Collections;
import java.util.List;

public class EqualMethodsFieldsSecondClass<T> {
    public final static int version = 2; //different value

    private final T type = null;

    private T data;

    protected List<T> memory; //List<? extends T> is equals to List<? extends T>

    List<T> getStoredData() { return null; } //List<? extends T> is equals to List<? extends T>

    private void setMemory(List<T> memory) { } //List<? extends T> is equals to List<? extends T>

    private void setData(T data, int size, List<T> memory, T type) { } //List<? extends T> is equals to List<? extends T>
}
