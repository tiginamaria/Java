package ru.hse.hw;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.geometry.Side.*;

public class ScorchedEarch extends Application {

    private Image explosionImage = new Image(new FileInputStream("src/main/resources/images/boom.png"));
    private ImageView backgroundView = new ImageView(new Image(new FileInputStream("src/main/resources/images/mountains.jpg")));
    private ImageView explosionView = new ImageView();

    private static final int sceneWidth = 700;
    private static final int sceneHeight = 600;
    private int score;

    private static Pane gameRoot = new Pane();
    private Scene scene;

    private List<Mountain> mountains = new ArrayList<>();
    private TargetView targetView;
    private TankView tankView = new TankView(0, 400);
    private int currentBulletSize = 6;

    private final Timeline boom = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(explosionView.imageProperty(), explosionImage)),
            new KeyFrame(Duration.seconds(2), new KeyValue(explosionView.imageProperty(), null)));

    public ScorchedEarch() throws FileNotFoundException {
    }


    private void getRandomTarget() {
        double x, y;
        Random random = new Random(System.currentTimeMillis());
        int n = random.nextInt(mountains.size());
        Mountain targetMountain = mountains.get(n);
        x = targetMountain.getRandomOverMountainX(random);
        y = targetMountain.getRandomOverMountainY(random, x, sceneHeight);
        try {
            targetView = new TargetView(x, y);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameRoot.getChildren().addAll(targetView);
    }

    private void initContent() {
        createBackGround();
        getRandomTarget();
        explosionView.setFitHeight(100);
        explosionView.setFitWidth(100);
        gameRoot.getChildren().addAll(tankView, explosionView);
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

            case ENTER:
                tankView.setOrientation(BOTTOM);
                var BulletBehavior = new BulletBehavior();
                Thread fire = new Thread(BulletBehavior);
                fire.start();
                break;
        }
    }

    private void endGame() {
        var textLabel = String.valueOf(score);
        var endGameLabel = new Label(textLabel);
        endGameLabel.setTextAlignment(TextAlignment.JUSTIFY);
        gameRoot.getChildren().add(endGameLabel);
    }

    private void checkResult() {
        if (targetView.isDone()) {
            score++;
            gameRoot.getChildren().remove(targetView);
            getRandomTarget();
        }
    }

    private class BulletBehavior extends Task {

        private BulletView bulletView;

        @Override
        protected Object call() throws Exception {
            bulletView = new BulletView(tankView.getBarrelPosition(), currentBulletSize, tankView.getBarrelAngle());
            Platform.runLater(() -> gameRoot.getChildren().add(bulletView));
            while(bulletView.onScene(sceneWidth, sceneHeight) && !bulletView.hitMountains(mountains) && !bulletView.hitTarget(targetView)) {
                Thread.sleep(30);
                bulletView.makeBulletMove();
            }
            explosion(bulletView.getPosition());
            Platform.runLater(() -> {
                checkResult();
                gameRoot.getChildren().remove(bulletView);
            } );
            return null;
        }

        private void explosion(Point2D position) {
            explosionView.setTranslateX(position.getX() - explosionView.getFitWidth() / 2);
            explosionView.setTranslateY(position.getY() - explosionView.getFitHeight() / 2);
            boom.play();
        }
    }

    private void createBackGround() {
        gameRoot.getChildren().add(backgroundView);
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);
        mountains.add(new Mountain(0, 400, 75, 300));
        mountains.add(new Mountain(75, 300, 150, 350));
        mountains.add(new Mountain(150, 350, 200, 250));
        mountains.add(new Mountain(200, 250, 300, 375));
        mountains.add(new Mountain(300, 375, 400, 200));
        mountains.add(new Mountain(400, 200, 450, 275));
        mountains.add(new Mountain(450, 275, 500, 175));
        mountains.add(new Mountain(500, 175, 600, 300));
        mountains.add(new Mountain(600, 300, 700, 200));

        setTimer();

        setChooseBullet();
    }

    private Timer timer;

    private void setTimer() {
        timer = new Timer();
        Label statusLabel = new Label();
        ProgressIndicator progressBar = new ProgressIndicator(0);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(timer.progressProperty());
        statusLabel.textProperty().unbind();
        statusLabel.textProperty().bind(timer.messageProperty());
        progressBar.setMinSize(70, 70);
        progressBar.setLayoutX(sceneWidth - 67);
        progressBar.setLayoutY(sceneHeight - 60);
        statusLabel.setFont(Font.font("Cambria", 15));
        statusLabel.setLayoutX(sceneWidth - 160);
        statusLabel.setLayoutY(sceneHeight - 35);
        timer.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                t -> {
                    statusLabel.textProperty().unbind();
                    statusLabel.setText("time is over");
                    timer.isDone();
                    endGame();
                });
        new Thread(timer).start();
        gameRoot.getChildren().addAll(progressBar, statusLabel);
    }

    private void setChooseBullet() {
        Label bulletLabel = new Label("Choose bullet size:");
        bulletLabel.setFont(Font.font("Cambria", 15));
        bulletLabel.setLayoutX(20);
        bulletLabel.setLayoutY(sceneHeight - 60);
        HBox bulletButtons = new HBox(3);
        bulletButtons.setFocusTraversable(false);
        bulletButtons.setTranslateX(20);
        bulletButtons.setTranslateY(sceneHeight - 35);
        Circle bulletExample = new Circle(currentBulletSize);
        bulletExample.setCenterX(130);
        bulletExample.setCenterY(sceneHeight - 20);
        for (int i = 0; i < 3; i++) {
            var bulletButton = new Button(String.valueOf(i + 1));
            var size = i;
            bulletButton.setOnMouseClicked(event -> {
                currentBulletSize = (size + 1) * 3;
                bulletExample.setRadius(currentBulletSize);
            });
            bulletButtons.getChildren().add(bulletButton);
            bulletButton.setFocusTraversable(false);
        }
        gameRoot.getChildren().addAll(bulletButtons, bulletLabel, bulletExample);
    }

    private static final int SECONDS = 60;
    private static final int MILLIS = 1000;

    public class Timer extends Task<Double> {
        @Override
        protected Double call() throws InterruptedException {
            double startTime = System.currentTimeMillis();
            double totalTime = 0.2 * SECONDS * MILLIS;
            double timeLeft = totalTime;
            while(timeLeft > 0) {
                this.updateProgress(System.currentTimeMillis() - startTime, totalTime);
                int min = (int)(timeLeft / SECONDS / MILLIS);
                int sec = (int)(timeLeft / MILLIS - min * SECONDS);
                this.updateMessage(min + " m " + sec + "s left");
                timeLeft = totalTime - (System.currentTimeMillis() - startTime);
                Thread.sleep(500);
            }
            return Math.max(0, timeLeft);
        }
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        initContent();
        scene = new Scene(gameRoot, sceneWidth, sceneHeight);
        scene.setOnKeyPressed(event -> update(event.getCode()));
        primaryStage.setTitle("ScorchedEarth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
