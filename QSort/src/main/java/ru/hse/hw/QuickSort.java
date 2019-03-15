package ru.hse.hw;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickSort {

    /**
     * Generator for random value
     */
    private static Random randomize = new Random();

    /**
     * Pool of threads
     */
    private static ExecutorService threadPool;


    /**
     * Current total number of uncompleted thread tasks
     */
    private static AtomicInteger taskCounter;


    /**
     * Task to sort interval from l to r index of given array
     * @param <T> type of elements to sort
     */
    private static class QuickSortTask<T extends Comparable<? super T>> implements Runnable {

        /**
         * Array to sort
         */
        private T[] a;

        /**
         * Left bound of interval of array to sort
         */
        private final int left;

        /**
         * Right bound of interval of array to sort
         */
        private final int right;

        /**
         * Primitive constructor for qSort task
         *
         * @param a
         * @param left
         * @param right
         */
        private QuickSortTask(T[] a, int left, int right) {
            this.a = a;
            this.left = left;
            this.right = right;
        }

        /**
         * Constructor for first task
         *
         * @param a array to sort
         */
        public QuickSortTask(T[] a) {
            this(a, 0, a.length - 1);
        }

        /**
         * Function which runs partition of given interval into two parts and recursively sorts this parts of array
         */
        @Override
        public void run() {
            if (left >= right) {
                return;
            }

            int pivotIndex = partition(a, left, right);

            taskCounter.addAndGet(2);
            threadPool.execute(new QuickSortTask<>(a, left, pivotIndex));
            threadPool.execute(new QuickSortTask<>(a, pivotIndex + 1, right));
            if (taskCounter.decrementAndGet() == 0) {
                synchronized (taskCounter) {
                    taskCounter.notify();
                }
            }
        }
    }

    /**
     * Divide elements of given array into two parts -
     * @param a
     * @param left
     * @param right
     * @param <T>
     * @return
     */
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
        return left + randomize.nextInt(right - left);
    }

    /**
     * Runs a QuickSortTask for whole given array.
     * @param a array to sort
     * @return sorted array
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] a) throws InterruptedException {
        quickSort(a, Runtime.getRuntime().availableProcessors());
    }

    /**
     * Sets the maximum number of threads and runs a QuickSortTask for whole given array.
     * @param a array to sort
     * @return sorted array
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] a, int threadCounter) throws InterruptedException {
        threadPool = Executors.newFixedThreadPool(threadCounter);
        taskCounter = new AtomicInteger(1);
        threadPool.execute(new QuickSortTask<>(a));
        synchronized (taskCounter) {
            while (taskCounter.get() > 0) {
                taskCounter.wait();
            }
        }
        threadPool.shutdown();
    }
}
