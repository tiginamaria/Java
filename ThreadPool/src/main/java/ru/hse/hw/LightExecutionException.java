package ru.hse.hw;

/**
 * Exception in process of calculation LightFuture in thread pool
 */
public class LightExecutionException extends Exception {

    /**
     * Constructor wraps given exception to LightExecutionException
     * @param throwable given exception
     */
    public LightExecutionException(Throwable throwable) {
        super(throwable);
    }
}
