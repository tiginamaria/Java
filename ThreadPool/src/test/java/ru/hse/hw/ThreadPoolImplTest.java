package ru.hse.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolImplTest {

    private static final int THREAD_COUNTER = 20;
    private ThreadPoolImpl threadPool;

    @BeforeEach
    private void threadPoolInit() {
        threadPool = new ThreadPoolImpl(THREAD_COUNTER);
    }

    private ArrayList<Integer> getRandomIntegerArray() {
        int size = new Random().nextInt(1000);
        ArrayList<Integer> array = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            array.add(new Random().nextInt());
        }
        return array;
    }

    @Test
    void simpleThreadPoolTest() {
        Supplier<Integer> supplier = () -> {
            return 5;
        };
        threadPool.execute(supplier);
        System.out.println(supplier.get());
        threadPool.shutdown();
    }

    @Test
    void sortArraysThreadPoolTest() {
        int taskCounter = new Random().nextInt(100) + 1;
        ArrayList<ArrayList<Integer>> toSortArrays = new ArrayList<>();
        ArrayList<Supplier<ArrayList<Integer>>> tasks = new ArrayList<>();

        for (int i = 0; i < taskCounter; i++) {
            ArrayList<Integer> array = getRandomIntegerArray();
            toSortArrays.add(new ArrayList<>(array));
            Supplier<ArrayList<Integer>> supplier = () -> {
                Collections.sort(array);
                return array;
            };
            tasks.add(supplier);
        }

        for (int i = 0; i < taskCounter; i++) {
            threadPool.execute(tasks.get(i));
        }

        for (int i = 0; i < taskCounter; i++) {
            Collections.sort(toSortArrays.get(i));
            assertEquals(toSortArrays.get(i), tasks.get(i).get());
        }
        threadPool.shutdown();
    }
}