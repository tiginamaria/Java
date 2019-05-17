package ru.hse.hw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetTest {

    @Test
    void decreaseLivesSmallBullet() {
        var target = new Target(100, 100);
        Bullet bullet1 = new Bullet(100, 91, 3, 60);
        for (int i = 0; i < 2; i++) {
            assertTrue(bullet1.hitTarget(target));
            assertFalse(target.isDone());
        }
        Bullet bullet2 = new Bullet(100, 89, 3, 60);
        assertFalse(bullet2.hitTarget(target));
        assertFalse(target.isDone());

        assertTrue(bullet1.hitTarget(target));
        assertTrue(target.isDone());
    }

    @Test
    void decreaseLivesMiddleBullet() {
        var target = new Target(100, 100);
        Bullet bullet1 = new Bullet(100, 89, 6, 60);
        assertTrue(bullet1.hitTarget(target));
        assertFalse(target.isDone());

        Bullet bullet2 = new Bullet(100, 81, 6, 60);
        assertFalse(bullet2.hitTarget(target));
        assertFalse(target.isDone());

        assertTrue(bullet1.hitTarget(target));
        assertTrue(target.isDone());
    }

    @Test
    void decreaseLivesBigBullet() {
        var target = new Target(100, 100);
        Bullet bullet1 = new Bullet(100, 74, 9, 60);
        assertFalse(bullet1.hitTarget(target));
        assertFalse(target.isDone());

        Bullet bullet2 = new Bullet(100, 81, 9, 60);
        assertTrue(bullet2.hitTarget(target));
        assertTrue(target.isDone());
    }
}