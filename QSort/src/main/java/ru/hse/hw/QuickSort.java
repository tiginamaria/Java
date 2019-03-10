package ru.hse.hw;

import java.util.concurrent.ForkJoinPool;

public class QuickSort<T extends Comparable<? super T>> {

    private static ForkJoinPool pool = ForkJoinPool.commonPool();

    /**
     * Invokes a ForkJoinQuickSortTask in the provided pool, blocking until done.
     * @param a array to sort
     * @return sorted array
     */
    public static T[] forkJoinQuickSort(T[] a) {
        pool.invoke(new ForkJoinQuickSortTask<T>(a));
        return a;
    }
}
