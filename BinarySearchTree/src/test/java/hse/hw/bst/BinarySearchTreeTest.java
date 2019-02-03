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
        assertTrue(treeSet.add(30));
        assertTrue(treeSet.add(25));
        assertTrue(treeSet.add(1000));
        assertTrue(treeSet.add(403));
        assertTrue(treeSet.add(5));
        assertTrue(treeSet.add(66));
        assertTrue(treeSet.add(-87));
        assertTrue(treeSet.add(-78));
        assertTrue(treeSet.add(-647));
        assertFalse(treeSet.add(30));
        assertFalse(treeSet.add(-647));
        assertTrue(treeSet.contains(30));
        assertTrue(treeSet.contains(25));
        assertTrue(treeSet.contains(403));
        assertTrue(treeSet.contains(1000));
        assertTrue(treeSet.contains(-78));
        assertFalse(treeSet.contains(31));
        assertFalse(treeSet.contains(-648));
    }

    @Test
    void addNullTest() {

    }

    @Test
    void containsNullTest() {

    }

    @Test
    void firstGeneralTest() {
        assertNull(treeSet.first());
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        assertEquals((Integer)(25), treeSet.first());
        treeSet.add(5);
        treeSet.add(66);
        treeSet.add(-8);
        assertEquals((Integer)(-8), treeSet.first());
        treeSet.add(-78);
        treeSet.add(-647);
        assertEquals((Integer)(-647), treeSet.first());
    }

    @Test
    void firstRemoveTest() {

    }

    @Test
    void lastGeneralTest() {
        assertNull(treeSet.last());
        treeSet.add(30);
        treeSet.add(25);
        treeSet.add(1000);
        treeSet.add(403);
        assertEquals((Integer)(1000), treeSet.last());
        treeSet.add(5);
        treeSet.add(66);
        treeSet.add(1024);
        treeSet.add(-8);
        assertEquals((Integer)(1024), treeSet.last());
        treeSet.add(-78);
        treeSet.add(1023);
        treeSet.add(-647);
        assertEquals((Integer)(1024), treeSet.last());
    }

    @Test
    void lastRemoveTest() {

    }

    @Test
    void lowerGeneralTest() {
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
        assertEquals((Integer)(25), treeSet.lower(30));
        assertEquals((Integer)(-78), treeSet.lower(-8));
        assertEquals((Integer)(403), treeSet.lower(1000));
        assertEquals((Integer)(66), treeSet.lower(400));
        assertEquals((Integer)(5), treeSet.lower(6));
        assertEquals((Integer)(6), treeSet.lower(7));
        assertNull(treeSet.lower(-647));
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