package ru.hse.hw.testClasses;

import java.io.Serializable;

public class ClassWithInterfaceAncestor<K, T> extends SimpleGenericClass<T> implements Serializable, Runnable {
    @Override
    public void run() { }
}
