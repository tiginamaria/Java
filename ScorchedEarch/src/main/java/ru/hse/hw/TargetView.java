package ru.hse.hw;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TargetView extends Pane {
    private Circle circle;
    private Target target;
    public TargetView(double x, double y, double r) {
        target = new Target(x, y, r);
        circle = new Circle(r);
        circle.setFill(Color.RED);
        circle.setCenterX(x);
        circle.setCenterY(y);
        getChildren().addAll(circle);
    }

    public boolean contains(BulletView bulletView) {
        return target.contains(bulletView.getPosition());
    }

    public void markDone() {
        System.out.println("done");
        target.markDone();
    }

    public boolean isDone() {
        return target.isDone();
    }

}
