package ru.hse.hw.samples;

import ru.hse.hw.annotation.Test;

public class Sample {
    @Test
    public static void m1() {
    } // Test should pass

    public static void m2() {
    }

    @Test
    public static void m3() { // Test should fail
        throw new RuntimeException("Boom");
    }

    public static void m4() {
    }

    public static void m6() {
    }

    @Test(expected = java.lang.RuntimeException.class)
    public static void m7() { // Test should fail
        throw new RuntimeException("Crash");
    }

    @Test(ignore = "why not")
    public static void m8() {
    }
}
