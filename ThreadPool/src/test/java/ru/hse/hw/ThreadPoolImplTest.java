package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadPoolImplTest {

    private static final int THREAD_COUNTER = 20;

    private ThreadPoolImpl threadPool = new ThreadPoolImpl(THREAD_COUNTER);

    private ArrayList<Integer> getRandomIntegerArray() {
        int size = new Random(1000).nextInt();
        ArrayList<Integer> array = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            array.add(new Random(10000).nextInt());
        }
        return array;
    }

    @Test
    void generalThreadPoolSortArraysTest() {
        int taskCounter = new Random(10000).nextInt();
        ArrayList<ArrayList<Integer>> toSortArrays = new ArrayList<>();
        ArrayList<Supplier<ArrayList<Integer>>> tasks = new ArrayList<>();

        for (int i = 0; i < taskCounter; i++) {
            ArrayList<Integer> array = getRandomIntegerArray();
            toSortArrays.add((ArrayList<Integer>)array.clone());
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
            assertEquals(toSortArrays.get(i), tasks.get(i));
        }
    }
}