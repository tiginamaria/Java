package ru.hse.hw;

import ru.hse.hw.annotation.*;

public class FailBeforeSample {
    @Test
    public static void test1() {
    } // Test should pass

    @After
    public static void method1() {
    }

    @Test
    public static void test2() { // Test should fail
        throw new RuntimeException("Boom");
    }

    public void method2() {
    }

    @Before
    public static void method3() throws IllegalAccessException {
        throw new IllegalAccessException("before method fail");
    }

    @Test(expected = java.lang.RuntimeException.class, ignore = "ignore with exception")
    public void test3() {
    }

    @Test(expected = java.lang.InterruptedException.class)
    public static void test4() throws InterruptedException {
        Thread.sleep(10);
    }

    @Test(ignore = "why not")
    public static void test5() {
    }

    @BeforeClass
    public static void method4() {

    }

    @AfterClass
    public static void method5() {

    }

    @Test(expected = java.lang.RuntimeException.class)
    public static void test6() { // Test should fail
        throw new RuntimeException("Crash");
    }

}

