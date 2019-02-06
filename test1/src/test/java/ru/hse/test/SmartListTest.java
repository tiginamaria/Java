package ru.hse.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartListTest {

    @Test
    void getData() {
    }

    @Test
    void get() {
    }

    @Test
    void add() {
        var list = new SmartList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        assertTrue(list.contains(3));

    }

    @Test
    void remove() {
    }

    @Test
    void contains() {
    }

    @Test
    void size() {
    }
}