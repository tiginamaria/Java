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
        var mountain1 = new Mountain(0, 0, 6, 6);
        var mountain2 = new Mountain(6, 6, 0, 12);
        var mountains = new ArrayList<Mountain>();
        mountains.add(mountain1);
        mountains.add(mountain2);
        //for(int i = 0; i)
        tank.move(mountains, Side.RIGHT);
    }
}