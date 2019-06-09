package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MountainTest {

    @Test
    void mountainFunctionTest() {
        var mountain1 = new Mountain(0, 0, 6, 6);
        for (int i = 1; i < 6; i++) {
            assertEquals(i, mountain1.mountainFunction(i));
        }
        var mountain2 = new Mountain(0, 0, 6, 12);
        for (int i = 1; i < 6; i++) {
            assertEquals(2 * i, mountain2.mountainFunction(i));
        }
    }

    @Test
    void pointIsOnMountainTest() {
        var mountain = new Mountain(1, 1, 6, 6);
        assertTrue(mountain.isOnMountain(2));
        assertFalse(mountain.isOnMountain(7));
        assertFalse(mountain.isOnMountain(0));
        assertFalse(mountain.isOnMountain(6));
        assertTrue(mountain.isOnMountain(1));
    }

    @Test
    void mountainContainsPointsTest() {
        var mountain = new Mountain(0, 0, 6, 6);
        assertTrue(mountain.contains(2, 3, 1));
        assertTrue(mountain.contains(1, 5, 1));
        assertFalse(mountain.contains(6, 6, 1));
        assertFalse(mountain.contains(2, 1, 1));
        assertFalse(mountain.contains(10, 10, 1));
        assertFalse(mountain.contains(-1, -1, 1));
    }

    @Test
    void getRandomPointOverMountainXText() {
        var mountain = new Mountain(0, 0, 6, 6);
        var random = new Random();
        var x = mountain.getRandomOverMountainX(random);
        assertFalse(mountain.contains(x, mountain.mountainFunction(x), 1));
    }

    @Test
    void getRandomOverMountainY() {
        var mountain = new Mountain(100, 100, 200, 200);
        var random = new Random();
        var x = mountain.getRandomOverMountainX(random);
        var y = mountain.getRandomOverMountainY(random, x, 400);
        assertFalse(mountain.contains(x, y, 1));
        assertTrue(y < 400);
    }

    @Test
    void getAngle() {
        var mountain1 = new Mountain(0, 0, 6, 6);
        System.out.print(mountain1.getAngle());
        assertEquals(1.0, mountain1.getAngle());
    }
}