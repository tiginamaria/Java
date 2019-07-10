package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  An service that executes each submitted task using one of possibly several pooled threads
 */
public class ThreadPoolImpl {

    /**
     * Queue of task to run by threads
     */
    private final Queue<Task<?>> taskQueue = new LinkedList<>();

    /**
     * Pool of available threads
     */
    private final Thread[] threadPool;

    /**
     * Flag which is true when thread pool was shut down
     */
    private final AtomicBoolean shutDown = new AtomicBoolean(false);

    /**
     * Constructor of thread pool
     * @param threadCounter number of available threads
     */
    public ThreadPoolImpl(int threadCounter) {
        threadPool = new Thread[threadCounter];
        for (int i = 0; i < threadCounter; i++) {
            threadPool[i] = new Thread(new Executor());
            threadPool[i].start();
        }
    }

    /**
     * Executor runs tasks from queue
     */
    private class Executor implements Runnable {
        @Override
        public void run() {
            Task<?> task;
            while (!shutDown.get()) {
                synchronized (taskQueue) {
                    if (shutDown.get()) {
                        return;
                    }
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = taskQueue.poll();
                }
                task.run();
            }
        }
    }

    /**
     * Start execution of given supplier
     * @param supplier for task to execute
     * @param <T> type task's returned value
     * @return LightFuture - box, where result of task is stored
     */
    public <T> LightFuture<T> execute(@NotNull Supplier<T> supplier) throws LightExecutionException {
        Task<T> task = new Task<>(supplier);
        return executeTask(task);
    }

    /**
     * Put task in task queue to execute it
     * @param task task to execute
     * @param <T> type task's returned value
     * @return LightFuture - box, where result of task is stored
     */
    private <T> LightFuture<T> executeTask(@NotNull Task<T> task) throws LightExecutionException {
        synchronized (shutDown) {
            if (!shutDown.get()) {
                synchronized (taskQueue) {
                    taskQueue.add(task);
                    taskQueue.notify();
                }
                return task;
            }
            throw new LightExecutionException(new IllegalStateException("Thread was shut down. Could not complete execution."));
        }
    }

    /**
     * Interrupt all threads in pool
     */
    public void shutdown() throws InterruptedException {
        synchronized (shutDown) {
            shutDown.set(true);
        }
        for (var thread : threadPool) {
            thread.interrupt();
            thread.join();
        }
    }

    /**
     * Task for threads, stores the task to execute, runs it, put the result into the LightFuture
     * @param <T> type of the result
     */
    private class Task<T> implements Runnable, LightFuture<T> {

        /**
         * Task to execute
         */
        private final Supplier<T> supplier;

        /**
         * Occurred exception
         */
        private volatile Exception exception;

        /**
         * Flag, which if true, when task has already been calculated
         */
        private final AtomicBoolean isReady;

        /**
         * Calculated result
         */
        private T result;

        /**
         * Task to do after current task
         */
        private final List<Task<?>> thenApplyTasks = new LinkedList<>();

        /**
         * Constructor for task
         * @param supplier function to calculate
         */
        private Task(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
            isReady = new AtomicBoolean(false);
        }

        /**
         * Execute supplier's task ans sets the result to the LightFuture
         */
        @Override
        public void run() {
            try {
                result = supplier.get();
            } catch (Exception e) {
                setException(e);
            }

            isReady.set(true);

            try {
                executeThenApplyLightFuture();
            } catch (Exception e) {
                setException(e);
            }
            synchronized (this) {
                this.notify();
            }
        }

        /**
         * Functions, which use the result(thenApplyTasks) of current task are stored in queue inside the task, until the calculation of current function.
         * After calculation thenApplyTasks putted into global queue
         */
        private void executeThenApplyLightFuture() throws LightExecutionException {
            synchronized (thenApplyTasks) {
                for (var task : thenApplyTasks) {
                    executeTask(task);
                }
            }
        }

        /**
         * Check if task has already been calculated
         * @return if task is calculated then true, other false
         */
        @Override
        public boolean isReady() {
            return isReady.get();
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
            while (!isReady.get()) {
                synchronized (this) {
                    if (!isReady.get()) {
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
         * @param <R> type of the new task's result
         * @param function function which can be applied to current task
         * @return new task
         */
        @Override
        public <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function) throws LightExecutionException {
            Supplier<R> newSupplier = () -> {
                try {
                    return function.apply(get());
                } catch (LightExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };

            var task = new Task<>(newSupplier);

            synchronized (isReady) {
                if (isReady.get()) {
                    return executeTask(task);
                } else {
                    synchronized (thenApplyTasks) {
                        thenApplyTasks.add(task);
                        thenApplyTasks.notifyAll();
                    }
                }
            }
            return task;
        }
    }
}
