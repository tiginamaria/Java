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

    private static class GeneralTaskTools<T> {
        /**
         * Pool of threads
         */
        private ExecutorService threadPool;

        /**
         * Current total number of uncompleted thread tasks
         */
        private AtomicInteger taskCounter;

        private Comparator<? super T> comparator;

        /**
         * Constructor sets common for all threads comparator, thead pool and number of current tasks;
         * @param threadPool
         * @param taskCounter
         */
        private GeneralTaskTools(ExecutorService threadPool, AtomicInteger taskCounter) {
            this.threadPool = threadPool;
            this.taskCounter = taskCounter;
        }
    }

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
         * Hold general information about tall tasks in qsort
         */
        private GeneralTaskTools tools;

        /**
         * Primitive constructor for qSort task
         *
         * @param a
         * @param left
         * @param right
         */
        private QuickSortTask(T[] a, int left, int right, GeneralTaskTools tools) {
            this.a = a;
            this.left = left;
            this.right = right;
            this.tools = tools;
        }

        /**
         * Constructor for first task
         *
         * @param a array to sort
         * @param tools number of threads
         */
        public QuickSortTask(T[] a, GeneralTaskTools tools) {
            this(a, 0, a.length - 1, tools);
        }

        /**
         * Function which runs partition of given interval into two parts and recursively sorts this parts of array
         */
        @Override
        public void run() {

            if (left < right) {
                int pivotIndex = partition(a, left, right);
                tools.taskCounter.addAndGet(2);
                tools.threadPool.execute(new QuickSortTask<>(a, left, pivotIndex, tools));
                tools.threadPool.execute(new QuickSortTask<>(a, pivotIndex + 1, right, tools));
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
        return left + randomize.nextInt(right - left + 1);
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
        var threadPool = Executors.newFixedThreadPool(threadCounter);
        var taskCounter = new AtomicInteger(1);
        var tools = new GeneralTaskTools<T>(threadPool, taskCounter);
        threadPool.execute(new QuickSortTask<>(a, tools));
        synchronized (taskCounter) {
            while (taskCounter.get() > 0) {
                taskCounter.wait();
            }
        }
        threadPool.shutdown();
    }
}
