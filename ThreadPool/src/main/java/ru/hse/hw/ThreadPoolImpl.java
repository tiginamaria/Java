package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl {

    private int threadCounter;

    private Queue<LightFuture> taskQueue = new LinkedList<>() {
    };

    private ThreadImpl[] threadPool;

    public ThreadPoolImpl(int threadCounter) {
        this.threadCounter = threadCounter;
        threadPool = new ThreadImpl[threadCounter];
        for (int i = 0; i < threadCounter; i++) {
            threadPool[i] = new ThreadImpl();
            threadPool[i].start();
        }
    }

    public <T> LightFutureImpl<T> execute(@NotNull Supplier<T> supplier) {
        var task = new LightFutureImpl<>(supplier);
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
        return task;
    }

    private class ThreadImpl extends Thread {
        public void run() {
            LightFuture task;
            while (true) {
                synchronized(taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                        }
                    }
                    task = taskQueue.poll();
                }
                try {
                    task.get();
                } catch (RuntimeException e) {
                    System.out.println("Thread threadPool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }

    private class LightFutureImpl<T> implements LightFuture<T> {
        private volatile Supplier<T> supplier;
        private T result;

        LightFutureImpl(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return supplier == null;
        }

        @Override
        public T get() {
            if (!isReady()) {
                synchronized (this) {
                    if (!isReady()) {
                        result = supplier.get();
                        supplier = null;
                    }
                }
            }
            return result;
        }

        @Override
        public <R> LightFuture<R> thenApply(Function<T, R> function) {
           Supplier<R> newSupplier = () -> {
                T currentTaskResult = null;
                try {
                    currentTaskResult = LightFutureImpl.this.get();
                } catch (Exception e) {
                    System.out.println("Interrupted due to an issue: " + e.getMessage());
                }
                return function.apply(currentTaskResult);
           };

            LightFuture<R> task = new LightFutureImpl<>(newSupplier);

            synchronized (taskQueue) {
                taskQueue.add(task);
                taskQueue.notify();
            }

            return task;
        }
    }
}
