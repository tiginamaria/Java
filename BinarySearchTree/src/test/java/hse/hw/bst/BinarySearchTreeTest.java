package hse.hw.bst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    private BinarySearchTree<Integer> treeSet;

    @BeforeEach
    void initTreeSet() {
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
        assertEquals((Integer)3, treeSet.first());
        treeSet.add(2);
        treeSet.add(1);
        treeSet.add(4);
        assertEquals((Integer)1, treeSet.first());
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
        assertEquals((Integer)1, treeSet.first());
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
        assertEquals((Integer)1, treeSet.last());
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
    void lowerNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.lower(null));
    }

    @Test
    void higherNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.higher(null));
    }

    @Test
    void floorNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.floor(null));
    }

    @Test
    void ceilingNullTest() {
        assertThrows(IllegalArgumentException.class, () -> treeSet.ceiling(null));
    }

    @Test
    void iteratorTest() {
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        treeSet.addAll(Arrays.asList(array));
        Arrays.sort(array);
        var index = 0;
        for (Integer integer : treeSet) {
            assertEquals(array[index], integer);
            index++;
        }
    }

    @Test
    void sizeTest() {
        assertEquals(0, treeSet.size());
        Integer[] firstArray = {-4, -1, 2, -3, 4, 1, -2, 3};
        treeSet.addAll(Arrays.asList(firstArray));
        assertEquals(firstArray.length, treeSet.size());
        Integer[] secondArray = {10, 11, 9, -8};
        treeSet.addAll(Arrays.asList(secondArray));
        assertEquals(firstArray.length + secondArray.length, treeSet.size());
    }

    @Test
    void descendingIteratorTest() {
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        treeSet.addAll(Arrays.asList(array));
        Arrays.sort(array, Collections.reverseOrder());
        var index = 0;
        for (Iterator<Integer> iterator = treeSet.descendingIterator(); iterator.hasNext();) {
            assertEquals(array[index], iterator.next());
            index++;
        }
    }

    @Test
    void descendingSetAddTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        treeSet.addAll(Arrays.asList(array));
        Arrays.sort(array, Collections.reverseOrder());
        var index = 0;
        for (Integer integer : descendingTreeSet) {
            assertEquals(array[index], integer);
            index++;
        }
    }

    @Test
    void descendingSetAddRemoveContainsTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        descendingTreeSet.addAll(Arrays.asList(array));
        assertTrue(treeSet.contains(1));
        assertTrue(descendingTreeSet.contains(1));
        treeSet.remove(1);
        assertFalse(treeSet.contains(1));
        assertFalse(descendingTreeSet.contains(1));
        assertTrue(treeSet.contains(-4));
        assertTrue(descendingTreeSet.contains(-4));
        descendingTreeSet.remove(-4);
        assertFalse(treeSet.contains(-4));
        assertFalse(descendingTreeSet.contains(-4));
    }

    @Test
    void descendingSetFirstLastTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        descendingTreeSet.addAll(Arrays.asList(array));
        assertEquals((Integer)(4), descendingTreeSet.first());
        assertEquals((Integer)(-4), descendingTreeSet.last());
    }

    @Test
    void descendingSetFloorCeilingTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        descendingTreeSet.addAll(Arrays.asList(array));
        assertEquals((Integer)(1), descendingTreeSet.floor(0));
        assertEquals((Integer)(1), descendingTreeSet.floor(1));
        assertEquals((Integer)(-1), descendingTreeSet.ceiling(0));
        assertEquals((Integer)(-1), descendingTreeSet.floor(-1));
    }

    @Test
    void descendingSetLowerHigherTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] array = {-4, -1, 2, -3, 4, 1, -2, 3};
        descendingTreeSet.addAll(Arrays.asList(array));
        assertEquals((Integer)(1), descendingTreeSet.lower(0));
        assertEquals((Integer)(2), descendingTreeSet.lower(1));
        assertEquals((Integer)(-1), descendingTreeSet.higher(0));
        assertEquals((Integer)(-2), descendingTreeSet.higher(-1));
    }

    @Test
    void descendingSetSizeTest() {
        var descendingTreeSet = treeSet.descendingSet();
        Integer[] firstArray = {-4, -1, 2, -3, 4, 1, -2, 3};
        treeSet.addAll(Arrays.asList(firstArray));
        assertEquals(treeSet.size(), descendingTreeSet.size());
        Integer[] secondArray = {10, 11, 9, -8};
        descendingTreeSet.addAll(Arrays.asList(secondArray));
        assertEquals(descendingTreeSet.size(), treeSet.size());
    }
}