package ru.hse.hw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetTest {

    @Test
    void decreaseLivesSmallBullet() {
        var target = new Target(100, 100);
        var bullet1 = new Bullet(100, 100, 0, 60);
        for (int i = 0; i < 2; i++) {
            assertTrue(bullet1.hitTarget(target));
            assertFalse(target.isDone());
        }
        var bullet2 = new Bullet(100 - Bullet.SMALL_RADIUS - Target.SMALL_RADIUS - 1, 100 - Bullet.SMALL_RADIUS - Target.SMALL_RADIUS - 1, 0, 60);
        assertFalse(bullet2.hitTarget(target));
        assertFalse(target.isDone());

        assertTrue(bullet1.hitTarget(target));
        assertTrue(target.isDone());
    }

    @Test
    void decreaseLivesMiddleBullet() {
        var target = new Target(100, 100);
        var bullet1 = new Bullet(100, 100, 1, 60);
        assertTrue(bullet1.hitTarget(target));
        assertFalse(target.isDone());

        var bullet2 = new Bullet(100 - Bullet.MIDDLE_RADIUS - Target.MIDDLE_RADIUS - 1, 100, 1, 60);
        assertFalse(bullet2.hitTarget(target));
        assertFalse(target.isDone());

        assertTrue(bullet1.hitTarget(target));
        assertTrue(target.isDone());
    }

    @Test
    void decreaseLivesBigBullet() {
        var target = new Target(100, 100);
        var bullet1 = new Bullet(100, 100 - Bullet.BIG_RADIUS - Target.BIG_RADIUS - 1, 2, 60);
        assertFalse(bullet1.hitTarget(target));
        assertFalse(target.isDone());

        var bullet2 = new Bullet(100, 100, 2, 60);
        assertTrue(bullet2.hitTarget(target));
        assertTrue(target.isDone());
    }
}