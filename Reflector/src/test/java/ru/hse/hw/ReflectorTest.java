package ru.hse.hw;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    Reflector reflector = new Reflector();

    public static class Printer<T> {

    }

    public static class PrettyPrinter<T> extends Printer<T> implements Serializable, Runnable {
        public final static int version = 10;

        private T data;

        protected List<? extends T> memory;

        void loadData(T newData) {}

        List<T> getStoredData() { return null; }

        public void printData() {}

        @Override
        public void run() {

        }
    }

    @Test
    void printStructure() throws IOException {
        reflector.printStructure(PrettyPrinter.class);
    }

    @Test
    void diffClasses() {
    }
}