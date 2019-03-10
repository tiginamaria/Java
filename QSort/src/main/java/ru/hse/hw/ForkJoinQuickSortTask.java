package ru.hse.hw;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinQuickSortTask<T extends Comparable<? super T>> extends RecursiveAction {

    private static T[] a;
    private final int left;
    private final int right;

    public ForkJoinQuickSortTask(T[] a) {
        this(a, 0, a.length - 1);
    }

    private ForkJoinQuickSortTask(T[] a, int left, int right) {
        this.a = a;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left >= right + 1) {
            return;
        }
        int pivotIndex = partition(left, right);
        ForkJoinTask t1 = null;

        if (left < pivotIndex)
            t1 = new ForkJoinQuickSortTask<T>(a, left, pivotIndex).fork();

        if (pivotIndex + 1 < right)
            new ForkJoinQuickSortTask<T>(a, pivotIndex + 1, right).compute();

        if (t1 != null) {
            t1.join();
        }
    }

    private static int partition(int left, int right) {
        T pivotValue = a[middleIndex(left, right)];
        while (true) {
            while (a[left].compareTo(pivotValue) < 0) {
                left++;
            }
            while (a[right].compareTo(pivotValue) < 0) {
                right--;
            }

            if (left < right) {
                T tmp = a[left];
                a[left] = a[right];
                a[right] = tmp;
            } else {
                return right;
            }
        }
    }

    private static int middleIndex(int left, int right) {
        return left + (right - left) / 2;
    }
}
