package ru.hse.hw;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class BulletView extends Pane {

    Bullet bullet;

    private static final Circle circle = new Circle();

    public BulletView(double x, double y, double r) {
        bullet = new Bullet( new Point2D(x, y));
        circle.setRadius(r);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setFill(Color.BLACK);
        getChildren().add(circle);
    }

    public void fire(double speed, double angle) {
        bullet.setMoveParameters(speed, angle);
    }

    public void makeBulletMove() {
        bullet.move();
        circle.setCenterX(bullet.getX());
        circle.setCenterY(bullet.getY());
    }

    public Point2D getPosition() {
        return bullet.getPosition();
    }

    public boolean onScene(double width, double height) {
        return getTranslateX() > 0 && getTranslateX() < width && getTranslateY() > 0 && getTranslateY() < height;
    }

    public boolean hit(List<Mountain> mountains) {
        if (bullet.hit(mountains)) {
            //TODO картинка взрыва
            return true;
        }
        return false;
    }
}
