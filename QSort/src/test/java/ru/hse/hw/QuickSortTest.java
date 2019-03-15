package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static ru.hse.hw.QuickSort.quickSort;

class QuickSortTest {

    @Test
    void quickSortTest() throws InterruptedException {
        var a = new Integer[]{5, 3, 5, 4 , 1};
        quickSort(a);
        for (var x : a) {
            System.out.println(x);
        }
    }
}