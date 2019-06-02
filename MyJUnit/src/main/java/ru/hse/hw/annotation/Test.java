package ru.hse.hw.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a test method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {

    /**
     * Class for not expected exception
     */
    class NoException extends Throwable { }

    /**
     * Default value for exception
     */
    Class<? extends Throwable> EXPECTED = NoException.class;

    /**
     * Default value for ignore
     */
    String IGNORE = "NO REASON";

    /**
     * Get expected exception from annotation
     * @return exception
     */
    Class<? extends Throwable> expected() default NoException.class;

    /**
     * Get ignore reason of the test
     * @return reason to ignore
     */
    String ignore() default IGNORE;
}

