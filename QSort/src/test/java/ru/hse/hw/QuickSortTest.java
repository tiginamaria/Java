package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.hw.QuickSort.*;

class QuickSortTest {

    private static final int MAXSIZE = 100000;

    /**
     * Generator for random double
     */
    private static final Random randomize = new Random(System.currentTimeMillis());

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

    private void setRandomIntegerElements(Integer[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = randomize.nextInt();
        }
    }

    @Test
    void quickSortSmallIntegerTest() throws InterruptedException {
        var a = new Integer[]{1, 5, 2, 7, 6, 10, 1000, 234};
        quickSort(a, Comparator.reverseOrder());
        assertTrue(checkSorted(a, Comparator.reverseOrder()));
    }

    @Test
    void quickSortSmallOneElementTest() throws InterruptedException {
        var a = new Integer[]{1};
        quickSort(a, Comparator.reverseOrder());
        assertTrue(checkSorted(a, Comparator.reverseOrder()));
    }

    @Test
    void quickSortSingleThreadSmallIntegerTest() {
        var a = new Integer[]{1, 5, 2, 7, 6, 10, 1000, 234};
        singleThreadQuickSort(a);
        assertTrue(checkSorted(a, Comparator.naturalOrder()));
    }

    @Test
    void quickSortSmallStringTest() throws InterruptedException {
        var a = new String[]{"a", "aa", "abc", "ab" , "b", "aaa", "bbb"};
        quickSort(a);
        assertTrue(checkSorted(a, Comparator.naturalOrder()));
    }

    @Test
    void quickSortBigIntegerTest() throws InterruptedException {
        var a = new Integer[MAXSIZE];
        setRandomIntegerElements(a);
        quickSort(a);
        assertTrue(checkSorted(a, Comparator.naturalOrder()));
    }

    @Test
    void quickSortEmptyArrayTest() throws InterruptedException {
        int size = 0;
        var a = new Integer[size];
        quickSort(a);
        assertTrue(checkSorted(a, Comparator.naturalOrder()));
    }

    /**
     * Test which calculates optimal size of array to use multithread quicksort
     */
    @Test
    void findMultiThreadMinimumLength() throws InterruptedException {
        int left = 0;
        int right = MAXSIZE;
        long start;
        long end;
        while (right - left > 1) {
            int middle = (right + left) / 2;
            var arrayMultiThread = new Integer[middle];
            var arraySingleThread = new Integer[middle];
            setRandomIntegerElements(arrayMultiThread);
            arraySingleThread = Arrays.copyOf(arrayMultiThread, arrayMultiThread.length);

            start = System.currentTimeMillis();
            quickSort(arrayMultiThread);
            end = System.currentTimeMillis();
            long timeMultiThread = end - start;

            start = System.currentTimeMillis();
            singleThreadQuickSort(arraySingleThread);
            end = System.currentTimeMillis();
            long timeSingleThread = end - start;

            if (timeSingleThread < timeMultiThread) {
                left = middle;
            } else {
                right = middle;
            }
        }
        System.out.println("Minimal length of array to use multithread sort is " + right +  ".");
    }
}