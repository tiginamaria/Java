package ru.hse.hw;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * View for bullet
 */
public class BulletView extends Circle {

    /**
     * The bullet logic
     */
    private final Bullet bullet;

    /**
     * Create bullet view with given start position, angle and size
     * @param position position of bullet
     * @param size size of bullet
     * @param angle angle of fire
     */
    public BulletView(Point2D position, int size, double angle) {
        super(size);
        setCenterX(position.getX());
        setCenterY(position.getY());
        bullet = new Bullet(position.getX(), position.getY(), size, angle);
        setFill(Color.BLACK);
    }

    /**
     * Move bullet image
     */
    public void makeBulletMove() {
        bullet.move();
        setCenterX(bullet.getX());
        setCenterY(bullet.getY());
    }

    public Point2D getPosition() {
        return new Point2D(bullet.getX(), bullet.getY());
    }

    public boolean onScene(double width, double height) {
        return getCenterX() > 0 && getCenterX() < width && getCenterY() > 0 && getCenterY() < height;
    }

    /**
     * Check if bullet image hit the mountains
     * @param mountains mountain lines
     * @return true if hit, false otherwise
     */
    public boolean hitMountains(List<Mountain> mountains) {
        return bullet.hitMountains(mountains);
    }

    /**
     * Check if bullet image hit the target
     * @param targetView target to check
     * @return true if hit, false otherwise
     */
    public boolean hitTarget(TargetView targetView) {
        return bullet.hitTarget(targetView.getTarget());
    }
}
