package ru.hse.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolImplTest {

    private static final int THREAD_COUNTER = 20;
    private static final int TASK_COUNTER = 1000;
    private ThreadPoolImpl threadPool;
    private final Random random = new Random(System.currentTimeMillis());

    @BeforeEach
    private void threadPoolInit() {
        threadPool = new ThreadPoolImpl(THREAD_COUNTER);
    }

    private ArrayList<Integer> getRandomIntegerArray() {
        var size = random.nextInt(1000);
        var array = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            array.add(random.nextInt());
        }
        return array;
    }

    @Test
    void sameValueReturnsThreadPoolTest() throws LightExecutionException, InterruptedException {
        int[] value = new int[1];
        value[0] = 1;
        Supplier<Integer> supplier = () -> value[0];
        var result = threadPool.execute(supplier);
        assertEquals(value[0], (int)result.get());
        value[0] = 2;
        assertNotEquals(value[0], result.get());
        threadPool.shutdown();
    }

    @Test
    void sameObjectReturnsThreadPoolTest() throws LightExecutionException, InterruptedException {
        Supplier<ArrayList<Integer>> supplier = () -> {
            ArrayList<Integer> array = getRandomIntegerArray();
            Collections.sort(array);
            return array;
        };
        var result = threadPool.execute(supplier);
        assertSame(result.get(), result.get());
        threadPool.shutdown();
    }

    @Test
    void sortArraysThreadPoolTest() throws LightExecutionException, InterruptedException {
        int taskCounter = random.nextInt(TASK_COUNTER) + 1;
        var toSortArrays = new ArrayList<ArrayList<Integer>>();
        var tasks = new ArrayList<Supplier<ArrayList<Integer>>>();
        var results = new ArrayList<LightFuture<ArrayList<Integer>>>();

        for (int i = 0; i < taskCounter; i++) {
            var array = getRandomIntegerArray();
            toSortArrays.add(new ArrayList<>(array));
            Supplier<ArrayList<Integer>> supplier = () -> {
                Collections.sort(array);
                return array;
            };
            tasks.add(supplier);
        }

        for (int i = 0; i < taskCounter; i++) {
            results.add(threadPool.execute(tasks.get(i)));
        }

        for (int i = 0; i < taskCounter; i++) {
            Collections.sort(toSortArrays.get(i));
            assertEquals(toSortArrays.get(i), results.get(i).get());
            assertSame(results.get(i).get(),  results.get(i).get());
        }
        threadPool.shutdown();
    }

    @Test
    void thenApplyArraysThreadPoolTest() throws LightExecutionException, InterruptedException {
        int taskCounter = random.nextInt(TASK_COUNTER) + 1;
        var toSortArrays = new ArrayList<ArrayList<Integer>>();
        var tasks = new ArrayList<Supplier<ArrayList<Integer>>>();
        var results = new ArrayList<LightFuture<ArrayList<Integer>>>();
        for (int i = 0; i < taskCounter; i++) {
            var array = getRandomIntegerArray();
            toSortArrays.add(new ArrayList<>(array));
            Supplier<ArrayList<Integer>> supplier = () -> {
                Collections.sort(array);
                return array;
            };
            tasks.add(supplier);
        }

        for (int i = 0; i < taskCounter; i++) {
            var result = threadPool.execute(tasks.get(i));
            results.add(result.thenApply((ArrayList<Integer> array) -> {
                for (int j = 0; j < array.size(); j++) {
                    Integer value = array.get(j);
                    if (value % 2 == 0) {
                        array.remove(value);
                    }
                }
                return array;
            }));
        }

        for (int i = 0; i < taskCounter; i++) {
            var array = toSortArrays.get(i);
            Collections.sort(array);
            for (int j = 0; j < array.size(); j++) {
                Integer value = array.get(j);
                if (value % 2 == 0) {
                    array.remove(value);
                }
            }
            assertEquals(toSortArrays.get(i), results.get(i).get());
            assertSame(results.get(i).get(),  results.get(i).get());
        }
        threadPool.shutdown();
    }

    @Test
    void lightFutureExceptionThreadPoolTest() throws LightExecutionException, InterruptedException {
        Supplier<Integer> supplier = () -> null;
        var result = threadPool.execute(supplier);
        Function<Integer, Integer> function = (x) -> 2 * x;
        var totalResult = result.thenApply(function);
        assertThrows(LightExecutionException.class, totalResult::get);
        threadPool.shutdown();
    }

    @Test
    void countActiveThreadsThreadPoolTest() {
        assertTrue(THREAD_COUNTER <= Thread.activeCount());
    }
}