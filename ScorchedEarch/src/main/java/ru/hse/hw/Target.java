package ru.hse.hw;


public class Target {
    private boolean done = false;
    private double x;
    private double y;
    private double smallRadius;
    private double bigRadius;

    public Target(double x, double y, double smallRadius, double bigRaius) {
        this.x = x;
        this.y = y;
        this.smallRadius = smallRadius;
        this.bigRadius = bigRaius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR(double size) {
        if (size < 3) {
            return smallRadius;
        }
        return bigRadius;
    }

    public void markDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }
}
