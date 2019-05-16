package ru.hse.hw;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TargetView extends Pane {

    private final ImageView targetView = new ImageView(new Image(new FileInputStream("src/main/resources/images/target.png")));
    private Circle circle;
    private Target target;
    public TargetView(double x, double y) throws FileNotFoundException {
        target = new Target(x, y, 10, 25);
        circle = new Circle(10);
        circle.setFill(Color.RED);
        circle.setCenterX(x);
        circle.setCenterY(y);
        targetView.setFitWidth(50);
        targetView.setFitHeight(50);
        targetView.setX(x - targetView.getFitWidth() / 2);
        targetView.setY(y - targetView.getFitHeight() / 2);
        getChildren().addAll(circle, targetView);
    }

    public Target getTarget() {
        return target;
    }

    public boolean isDone() {
        return target.isDone();
    }
}
