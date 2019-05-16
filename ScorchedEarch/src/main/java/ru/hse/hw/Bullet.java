package ru.hse.hw;
import java.util.List;


public class Bullet {
    private static final double G = 9.8;
    private double x, y;
    private double speed;
    private double angle;
    private double time;
    private double size;

    public Bullet(double x, double y, double size,  double angle, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.angle = Math.toRadians(90 - angle);
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move() {
        x += time * speed * Math.cos(angle);
        y += -time * speed * Math.sin(angle) + G * time * time / 2;
        time += 0.2;
    }

    public boolean hitMountains(List<Mountain> mountains) {
        for (var mountain : mountains) {
            if (mountain.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean hitTarget(Target target) {
        if ((target.getX() - x) * (target.getX() - x) + (target.getY() - y) * (target.getY() - y) <= target.getR(size) * target.getR(size)) {
            target.markDone();
            return true;
        }
        return false;
    }

}
