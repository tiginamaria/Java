package ru.hse.hw;

import javafx.geometry.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void moveBarrelTest() {
        var tank = new Tank(100, 100);
        tank.moveBarrel(Side.BOTTOM);
        assertEquals(-1, tank.getBarrelAngle());
        tank.moveBarrel(Side.TOP);
        assertEquals(0, tank.getBarrelAngle());
        tank.moveBarrel(Side.TOP);
        assertEquals(1, tank.getBarrelAngle());
        for (int i = 0; i < 90; i++) {
            tank.moveBarrel(Side.TOP);
        }
        tank.moveBarrel(Side.TOP);
        assertEquals(90, tank.getBarrelAngle());
        tank.moveBarrel(Side.TOP);
        assertEquals(90, tank.getBarrelAngle());

        for (int i = 0; i < 180; i++) {
            tank.moveBarrel(Side.BOTTOM);
        }
        tank.moveBarrel(Side.BOTTOM);
        assertEquals(-90, tank.getBarrelAngle());
        tank.moveBarrel(Side.BOTTOM);
        assertEquals(-90, tank.getBarrelAngle());
    }

    @Test
    void moveTankTest() {
        var tank = new Tank(0, 0);
        var mountain1 = new Mountain(0, 0, 60, 60);
        var mountain2 = new Mountain(60, 60, 120, 0);
        var mountains = new ArrayList<Mountain>();
        mountains.add(mountain1);
        mountains.add(mountain2);

        while (tank.getX() <= 60) {
            assertEquals(tank.getY(), mountain1.mountainFunction(tank.getX()));
            tank.move(mountains, Side.RIGHT);
        }

        double xR = tank.getX();
        double yR;
        tank.move(mountains, Side.RIGHT);

        while (tank.getX() > xR) {
            xR = tank.getX();
            yR = tank.getY();
            assertEquals(yR, mountain2.mountainFunction(tank.getX()));
            tank.move(mountains, Side.RIGHT);
        }

        xR = tank.getX();
        yR = tank.getY();
        tank.move(mountains, Side.RIGHT);
        assertEquals(xR, tank.getX());
        assertEquals(yR, tank.getY());

        while (tank.getX() >= 60) {
            assertEquals(tank.getY(), mountain2.mountainFunction(tank.getX()));
            tank.move(mountains, Side.LEFT);
        }

        double xL = tank.getX();
        double yL;
        tank.move(mountains, Side.LEFT);

        while (tank.getX() < xL) {
            xL = tank.getX();
            yL = tank.getY();
            assertEquals(yL, mountain1.mountainFunction(tank.getX()));
            tank.move(mountains, Side.LEFT);
        }

        xL = tank.getX();
        yL = tank.getY();
        tank.move(mountains, Side.LEFT);
        assertEquals(xL, tank.getX());
        assertEquals(yL, tank.getY());
    }
}