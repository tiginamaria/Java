package hse.hw.bst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BinarySearchTreeTest {

    private BinarySearchTree<Integer> treeSet;

    @BeforeEach
    void initTrie() {
        treeSet = new BinarySearchTree<>();
    }

    @Test
    void addContainsGeneralTest() {
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(-3));
        assertTrue(treeSet.add(-4));
        assertTrue(treeSet.add(-1));
        assertTrue(treeSet.add(-2));
        assertTrue(treeSet.add(4));
        assertFalse(treeSet.add(4));
        assertFalse(treeSet.add(1));
        assertTrue(treeSet.contains(2));
        assertTrue(treeSet.contains(-3));
        assertTrue(treeSet.contains(-4));
        assertTrue(treeSet.contains(-1));
        assertTrue(treeSet.contains(1));
        assertTrue(treeSet.contains(3));
        assertFalse(treeSet.contains(0));
        assertFalse(treeSet.contains(-5));
    }

    @Test
    void addRemoveTest() {
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.remove(3));
        assertTrue(treeSet.remove(2));
        assertTrue( treeSet.remove(1));
        assertTrue(treeSet.add(1));
        assertTrue(treeSet.add(2));
        assertTrue(treeSet.add(3));
        assertTrue(treeSet.add(-3));
        assertTrue(treeSet.add(-2));
        assertTrue(treeSet.add(4));
        assertFalse(treeSet.add(4));
        assertTrue(treeSet.remove(4));
        assertFalse(treeSet.remove(4));
        assertFalse(treeSet.add(1));
        assertFalse(treeSet.contains(4));
        assertTrue(treeSet.remove(3));
        assertTrue(treeSet.contains(-3));
        assertTrue(treeSet.remove(-3));
        assertFalse(treeSet.contains(-3));
        assertFalse(treeSet.contains(3));
        assertTrue(treeSet.remove(1));
        assertFalse(treeSet.contains(1));
    }

    @Test
    void addNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.add(null));
    }

    @Test
    void containsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.contains(null));
    }

    @Test
    void removeNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.remove(null));
    }


    @Test
    void firstGeneralTest() {
        assertNull(treeSet.first());
        treeSet.add(3);
        assertEquals((Integer)(3), treeSet.first());
        treeSet.add(2);
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)(1), treeSet.first());
        treeSet.add(-5);
        treeSet.add(-6);
        treeSet.add(-4);
        assertEquals((Integer)(-6), treeSet.first());
        treeSet.add(-7);
        treeSet.add(-6);
        assertEquals((Integer)(-7), treeSet.first());
    }

    @Test
    void firstRemoveTest() {
        assertNull(treeSet.first());
        treeSet.add(3);
        treeSet.add(2);
        treeSet.add(1);
        assertEquals((Integer)1, treeSet.first());
        treeSet.remove(3);
        treeSet.remove(2);
        treeSet.remove(1);
        assertNull(treeSet.first());
        treeSet.add(-3);
        treeSet.add(2);
        treeSet.add(-4);
        treeSet.add(1);
        assertEquals((Integer)(-4), treeSet.first());
        treeSet.add(-5);
        treeSet.add(-6);
        treeSet.add(-4);
        assertEquals((Integer)(-6), treeSet.first());
        treeSet.add(-7);
        treeSet.add(-6);
        assertEquals((Integer)(-7), treeSet.first());
        treeSet.remove(-7);
        assertEquals((Integer)(-6), treeSet.first());
    }

    @Test
    void lastGeneralTest() {
        assertNull(treeSet.last());
        treeSet.add(3);
        assertEquals((Integer)(3), treeSet.last());
        treeSet.add(2);
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)(4), treeSet.last());
        treeSet.add(-5);
        treeSet.add(-6);
        treeSet.add(-4);
        assertEquals((Integer)(4), treeSet.last());
        treeSet.add(7);
        treeSet.add(6);
        assertEquals((Integer)(7), treeSet.last());
    }

    @Test
    void lastRemoveTest() {
        assertNull(treeSet.last());
        treeSet.add(3);
        treeSet.remove(3);
        treeSet.add(2);
        treeSet.add(1);
        assertEquals((Integer)2, treeSet.last());
        treeSet.remove(2);
        treeSet.remove(1);
        assertNull(treeSet.last());
        treeSet.add(-3);
        treeSet.add(2);
        treeSet.add(-4);
        treeSet.add(1);
        assertEquals((Integer)(2), treeSet.last());
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(4);
        assertEquals((Integer)(6), treeSet.last());
        treeSet.add(7);
        treeSet.add(-6);
        assertEquals((Integer)(7), treeSet.last());
        treeSet.remove(7);
        assertEquals((Integer)(6), treeSet.last());
    }

    @Test
    void lowerGeneralTest() {
        assertNull(treeSet.lower(0));
        treeSet.add(3);
        treeSet.add(-2);
        assertEquals((Integer)(-2), treeSet.lower(3));
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)(1), treeSet.lower(3));
        treeSet.add(-2);
        treeSet.add(6);
        assertEquals((Integer)(4), treeSet.lower(5));
        treeSet.add(-8);
        treeSet.add(-9);
        assertEquals((Integer)(-9), treeSet.lower(-8));
        assertNull(treeSet.lower(-9));
        assertNull(treeSet.lower(-1000));
    }

    @Test
    void lowerRemoveTest() {
        assertNull(treeSet.lower(0));
        treeSet.add(3);
        treeSet.add(-2);
        assertEquals((Integer)(-2), treeSet.lower(3));
        treeSet.remove(-2);
        assertNull(treeSet.lower(3));
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)(1), treeSet.lower(3));
        treeSet.add(-2);
        treeSet.add(6);
        assertEquals((Integer)(4), treeSet.lower(5));
        treeSet.add(-8);
        treeSet.add(-9);
        assertEquals((Integer)(-8), treeSet.lower(-2));
        assertEquals((Integer)(-9), treeSet.lower(-8));
        treeSet.remove(-8);
        treeSet.remove(-2);
        assertEquals((Integer)(-9), treeSet.lower(1));
        treeSet.remove(-9);
        assertNull(treeSet.lower(1));
    }

    @Test
    void lowerOfFirstTest() {
        assertNull(treeSet.lower(0));
        for (int i = -10; i < 10; i += 3) {
            treeSet.add(i);
        }
        assertNull(treeSet.lower(-10));
        assertNull(treeSet.lower(-100));
    }

    @Test
    void higherGeneralTest() {
        assertNull(treeSet.higher(0));
        treeSet.add(-3);
        treeSet.add(2);
        assertEquals((Integer)(2), treeSet.higher(-3));
        treeSet.add(-1);
        treeSet.add(-4);
        assertEquals((Integer)(-1), treeSet.higher(-3));
        treeSet.add(2);
        treeSet.add(-6);
        assertEquals((Integer)(-4), treeSet.higher(-6));
        treeSet.add(8);
        treeSet.add(9);
        assertEquals((Integer)(9), treeSet.higher(8));
        assertNull(treeSet.higher(9));
        assertNull(treeSet.higher(1000));
    }

    @Test
    void higherOfLastTest()  {
        assertNull(treeSet.lower(0));
        for (int i = 10; i > -10; i -= 3) {
            treeSet.add(i);
        }
        assertNull(treeSet.higher(10));
        assertNull(treeSet.higher(100));
    }

    @Test
    void floorGeneralTest() {
        assertNull(treeSet.floor(0));
        treeSet.add(3);
        treeSet.add(-2);
        assertEquals((Integer)(3), treeSet.floor(3));
        treeSet.remove(3);
        assertEquals((Integer)(-2), treeSet.floor(3));
        assertEquals((Integer)(-2), treeSet.floor(2));
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)(1), treeSet.floor(2));
        treeSet.add(-2);
        treeSet.add(6);
        assertEquals((Integer)(6), treeSet.floor(6));
        assertEquals((Integer)(6), treeSet.floor(7));
        treeSet.add(-8);
        treeSet.add(-9);
        assertEquals((Integer)(-8), treeSet.floor(-8));
        treeSet.remove(-8);
        assertEquals((Integer)(-9), treeSet.floor(-8));
        assertNotNull(treeSet.floor(-9));
        assertNull(treeSet.floor(-1000));
    }

    @Test
    void ceilingGeneralTest() {
        assertNull(treeSet.floor(0));
        treeSet.add(-3);
        treeSet.add(2);
        assertEquals((Integer)(-3), treeSet.ceiling(-3));
        treeSet.remove(-3);
        assertEquals((Integer)(2), treeSet.ceiling(-3));
        assertEquals((Integer)(2), treeSet.ceiling(-2));
        treeSet.add(-1);
        treeSet.add(-4);
        assertEquals((Integer)(-1), treeSet.ceiling(-2));
        treeSet.add(2);
        treeSet.add(-6);
        assertEquals((Integer)(-6), treeSet.ceiling(-6));
        assertEquals((Integer)(-6), treeSet.ceiling(-7));
        treeSet.add(8);
        treeSet.add(9);
        assertEquals((Integer)(8), treeSet.ceiling(7));
        treeSet.remove(8);
        assertEquals((Integer)(9), treeSet.ceiling(7));
        assertNotNull(treeSet.ceiling(9));
        assertNull(treeSet.ceiling(1000));
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