package ru.hse.hw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {


    Reflector reflector = new Reflector();
    class Printer<T> implements Serializable {
        public final static int version = 10;

        private T data;

        protected List<? extends T> memory;

        void loadData(T newData) {}

        List<T> getStoredData() { return null; }

        public void printData() {}

        class PrettyPrinter extends Printer {
            public void prettyPrintData() {}
        }
    }

    @Test
    void printStructure() {
        var printer = new Printer<String>();
        reflector.printStructure(printer.getClass());
    }

    @Test
    void diffClasses() {
    }
}