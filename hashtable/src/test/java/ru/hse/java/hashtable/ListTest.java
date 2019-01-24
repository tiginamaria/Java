package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List l;

    @BeforeEach
    void initList() {
        l = new List();
    }

    @Test
    void putFindListTest() {
        l.put("a", "b");
        l.put("c", "d");
        assertNotNull(l.find("a"));
    }

    @Test
    void putFindNullListTest() {
        l.put("a", "b");
        assertNull(l.find(null));
    }

    @Test
    void putNotFindListTest() {
        l.put("a", "b");
        assertNull(l.find("b"));
    }

    @Test
    void putGetListTest() {
        l.put("a", "b");
        assertEquals(l.get("a"), "b" );
        l.put("a", "d");
        assertEquals(l.get("a"), "d" );
    }

    @Test
    void putGetNotListTest() {
        l.put("a", "b");
        l.put("c", "d");
        assertNotEquals(l.get("a"), "d" );
    }

    @Test
    void putGetNullListTest() {
        l.put("a", "b");
        assertNull(l.get("b"));
    }

    @Test
    void addToRemoveFromHeadListTest() {
        l.addToHead("a", "b");
        l.addToHead("c", "d");
        assertEquals(l.removeFromHead().getKey(), "c");
        assertEquals(l.removeFromHead().getKey(), "a");
        assertNull(l.removeFromHead());
    }

    @Test
    void removeListTest() {
        l.addToHead("a", "b");
        l.addToHead("c", "d");
        l.addToHead("e", "f");
        assertEquals(l.remove("a"), "b");
        assertNull(l.remove("a"));
        assertEquals(l.remove("c"), "d");
        assertNull(l.remove("c"));
        assertEquals(l.remove("e"), "f");
        assertNull(l.remove("e"));
    }

    @Test
    void removeFindListTest() {
        l.addToHead("a", "b");
        l.addToHead("c", "d");
        assertEquals(l.remove("a"), "b");
        assertNull(l.find("a"));
        assertNotNull(l.find("c"));
    }


    @Test
    void emptyNotListTest() {
        l.put("a", "b");
        l.put("c", "d");
        l.remove("c");
        assertFalse(l.empty());
    }

    @Test
    void emptyListTest() {
        l.put("a", "b");
        l.put("c", "d");
        l.remove("a");
        l.remove("c");
        assertTrue(l.empty());
    }

    @Test
    void emptyNewListTest() {
        assertTrue(l.empty());
    }

    @Test
    void clear() {
        l.addToHead("a", "b");
        l.addToHead("c", "d");
        l.clear();
        assertTrue(l.empty());
    }
}