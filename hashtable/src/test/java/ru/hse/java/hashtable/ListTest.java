package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List list;

    @BeforeEach
    void initHashTable() {
        list = new List();
    }

    @Test
    void putFindListTest() {
        list.put("a", "b");
        list.put("c", "d");
        assertNotNull(list.find("a"));
    }

    @Test
    void putFindNullListTest() {
        list.put("a", "b");
        assertNull(list.find(null));
    }

    @Test
    void putNotFindListTest() {
        list.put("a", "b");
        assertNull(list.find("b"));
    }

    @Test
    void putGetListTest() {
        list.put("a", "b");
        assertEquals(list.get("a"), "b" );
        list.put("a", "d");
        assertEquals(list.get("a"), "d" );
    }

    @Test
    void putGetNotListTest() {
        list.put("a", "b");
        list.put("c", "d");
        assertNotEquals(list.get("a"), "d" );
    }

    @Test
    void putGetNullListTest() {
        list.put("a", "b");
        assertNull(list.get("b"));
    }

    @Test
    void addToRemoveFromHeadListTest() {
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        assertEquals(list.removeFromHead().getKey(), "c");
        assertEquals(list.removeFromHead().getKey(), "a");
        assertNull(list.removeFromHead());
    }

    @Test
    void removeListTest() {
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
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        assertEquals(list.remove("a"), "b");
        assertNull(list.find("a"));
        assertNotNull(list.find("c"));
    }


    @Test
    void emptyNotListTest() {
        list.put("a", "b");
        list.put("c", "d");
        list.remove("c");
        assertFalse(list.empty());
    }

    @Test
    void emptyListTest() {
        list.put("a", "b");
        list.put("c", "d");
        list.remove("a");
        list.remove("c");
        assertTrue(list.empty());
    }

    @Test
    void emptyNewListTest() {
        assertTrue(list.empty());
    }

    @Test
    void clear() {
        list.addToHead("a", "b");
        list.addToHead("c", "d");
        list.clear();
        assertTrue(list.empty());
    }
}