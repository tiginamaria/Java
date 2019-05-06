package ru.hse.hw;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class BulletView extends Circle {

    Bullet bullet;

    public BulletView(double x, double y, double r) {
        super(r);
        setX(x); setY(y);
        bullet = new Bullet(new Point2D(x, y));
        setFill(Color.BLACK);
    }

    public void fire(double speed, double angle) {
        bullet.setMoveParameters(speed, angle);
    }

    public void makeBulletMove() {
        bullet.move();
        setCenterX(bullet.getX());
        setCenterY(bullet.getY());
    }

    public double getX() {
        return getCenterX();
    }

    public double getY() {
        return getCenterY();
    }

    public void setX(double x) {
        setCenterX(x);
    }

    public void setY(double y) {
        setCenterY(y);
    }

    public Point2D getPosition() {
        return bullet.getPosition();
    }

    public boolean onScene(double width, double height) {
        return getX() > 0 && getX() < width && getY() > 0 && getY() < height;
    }

    public boolean hit(List<Mountain> mountains) {
        if (bullet.hit(mountains)) {
            //TODO картинка взрыва
            return true;
        }
        return false;
    }
}
