package ru.hse.hw;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target extends Pane {

    private Circle circle;

    public Target(double x, double y, double r) {
        circle = new Circle(r);
        circle.setFill(Color.RED);
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public boolean contains(double x, double y) {
        return circle.contains(x, y);
    }
}
