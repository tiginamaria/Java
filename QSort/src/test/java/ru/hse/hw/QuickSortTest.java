package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.hw.QuickSort.quickSort;

class QuickSortTest {

    /**
     * Check if the array is sorted
     * @param a array to check
     * @param comparator comparator for elements in array
     * @param <T> type or elements in array
     * @return true if sorted, false otherwise
     */
    private <T> boolean checkSorted(T[] a, Comparator<? super T> comparator) {
        for (int i = 0; i + 1 < a.length - 1; i++) {
            if (comparator.compare(a[i], a[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    void quickSortIntegerTest() throws InterruptedException {
        var a = new Integer[]{1, 5, 2, 7, 6, 10, 1000, 234};
        quickSort(a, Comparator.reverseOrder());
        assertTrue(checkSorted(a, Comparator.reverseOrder()));
    }

    @Test
    void quickSortTest2() throws InterruptedException {
        var a = new String[]{"a", "aa", "abc", "ab" , "b", "aaa", "bbb"};
        quickSort(a);
        assertTrue(checkSorted(a, Comparator.naturalOrder()));
    }
}