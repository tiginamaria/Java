package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTest {

    private Data d;

    @BeforeEach
    void initData() {
        d = new Data("a", "b");;
    }

    @Test
    void getKeyDataTest() {
        assertEquals(d.getKey(), "a");
    }

    @Test
    void getValueDataTest() {
        assertEquals(d.getValue(),"b");
    }

    @Test
    void setKeyDataTest() {
        d.setKey("c");
        assertEquals(d.getKey(),"c");
    }

    @Test
    void setValueDataTest() {
        d.setValue("c");
        assertEquals(d.getValue(),"c");
    }

}