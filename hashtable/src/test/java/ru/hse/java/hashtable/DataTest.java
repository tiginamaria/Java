package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTest {

    private Data pair;

    @BeforeEach
    void initData() {
        pair = new Data("a", "b");
    }

    @Test
    void getKeyDataTest() {
        assertEquals(pair.getKey(), "a");
    }

    @Test
    void getValueDataTest() {
        assertEquals(pair.getValue(), "b");
    }

    @Test
    void setKeyDataTest() {
        pair.setKey("c");
        assertEquals(pair.getKey(), "c");
    }

    @Test
    void setValueDataTest() {
        pair.setValue("c");
        assertEquals(pair.getValue(), "c");
    }

}