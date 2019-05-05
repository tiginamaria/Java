package ru.hse.hw;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet extends Pane {

    private static final double G = 9.8;

    private static final Circle circle = new Circle();

    public Bullet(double x, double y, double r) {
        circle.setRadius(r);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setFill(Color.BLACK);
        getChildren().add(circle);
    }

    public void setPosition(double x, double y, double r) {
        circle.setRadius(r);
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public void move(double time, double speed, double angle) {
        //circle.setCenterX(circle.getCenterX() + 1);
        //circle.setCenterY(circle.getCenterY() + 1);
        circle.setCenterX(circle.getCenterX() + time * speed * Math.cos(angle));
        circle.setCenterY(circle.getCenterY() + time * speed * Math.sin(angle) - G * time * time / 2);
    }
}
