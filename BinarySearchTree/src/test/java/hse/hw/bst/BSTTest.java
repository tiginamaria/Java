package hse.hw.bst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BSTTest {

    private BST<Integer> treeSet;

    @BeforeEach
    void initTrie() {
        treeSet = new BST<>();
    }

    @Test
    void add() {
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.add(4));
        assertTrue(treeSet.add(5));
        assertTrue(treeSet.add(6));
        assertTrue(treeSet.add(-8));
        assertTrue(treeSet.add(-7));
        assertTrue(treeSet.add(-6));
        assertFalse(treeSet.add(3));
        assertFalse(treeSet.add(2));
        assertTrue(treeSet.contains(1));
        assertTrue(treeSet.contains(2));
        assertTrue(treeSet.contains(3));
        assertTrue(treeSet.contains(4));
        assertTrue(treeSet.contains(5));
        assertTrue(treeSet.contains(6));
        assertFalse(treeSet.contains(7));
        assertTrue(treeSet.contains(-8));
        assertFalse(treeSet.contains(-2));
    }

    @Test
    void first() {
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.add(4));
        assertTrue(treeSet.add(5));
        assertTrue(treeSet.add(6));
        assertTrue(treeSet.add(-8));
        assertTrue(treeSet.add(-7));
        assertTrue(treeSet.add(-6));
        assertEquals((Integer)(-8), treeSet.first());
    }

    @Test
    void last() {
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.add(4));
        assertTrue(treeSet.add(5));
        assertTrue(treeSet.add(6));
        assertTrue(treeSet.add(-8));
        assertTrue(treeSet.add(-7));
        assertTrue(treeSet.add(-6));
        assertEquals((Integer)(6), treeSet.last());
    }

    @Test
    void lower() {
    }

    @Test
    void higher() {
    }

    @Test
    void floor() {
    }

    @Test
    void ceiling() {
    }

    @Test
    void iterator() {
    }

    @Test
    void descendingIterator() {
    }

    @Test
    void descendingSet() {
    }

    @Test
    void size() {
    }
}