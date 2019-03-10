package ru.hse.hw.testClasses;

import java.util.List;
import java.util.Set;

public class MethodsClassA<T> {

    private void setMemory(List<? extends T> memory) { } //different number of parameters (one parameter)

    List<T> getStoredData() { return null; } //not fixed list parameter type

    private void setData(T data, int size, List<? extends T> memory, T type) { } //changed order of parameters

    private Set<T> getMemory() { return null; } //different return type

    private void getSizeMemory() { } //different name of method

    private void setMemorySize() { } //different modifier of method
}
