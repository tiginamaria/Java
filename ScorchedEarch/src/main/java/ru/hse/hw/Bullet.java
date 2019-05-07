package ru.hse.hw;

import javafx.geometry.Point2D;

import java.util.List;


public class Bullet {
    private static final double G = 9.8;
    private Point2D position;
    private double speed;
    private double angle;
    private double time;

    public Bullet(Point2D position, double angle, double speed) {
        this.position = position;
        this.angle = Math.toRadians(90 - angle);
        this.speed = speed;
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

    public void move() {
        System.out.println(position.getX() + " " + position.getY());
        position = new Point2D(
                position.getX() + time * speed * Math.cos(angle),
                position.getY() - time * speed * Math.sin(angle) + G * time * time / 2);
        time += 0.2;
    }

    public boolean hit(List<Mountain> mountains) {
        for (var mountain : mountains) {
            if (mountain.contains(position.getX(), position.getY())) {
                return true;
            }
        }
        return false;
    }
}
