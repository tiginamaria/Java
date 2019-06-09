package ru.hse.hw;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * View for target.
 */
public class TargetView extends Pane {

    private final ImageView targetView = new ImageView(new Image(new FileInputStream("src/main/resources/images/target.png")));
    private final Circle targetHeart;

    /**
     * The target logic.
     */
    private final Target target;

    /**
     * Create target in given position.
     * @param x position on OX axes
     * @param y position on OY axes
     * @throws FileNotFoundException when no image of target is found
     */
    public TargetView(double x, double y) throws FileNotFoundException {
        target = new Target(x, y);
        targetHeart = new Circle(10);
        targetHeart.setFill(Color.RED);
        targetHeart.setCenterX(x);
        targetHeart.setCenterY(y);
        targetView.setFitWidth(50);
        targetView.setFitHeight(50);
        targetView.setX(x - targetView.getFitWidth() / 2);
        targetView.setY(y - targetView.getFitHeight() / 2);
        getChildren().addAll(targetHeart, targetView);
    }

    public Target getTarget() {
        return target;
    }

    /**
     * Check if target is done.
     * @return state of target
     */
    public boolean isDone() {
        return target.isDone();
    }

}
