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

    class NoException extends Throwable { }

    Class<? extends Throwable> EXPECTED = NoException.class;
    String IGNORE = "NO REASON";

    Class<? extends Throwable> expected() default NoException.class;

    String ignore() default IGNORE;
}

