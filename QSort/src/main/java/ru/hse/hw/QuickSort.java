package ru.hse.hw;

import java.util.concurrent.ForkJoinPool;

public class QuickSort {

    /**
     * Invokes a ForkJoinQuickSortTask in the provided pool, blocking until done.
     * @param a array to sort
     * @return sorted array
     */
    public static <T extends Comparable<? super T>> T[] forkJoinQuickSort(T[] a) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ForkJoinQuickSortTask<>(a));
        return a;
    }
}
