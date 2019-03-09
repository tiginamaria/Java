package ru.hse.hw.testClasses;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class FullOptionsGenericClass<T> extends SimpleGenericClass<T> implements Serializable, Runnable {

    public final static int version = 10;

    private final T t = null;

    private T data;

    protected List<? extends T> memory;

    void loadData(T newData) { }

    List<T> getStoredData() { return null; }

    private void setMemory(List<? extends T> memory) { }

    private void setData(T data, int size, List<? extends T> memory, T type) { }

    public void printData() { }

    public FullOptionsGenericClass() { }

    private FullOptionsGenericClass(T data) { }

    protected FullOptionsGenericClass(List<T> list) { }

    @Override
    public void run() { }
}
