package ru.hse.hw;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinQuickSortTask<T extends Comparable<? super T>> extends RecursiveAction {

    private T[] a;
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
        if (left > right + 1) {
            return;
        }

        int pivotIndex = partition(a, left, right);
        var t1 = new ForkJoinQuickSortTask<>(a, left, pivotIndex).fork();
        new ForkJoinQuickSortTask<>(a, pivotIndex + 1, right).compute();
        t1.join();
    }

    private static <T extends Comparable<? super T>> int partition(T[] a, int left, int right) {
        T pivotValue = a[middleIndex(left, right)];
        while (left < right) {
            while (a[left].compareTo(pivotValue) < 0) {
                left++;
            }

            while (a[right].compareTo(pivotValue) > 0) {
                right--;
            }

            if (left < right) {
                T tmp = a[left];
                a[left] = a[right];
                a[right] = tmp;
                left++;
                right--;
            }
        }
        return right;
    }

    private static int middleIndex(int left, int right) {
        return left + (right - left + 1) / 2;
    }
}
