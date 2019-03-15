package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static ru.hse.hw.QuickSort.quickSort;

class QuickSortTest {

    @Test
    void quickSortTest1() throws InterruptedException {
        var a = new Integer[]{5, 3, 5, 4 , 1};
        quickSort(a, Comparator.reverseOrder());
        for (var x : a) {
            System.out.println(x);
        }
        System.out.println();
    }

    @Test
    void quickSortTest2() throws InterruptedException {
        var a = new Integer[]{5, 3, 5, 4 , 1, 9, 8, 4};
        quickSort(a, Comparator.reverseOrder());
        for (var x : a) {
            System.out.println(x);
        }
        System.out.println();
    }

    @Test
    void quickSortTest3() throws InterruptedException {
        var a = new Integer[]{5, 3, 5, 4 , 4, 4};
        quickSort(a, Comparator.reverseOrder());
        for (var x : a) {
            System.out.println(x);
        }
        System.out.println();
    }

    @Test
    void quickSortTest4() throws InterruptedException {
        var a = new Integer[]{5, 3, 5, 4 , 3};
        quickSort(a, Comparator.reverseOrder());
        for (var x : a) {
            System.out.println(x);
        }
        System.out.println();
    }
}