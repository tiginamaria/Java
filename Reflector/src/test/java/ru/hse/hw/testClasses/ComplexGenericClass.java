package ru.hse.hw.testClasses;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ComplexGenericClass<T> extends SimpleGenericClass<T> implements Serializable, Runnable {

    public final static int version = 10;

    private final T t = null;

    private T data;

    protected List<? extends T> memory;

    void loadData(T newData) { }

    List<T> getStoredData() throws IllegalAccessException { return null; }

    private void setMemory(List<? extends T> memory) { }

    private void setData(T data, int size, List<? extends T> memory, T type) { }

    public void printData() { }

    public ComplexGenericClass() throws IOException { }

    private ComplexGenericClass(T data) { }

    protected ComplexGenericClass(List<T> list) { }

    @Override
    public void run() { }
}
