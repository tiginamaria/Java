package ru.hse.hw;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import java.util.List;

import static javafx.geometry.Side.*;
import static javafx.geometry.Side.RIGHT;


public class Tank {

    private Point2D position;
    private final static double step = 0.5;
    private final static int barrelRotateAngle = 1;
    private double barrelAngle;
    private double tankAngle;

    public Tank(double x, double y) {
        position = new Point2D(x, y);
    }

    public double moveBarrel(Side side) {
        if ((side == BOTTOM && barrelAngle > -90.0) || (side == TOP && barrelAngle < 90.0)) {
            var angle = (side == TOP) ? barrelRotateAngle : -barrelRotateAngle;
            barrelAngle += angle;
            return angle;
        }
        return 0;
    }

    public void setX(double x) {
        position = new Point2D(x, position.getY());
    }

    public void setY(double y) {
        position = new Point2D(position.getX(), y);
    }

    public Point2D getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setBarrelAngle(double angle) {
        barrelAngle = angle;
    }

    public double getBarrelAngle() {
        return barrelAngle;
    }

    public double getTankAngle() {
        return tankAngle;
    }

    public void rotate(double angle) {
        tankAngle = angle;
    }

    public void move(List<Mountain> mountains, Side side) {
        double newX = position.getX() + (side == RIGHT ? step : -step);
        for (var mountain : mountains) {
            if (mountain.contains(newX)) {
                position = new Point2D(newX, mountain.mountainFunction(newX));
                tankAngle = mountain.getAngle();
            }
        }
    }
}
