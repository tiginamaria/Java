package ru.hse.hw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    @Test
    void latchQuickSort() {
        var a = new Integer[]{5, 3, 5, 4 , 1};
        QuickSort.forkJoinQuickSort(a);
        for (var x : a) {
            System.out.println(x);
        }
    }
}