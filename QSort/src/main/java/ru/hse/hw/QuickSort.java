package ru.hse.hw;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implement sort of array, in original order or according to comparator
 */
public class QuickSort {

    /**
     * Generator for random value
     */
    private static Random randomize = new Random();

    /**
     * Stores general information common for all threads
     * @param <T> type of sorted elements
     */
    private static class GeneralTaskTools<T> {

        /**
         * Pool of threads
         */
        private final ExecutorService threadPool;

        /**
         * Current total number of uncompleted thread tasks
         */
        private final AtomicInteger taskCounter;

        /**
         * Comparator for elements in sorted array
         */
        private final Comparator<? super T> comparator;

        /**
         * Constructor sets common for all threads comparator, thead pool and number of current tasks
         * @param threadPool thread pool
         * @param taskCounter and number of current tasks
         */
        private GeneralTaskTools(ExecutorService threadPool, AtomicInteger taskCounter, Comparator<? super T> comparator) {
            this.threadPool = threadPool;
            this.taskCounter = taskCounter;
            this.comparator = comparator;
        }
    }

    /**
     * Task to sort interval from l to r index of given array
     * @param <T> type of elements to sort
     */
    private static class QuickSortTask<T> implements Runnable {

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
         * Hold general information about tall tasks in qsort
         */
        private final GeneralTaskTools<T> tools;

        /**
         * Primitive constructor for qSort task
         *
         * @param a array to sort
         * @param left left bound of sorting interval of array
         * @param right right bound of sorting interval of array
         * @param tools general tools for multithreaded sort
         */
        private QuickSortTask(T[] a, int left, int right, GeneralTaskTools tools) {
            this.a = a;
            this.left = left;
            this.right = right;
            this.tools = tools;
        }

        /**
         * Constructor for first task
         * @param a array to sort
         * @param tools general tools for multithreaded sort
         */
        public QuickSortTask(T[] a, GeneralTaskTools tools) {
            this(a, 0, a.length, tools);
        }

        /**
         * Function which runs partition of given interval into two parts and recursively sorts this parts of array
         */
        @Override
        public void run() {

            if (right - left > 1) {
                int pivotIndex = partition(a, left, right, tools.comparator);
                tools.taskCounter.addAndGet(2);
                tools.threadPool.execute(new QuickSortTask<>(a, left, pivotIndex, tools));
                tools.threadPool.execute(new QuickSortTask<>(a, pivotIndex, right, tools));
            }

            if (tools.taskCounter.decrementAndGet() == 0) {
                synchronized (tools.taskCounter) {
                    tools.taskCounter.notify();
                }
            }
        }
    }

    /**
     * Divide elements of given array into two parts -
     * @param a array to do partition
     * @param left left bound to do partition
     * @param right right bound to do partition
     * @param <T> type of element in array
     * @return bound of partition
     */
    private static <T> int partition(T[] a, int left, int right, Comparator<? super T> comparator) {
        T pivotValue = a[middleIndex(left, right)];
        right--;
        while (left <= right) {
            while (comparator.compare(a[left], pivotValue) < 0) {
                left++;
            }

            while (comparator.compare(a[right], pivotValue) > 0) {
                right--;
            }

            if (left <= right) {
                T tmp = a[left];
                a[left] = a[right];
                a[right] = tmp;
                left++;
                right--;
            }
        }
        return left;
    }

    /**
     * Calculate index for partition
     * @param left left bound of interval
     * @param right right bound of interval
     * @return pivot element
     */
    private static int middleIndex(int left, int right) {
        return left + randomize.nextInt(right - left);
    }

    /**
     * Runs a QuickSortTask for whole given array.
     * @param a array to sort
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] a) throws InterruptedException {
        quickSort(a, Comparator.naturalOrder(), Runtime.getRuntime().availableProcessors());
    }

    /**
     * Runs a QuickSortTask for whole given array and set tre comparator.
     * @param a array to sort
     * @param comparator for elements in array
     */
    public static <T> void quickSort(T[] a, Comparator<? super T> comparator) throws InterruptedException {
        quickSort(a, comparator, Runtime.getRuntime().availableProcessors());
    }

    /**
     * Sets the maximum number of threads and runs a QuickSortTask for whole given array.
     * @param a array to sort
     */
    private static <T> void quickSort(T[] a, Comparator<? super T> comparator, int threadCounter) throws InterruptedException {
        var threadPool = Executors.newFixedThreadPool(threadCounter);
        var taskCounter = new AtomicInteger(1);
        var tools = new GeneralTaskTools<T>(threadPool, taskCounter, comparator);
        threadPool.execute(new QuickSortTask<>(a, tools));
        synchronized (tools.taskCounter) {
            while (tools.taskCounter.get() > 0) {
                tools.taskCounter.wait();
            }
        }
        threadPool.shutdown();
    }
}
