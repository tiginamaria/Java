package ru.hse.java.hashtable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void putFindListTest() {
        List list = new List();
        list.put("a", "b");
        list.put("c", "d");
        assertNotNull(list.find("a"));
    }

    @Test
    void putFindNullListTest() {
        List list = new List();
        list.put("a", "b");
        assertNull(list.find(null));
    }

    @Test
    void putNotFindListTest() {
        List list = new List();
        list.put("a", "b");
        assertNull(list.find("b"));
    }

    @Test
    void putGetListTest() {
        List list = new List();
        list.put("a", "b");
        assertEquals(list.get("a"), "b" );
        list.put("a", "d");
        assertEquals(list.get("a"), "d" );
    }

    @Test
    void putGetNotListTest() {
        List list = new List();
        list.put("a", "b");
        list.put("c", "d");
        assertNotEquals(list.get("a"), "d" );
    }

    @Test
    void putGetNullListTest() {
        List list = new List();
        list.put("a", "b");
        assertNull(list.get("b"));
    }

    @Test
    void addToRemoveFromHeadListTest() {
        List list = new List();
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        assertEquals(list.removeFromHead().getKey(), "c");
        assertEquals(list.removeFromHead().getKey(), "a");
        assertNull(list.removeFromHead());
    }

    @Test
    void removeListTest() {
        List list = new List();
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        list.addToHead("e", "f");
        assertEquals(list.remove("a"), "b");
        assertNull(list.remove("a"));
        assertEquals(list.remove("c"), "d");
        assertNull(list.remove("c"));
        assertEquals(list.remove("e"), "f");
        assertNull(list.remove("e"));
    }

    @Test
    void removeFindListTest() {
        List list = new List();
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        assertEquals(list.remove("a"), "b");
        assertNull(list.find("a"));
        assertNotNull(list.find("c"));
    }


    @Test
    void emptyNotListTest() {
        List list = new List();
        list.put("a", "b");
        list.put("c", "d");
        list.remove("c");
        assertFalse(list.empty());
    }

    @Test
    void emptyListTest() {
        List list = new List();
        list.put("a", "b");
        list.put("c", "d");
        list.remove("a");
        list.remove("c");
        assertTrue(list.empty());
    }

    @Test
    void emptyNewListTest() {
        List list = new List();
        assertTrue(list.empty());
    }

    @Test
    void clear() {
        List list = new List();
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        list.clear();
        assertTrue(list.empty());
    }
}