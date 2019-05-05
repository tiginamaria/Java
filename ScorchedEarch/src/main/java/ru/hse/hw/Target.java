package ru.hse.hw;

import javafx.geometry.Point2D;


public class Target {
    private boolean done = false;
    private double x;
    private double y;
    private double radios;

    public Target(double x, double y, double radios) {
        this.x = x;
        this.y = y;
        this.radios = radios;
    }

    public boolean contains(Point2D point) {
        return (point.getX() - x) * (point.getX() - x) + (point.getY() - y) * (point.getY() - y) <= radios * radios;
    }

    public void markDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }
}
