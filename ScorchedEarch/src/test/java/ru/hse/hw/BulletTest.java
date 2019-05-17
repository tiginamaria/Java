package ru.hse.hw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    @Test
    void moveTest() {
        Bullet bullet1 = new Bullet(0, 0, 6, 60);
        double x1 = bullet1.getX();
        double y1 = bullet1.getY();
        bullet1.move();
        assertNotEquals(x1, bullet1.getX());
        assertNotEquals(y1, bullet1.getY());
    }

    @Test
    void hitMountains() {
        var mountain1 = new Mountain(0, 0, 60, 60);
        var mountain2 = new Mountain(60, 60, 120, 0);
        var mountains = new ArrayList<Mountain>();
        mountains.add(mountain1);
        mountains.add(mountain2);
        Bullet bullet1 = new Bullet(30, 0, 6, 60);
        assertFalse(bullet1.hitMountains(mountains));
        Bullet bullet2 = new Bullet(0, 30, 6, 60);
        assertTrue(bullet2.hitMountains(mountains));
    }

    @Test
    void hitTarget() {
        var target = new  Target(100, 100);
        Bullet bullet1 = new Bullet(100, 91, 3, 60);
        assertTrue(bullet1.hitTarget(target));
        Bullet bullet2 = new Bullet(100, 89, 3, 60);
        assertFalse(bullet2.hitTarget(target));

        Bullet bullet3 = new Bullet(100, 89, 6, 60);
        assertTrue(bullet3.hitTarget(target));
        Bullet bullet4 = new Bullet(100, 81, 6, 60);
        assertFalse(bullet4.hitTarget(target));

        Bullet bullet5 = new Bullet(100, 81, 9, 60);
        assertTrue(bullet5.hitTarget(target));
        Bullet bullet6 = new Bullet(100, 74, 9, 60);
        assertFalse(bullet6.hitTarget(target));
    }
}