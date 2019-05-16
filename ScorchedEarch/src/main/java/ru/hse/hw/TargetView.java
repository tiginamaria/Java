package ru.hse.hw;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class TargetView extends Pane {

    private final ImageView targetView = new ImageView(new Image(new FileInputStream("src/main/resources/images/target.png")));
    private Circle circle;
    private Target target;
    public TargetView(double x, double y, double r) throws FileNotFoundException {
        target = new Target(x, y, r);
        circle = new Circle(r);
        circle.setFill(Color.RED);
        circle.setCenterX(x);
        circle.setCenterY(y);
        targetView.setFitWidth(50);
        targetView.setFitHeight(50);
        targetView.setX(x - targetView.getFitWidth() / 2);
        targetView.setY(y - targetView.getFitHeight() / 2);
        getChildren().addAll(circle, targetView);
    }

    public boolean containsBullet(BulletView bulletView) {
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
