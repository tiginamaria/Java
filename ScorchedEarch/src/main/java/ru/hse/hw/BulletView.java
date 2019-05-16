package ru.hse.hw;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class BulletView extends Circle {

    private Bullet bullet;

    public BulletView(Point2D position, double angle, double radius, double speed) {
        super(radius);
        setPosition(position);
        bullet = new Bullet(position.getX(), position.getY(), radius, angle, speed);
        setFill(Color.BLACK);
    }

    public void makeBulletMove() {
        bullet.move();
        setCenterX(bullet.getX());
        setCenterY(bullet.getY());
    }

    public void setPosition(Point2D position) {
        setCenterX(position.getX());
        setCenterY(position.getY());
    }

    public Point2D getPosition() {
        return new Point2D(bullet.getX(), bullet.getY());
    }

    public boolean onScene(double width, double height) {
        System.out.println(getCenterX() > 0 && getCenterX() < width && getCenterY() > 0 && getCenterY() < height);
        return getCenterX() > 0 && getCenterX() < width && getCenterY() > 0 && getCenterY() < height;
    }

    public boolean hitMountains(List<Mountain> mountains) {
        return bullet.hitMountains(mountains);
    }

    public boolean hitTarget(TargetView targetView) {
        return bullet.hitTarget(targetView.getTarget());
    }
}
