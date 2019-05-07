package ru.hse.hw;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Side.*;

public class ScorchedEarch extends Application {

    private Image explosionImage = new Image(new FileInputStream("src/main/resources/images/boom.png"));
    private ImageView explosionView = new ImageView();

    private static final double sceneWidth = 700;
    private static final double sceneHeight = 600;

    private static Pane gameRoot = new Pane();

    private List<Mountain> mountains = new ArrayList<>();
    private TargetView targetView;
    private TankView tankView;
    private double currentBulletSize;
    private Circle currentBulletView;

    private final Timeline boom = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(explosionView.imageProperty(), explosionImage)),
            new KeyFrame(Duration.seconds(2), new KeyValue(explosionView.imageProperty(), null)));

    public ScorchedEarch() throws FileNotFoundException {
    }

    private void initContent() throws FileNotFoundException {
        tankView = new TankView(0, 400);
        createBackGround();
        createTarget(10);
        explosionView.setFitHeight(100);
        explosionView.setFitWidth(100);
        gameRoot.getChildren().addAll(tankView, explosionView);
    }

    private void createTarget(double size) {
        targetView = new TargetView(20, 20, size);
        gameRoot.getChildren().addAll(targetView);
    }

    private void update(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                tankView.makeBarrelMove(TOP);
                break;

            case DOWN:
                tankView.makeBarrelMove(BOTTOM);
                break;

            case LEFT:
                tankView.setOrientation(LEFT);
                tankView.makeTankMove(mountains, LEFT);
                break;

            case RIGHT:
                tankView.setOrientation(RIGHT);
                tankView.makeTankMove(mountains,RIGHT);
                break;

            case SPACE:
                tankView.setOrientation(BOTTOM);
                var bulletBehavior = new bulletBehavior();
                Thread fire = new Thread(bulletBehavior);
                fire.start();
                endGame();
                break;
        }
    }

    private void endGame() {
        if (targetView.isDone()) {
        }
    }

    private class bulletBehavior extends Task {

        private BulletView bulletView;

        public bulletBehavior() {
            bulletView = new BulletView(tankView.getBarrelPosition(), tankView.getBarrelAngle(), 5, 10);
            Platform.runLater(() -> gameRoot.getChildren().add(bulletView));
        }

        @Override
        protected Object call() throws Exception {
            while(bulletView.onScene(sceneWidth, sceneHeight) && !bulletView.hit(mountains) && !targetView.isDone()) {
                if (targetView.contains(bulletView.getPosition())) {
                    targetView.markDone();
                }
                Thread.sleep(30);
                bulletView.makeBulletMove();
            }
            explosion(bulletView.getPosition());
            Platform.runLater(() -> gameRoot.getChildren().remove(bulletView));
            return null;
        }

        private void explosion(Point2D position) {
            explosionView.setTranslateX(position.getX() - explosionView.getFitWidth() / 2);
            explosionView.setTranslateY(position.getY() - explosionView.getFitHeight() / 2);
            boom.play();
        }
    }

    private void createBackGround() {
        mountains.add(new Mountain(0, 400, 75, 300));
        gameRoot.getChildren().addAll(new Line(0, 400, 75, 300));
        mountains.add(new Mountain(75, 300, 150, 350));
        gameRoot.getChildren().addAll(new Line(75, 300, 150, 350));
        mountains.add(new Mountain(150, 350, 200, 250));
        gameRoot.getChildren().addAll(new Line(150, 350, 200, 250));
        mountains.add(new Mountain(200, 250, 300, 375));
        gameRoot.getChildren().addAll(new Line(200, 250, 300, 375));
        mountains.add(new Mountain(300, 375, 400, 200));
        gameRoot.getChildren().addAll(new Line(300, 375, 400, 200));
        mountains.add(new Mountain(400, 200, 450, 275));
        gameRoot.getChildren().addAll(new Line(400, 200, 450, 275));
        mountains.add(new Mountain(450, 275, 500, 175));
        gameRoot.getChildren().addAll(new Line(450, 275, 500, 175));
        mountains.add(new Mountain(500, 175, 600, 300));
        gameRoot.getChildren().addAll(new Line(500, 175, 600, 300));
        mountains.add(new Mountain(600, 300, 700, 200));
        gameRoot.getChildren().addAll(new Line(600, 300, 700, 200));
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        initContent();
        Scene scene = new Scene(gameRoot, sceneWidth, sceneHeight);
        scene.setOnKeyPressed(event -> update(event.getCode()));

        currentBulletView = new Circle(3);
        currentBulletView.setCenterX(sceneWidth - 30);
        currentBulletView.setCenterY(sceneHeight - 200);
        gameRoot.getChildren().add(currentBulletView);

        VBox bulletButtons = new VBox(5);
        bulletButtons.setTranslateX(sceneWidth - 30);
        bulletButtons.setTranslateY(sceneHeight - 180);
        for (int i = 0; i < 5; i++) {
            var bulletButton = new Button(String.valueOf(i));
            var size = i;
            bulletButton.setOnAction(event -> {
                currentBulletSize = (size + 1) * 4;
                currentBulletView.setRadius(currentBulletSize);
            });
            bulletButtons.getChildren().add(bulletButton);
        }
        gameRoot.getChildren().addAll(bulletButtons);
        primaryStage.setTitle("ScorchedEarth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
