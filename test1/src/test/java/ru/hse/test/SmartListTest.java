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
        list.add(6);
        list.add(7);
        assertTrue(list.contains(6));
        list.remove(6);
        assertFalse(list.contains(6));
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