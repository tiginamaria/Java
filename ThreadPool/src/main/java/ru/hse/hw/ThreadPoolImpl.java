package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  An service that executes each submitted task using one of possibly several pooled threads
 */
public class ThreadPoolImpl {

    /**
     * Number of working threads
     */
    private int threadCounter;

    /**
     * Queue of task to run by threads
     */
    private final Queue<Task> taskQueue = new LinkedList<>();

    /**
     * Pool of available threads
     */
    private Executor[] threadPool;

    /**
     * Constructor of thread pool
     * @param threadCounter number of available threads
     */
    public ThreadPoolImpl(int threadCounter) {
        this.threadCounter = threadCounter;
        threadPool = new Executor[threadCounter];
        for (int i = 0; i < threadCounter; i++) {
            threadPool[i] = new Executor();
            threadPool[i].start();
        }
    }

    /**
     * Execute all available threads in pool
     * @param supplier task to execute
     * @param <T> type task's returned value
     * @return LightFuture - box, where result of task is stored
     */
    public <T> LightFutureImpl<T> execute(@NotNull Supplier<T> supplier) {
        Task<T> task = new Task<>(supplier);
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
        return task.lightFuture;
    }

    /**
     * Interrupt all threads in pool
     */
    public void shutdown() {
        for (int i = 0; i < threadCounter; i++) {
            threadPool[i].interrupt();
        }
    }

    /**
     * Implementation for thread, which uses common queue to take tasks to execute
     */
    private class Executor extends Thread {
        /**
         * Execute available task from queue
         */
        @Override
        public void run() {
            Task task;
            while (!Thread.interrupted()) {
                synchronized(taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = taskQueue.poll();
                }
                try {
                    task.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Task for threads, stores the task to execute, runs it, put the result into the LightFuture
     * @param <T> type of the result
     */
    private class Task<T> implements Runnable {

        /**
         * Task to execute
         */
        private final Supplier<T> supplier;

        /**
         * Box with result
         */
        private final LightFutureImpl<T> lightFuture;

        /**
         * Constructor, which sets to values to final fields, so ensure that they won't be null
         * @param supplier task to run
         */
        public Task(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
            lightFuture = new LightFutureImpl<>();
        }

        /**
         * Execute supplier's task ans sets the result to the LightFuture
         */
        @Override
        public void run() {
            synchronized (lightFuture) {
                T result = null;
                try {
                    result = supplier.get();
                } catch (Exception e) {
                    lightFuture.setException(e);
                }
                lightFuture.setResult(result);
                lightFuture.notify();
            }
        }
    }

    /**
     * Special box with result of task, which calculates only ones
     * @param <T> type of he result
     */
    private class LightFutureImpl<T> implements LightFuture<T> {

        /**
         * Occurred exception
         */
        private Exception exception;

        /**
         * Flag, which if true, when task has already been calculated
         */
        private volatile boolean isReady;

        /**
         * Calculated result
         */
        private T result;

        /**
         * Check if task has already been calculated
         * @return if task is calculated then true, other false
         */
        @Override
        public boolean isReady() {
            return isReady;
        }

        /**
         * Set result of supplier execution
         * @param result
         */
        private void setResult(T result) {
            this.result = result;
            isReady = true;
        }

        /**
         * If the exception occur in supplier method get(), exception is translated to here
         * @param e occurred exception
         */
        private void setException(Exception e) {
            if (exception == null) {
                exception = new Exception(e);
            } else {
                exception.addSuppressed(e);
            }
        }

        /**
         * Get calculated tasks result
         * @return result of task
         */
        @Override
        public T get() throws LightExecutionException, InterruptedException {
            while (!isReady) {
                synchronized (this) {
                    if (!isReady) {
                        wait();
                    }
                }
            }
            if (exception != null) {
                throw new LightExecutionException(exception);
            }
            return result;
        }

        /**
         * Create a new task by applying given function to current task
         * @param function function which can be applied to current task
         * @param <R> type of the new task's result
         * @return new task
         */
        @Override
        public <R> LightFuture<R> thenApply(Function<T, R> function) {
            Supplier<R> newSupplier = () -> {
                T currentTaskResult;
                try {
                    currentTaskResult = LightFutureImpl.this.get();
                } catch (LightExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return function.apply(currentTaskResult);
            };

            return execute(newSupplier);
        }
    }

}
