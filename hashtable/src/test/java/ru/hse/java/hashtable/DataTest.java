package ru.hse.java.hashtable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTest {

    @Test
    void getKeyDataTest() {
        Data d = new Data("a", "b");
        assertEquals(d.getKey(), "a");
    }

    @Test
    void getValueDataTest() {
        Data d = new Data("a", "b");
        assertEquals(d.getValue(),"b");
    }

    @Test
    void setKeyDataTest() {
        Data d = new Data("a", "b");
        d.setKey("c");
        assertEquals(d.getKey(),"c");
    }

    @Test
    void setValueDataTest() {
        Data d = new Data("a", "b");
        d.setValue("c");
        assertEquals(d.getValue(),"c");
    }

}