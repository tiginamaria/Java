package ru.hse.hw.testClasses;

import java.util.Collections;
import java.util.List;

public class EqualMethodsFiealsFirstClass<T> {
    public final static int version = 1; //different value

    private final T type = null;

    private T data;

    protected List<? extends T> memory; // List<? extends T> is equals to List<T>

    List<? extends T> getStoredData() { return null; }  //List<? extends T> is equals to List<? extends T>

    private void setMemory(List<? extends T> memory) { } //List<? extends T> is equals to List<? extends T>

    private void setData(T data, int size, List<? extends T> memory, T type) { } //List<? extends T> is equals to List<? extends T>
}
