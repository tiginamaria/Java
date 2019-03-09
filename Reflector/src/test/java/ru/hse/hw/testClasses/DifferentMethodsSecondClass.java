package ru.hse.hw.testClasses;

import java.util.List;

public class DifferentMethodsSecondClass<T> {

    List<Integer> getStoredData() { return null; } //fixed list parameter type

    private void setMemory(List<? extends T> memory, int size) { } //different number of parameters (two parameters)

    private void setData(T data, int size, T type, List<? extends T> memory) { } //changed order of parameters

    private List<T> getMemory() { return null; } //different return type

    private void getMemorySize() { } //different name of method

    public void setMemorySize() { } //different modifier of method
}
