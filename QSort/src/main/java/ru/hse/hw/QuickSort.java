package ru.hse.hw;

import java.util.concurrent.ForkJoinPool;

public class QuickSort {

    /**
     * Invokes a ForkJoinQuickSortTask in the provided pool, blocking until done.
     * @param a array to sort
     * @return sorted array
     */
    public static <T extends Comparable<? super T>> void forkJoinQuickSort(T[] a) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinQuickSortTask<>(a));
    }
}
