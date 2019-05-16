package ru.hse.hw;

import javafx.geometry.Side;
import java.util.List;

import static javafx.geometry.Side.*;
import static javafx.geometry.Side.RIGHT;


public class Tank {

    private double x, y;
    private final static double step = 0.8;
    private final static int barrelRotateAngle = 1;
    private double barrelAngle;
    private double tankAngle;

    public Tank(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double moveBarrel(Side side) {
        if ((side == BOTTOM && barrelAngle > -90.0) || (side == TOP && barrelAngle < 90.0)) {
            var angle = (side == TOP) ? barrelRotateAngle : -barrelRotateAngle;
            barrelAngle += angle;
            return angle;
        }
        return 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getBarrelAngle() {
        return barrelAngle;
    }

    public double getTankAngle() {
        return tankAngle;
    }

    public void move(List<Mountain> mountains, Side side) {
        double newX = x + (side == RIGHT ? step : -step);
        for (var mountain : mountains) {
            if (mountain.isOnMountain(newX)) {
                x = newX;
                y = mountain.mountainFunction(newX);
                tankAngle = mountain.getAngle();
            }
        }
    }
}
