package ru.hse.hw;

import java.util.function.Function;

/**
 * Interface for tasks in ThreadPool
 */
public interface LightFuture<T> {
    /**
     * Check if task has already been calculated
     * @return if task is calculated then true, other false
     */
    boolean isReady();

    /**
     * Get calculated tasks result
     * @return result of task
     */
    T get() throws InterruptedException, LightExecutionException;

    /**
     * Create a new task by applying given function to current task
     * @param function function which can be applied to current task
     * @param <R> type of the new task's result
     * @return new task
     */
    <R> LightFuture<R> thenApply(Function<? super T, R> function) throws LightExecutionException;

}
