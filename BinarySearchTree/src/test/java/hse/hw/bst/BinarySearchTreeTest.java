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
        assertEquals((Integer)(4), treeSet.lower(6));
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
        assertEquals((Integer)(4), treeSet.lower(6));
        treeSet.add(-8);
        treeSet.add(-9);
        assertEquals((Integer)(-8), treeSet.lower(-2));
        assertEquals((Integer)(-9), treeSet.lower(-8));
        treeSet.remove(-8);
        treeSet.remove(-2);
        assertEquals((Integer)(-9), treeSet.lower(1));
        treeSet.remove(-9);
        assertNull(treeSet.lower(1));
        assertNull(treeSet.lower(-1000));
    }

    @Test
    void lowerOfFirstTest() {
        assertNull(treeSet.lower(10));
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(-647);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        assertNull(treeSet.lower(-647));
        assertNull(treeSet.lower(-1000));
    }

    @Test
    void higherGeneralTest() {
        assertNull(treeSet.lower(10));
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        treeSet.add(-647);
        assertEquals((Integer)(30), treeSet.higher(26));
        assertEquals((Integer)(25), treeSet.higher(24));
        assertEquals((Integer)(-8), treeSet.higher(-77));
        assertEquals((Integer)(403), treeSet.higher(100));
        assertEquals((Integer)(66), treeSet.higher(30));
        assertEquals((Integer)(6), treeSet.higher(5));
        assertEquals((Integer)(-78), treeSet.higher(-647));
    }

    @Test
    void higherOfLastTest() {
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        treeSet.add(-647);
        assertNull(treeSet.higher(1000));
        assertNull(treeSet.higher(1500));
    }

    @Test
    void floorGeneralTest() {
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(-647);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        assertEquals((Integer)(25), treeSet.floor(26));
        assertEquals((Integer)(25), treeSet.floor(25));
        assertEquals((Integer)(-78), treeSet.floor(-77));
        assertEquals((Integer)(66), treeSet.floor(100));
        assertEquals((Integer)(30), treeSet.floor(30));
        assertEquals((Integer)(-78), treeSet.floor(-9));
        assertEquals((Integer)(-647), treeSet.floor(-647));
        assertEquals((Integer)(1000), treeSet.floor(10000));
        assertEquals((Integer)(-647), treeSet.floor(-646));
    }

    @Test
    void floorLessThenLeastTest() {
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(-647);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        assertNull(treeSet.floor(-648));
        assertNull(treeSet.floor(-1000));
    }

    @Test
    void ceilingGeneralTest() {
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(-647);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        assertEquals((Integer)(30), treeSet.ceiling(26));
        assertEquals((Integer)(25), treeSet.ceiling(25));
        assertEquals((Integer)(-8), treeSet.ceiling(-77));
        assertEquals((Integer)(403), treeSet.ceiling(100));
        assertEquals((Integer)(30), treeSet.ceiling(30));
        assertEquals((Integer)(5), treeSet.ceiling(0));
        assertEquals((Integer)(-647), treeSet.ceiling(-647));
        assertEquals((Integer)(-647), treeSet.ceiling(-1000));
        assertEquals((Integer)(-78), treeSet.ceiling(-646));
    }

    @Test
    void ceilingGreaterThenLastTest() {
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        treeSet.add(-647);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(66);
        treeSet.add(-8);
        treeSet.add(-78);
        assertNull( treeSet.ceiling(1001));
        assertNull( treeSet.ceiling(10000));
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