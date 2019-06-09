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
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.geometry.Side.*;

/**
 *  Game ScorchedEarch:
 *  There is an image of the tank, and target. You have only one minute to kill as many targets as you can.
 *  Press the keys:
 *      left-right - to make the gun move left-right and climb up the mountains if necessary
 *      up-down    - to make the barrel of the tank goes left-right
 *      Enter      - to make tank
 */
public class ScorchedEarch extends Application {

    private final Image explosionImage = new Image(new FileInputStream("src/main/resources/images/boom.png"));
    private final ImageView backgroundView = new ImageView(new Image(new FileInputStream("src/main/resources/images/mountains.jpg")));
    private final ImageView explosionView = new ImageView();


    /**
     * Sizes of scene
     */
    private static final int sceneWidth = 700;
    private static final int sceneHeight = 600;

    /**
     * Current number of killed targets
     */
    private int score;

    /**
     * Game panel
     */
    private final static Pane gameRoot = new Pane();

    /**
     * List of mountains
     */
    private final List<Mountain> mountains = new ArrayList<>();

    /**
     * View of target
     */
    private TargetView targetView;

    /**
     * View of tank
     */
    private final TankView tankView = new TankView(0, 400);

    /**
     * Current bullet id (0 - SMALL, 1 - MIDDLE, 2 - BIG)
     */
    private int currentBulletId = 2;

    /**
     * Flag to freeze the screen
     */
    private boolean gameOver;

    /**
     * Process of bullet explosion
     */
    private final Timeline boom = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(explosionView.imageProperty(), explosionImage)),
            new KeyFrame(Duration.seconds(2), new KeyValue(explosionView.imageProperty(), null)));

    public ScorchedEarch() throws FileNotFoundException { }

    /**
     * Set target on a random position under the mountains
     */
    private void getRandomTarget() {
        var random = new Random(System.currentTimeMillis());
        int n = random.nextInt(mountains.size());
        var targetMountain = mountains.get(n);
        var x = targetMountain.getRandomOverMountainX(random);
        var y = targetMountain.getRandomOverMountainY(random, x, sceneHeight);
        try {
            targetView = new TargetView(x, y);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameRoot.getChildren().addAll(targetView);
    }

    /**
     * Create environment of the game
     */
    private void initContent() {
        createBackGround();
        getRandomTarget();
        explosionView.setFitHeight(100);
        explosionView.setFitWidth(100);
        gameRoot.getChildren().addAll(tankView, explosionView);
    }

    /**
     * Translate pressed keys to action according to game rules
     * @param keyCode pressed key on keyboard
     */
    private void update(@NotNull KeyCode keyCode) {
        if (gameOver) {
            return;
        }
        switch (keyCode) {
            case UP:
                tankView.makeBarrelMove(RIGHT);
                break;

            case DOWN:
                tankView.makeBarrelMove(LEFT);
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
                var bulletBehavior = new BulletBehavior();
                var fire = new Thread(bulletBehavior);
                fire.start();
                break;
        }
    }

    /**
     * Stop game and show the game result
     */
    private void endGame() {
        gameOver = true;
        var textLabel = "END GAME\nYour score is " + score;
        var endGameLabel = new Label(textLabel);
        endGameLabel.setFont(Font.font("Cambria", 30));
        endGameLabel.setLayoutX(sceneWidth / 3.0);
        endGameLabel.setLayoutY(sceneHeight / 5.0);
        endGameLabel.setTextAlignment(TextAlignment.CENTER);
        gameRoot.getChildren().add(endGameLabel);
    }

    /**
     * Check if the target is done and set new one
     */
    private void checkResult() {
        if (targetView.isDone()) {
            score++;
            gameRoot.getChildren().remove(targetView);
            getRandomTarget();
        }
    }

    /**
     * Implements bullet fly
     */
    private class BulletBehavior extends Task {

        /**
         * Bullet to make fire
         */
        private final BulletView bulletView;

        /**
         * Create new bullet in tank barrel end position
         */
        public BulletBehavior() {
            bulletView = new BulletView(tankView.getBarrelPosition(), currentBulletId, tankView.getBarrelAngle());
        }

        /**
         * While bullet is on the screen, don't hit mountains or target, move bullet. After remove bullet and check the result
         * @return always null
         * @throws InterruptedException when fly process was interrupted
         */
        @Override
        protected Object call() throws InterruptedException {
            Platform.runLater(() -> gameRoot.getChildren().add(bulletView));
            while (bulletView.onScene(sceneWidth, sceneHeight) && !bulletView.hitMountains(mountains) && !bulletView.hitTarget(targetView)) {
                Thread.sleep(30);
                bulletView.makeBulletMove();
            }
            explosion(bulletView.getPosition());
            Platform.runLater(() -> {
                checkResult();
                gameRoot.getChildren().remove(bulletView);
            });
            return null;
        }

        /**
         * Show the bullet explosion after hit
         * @param position position of bullet when it hit something
         */
        private void explosion(@NotNull Point2D position) {
            if (bulletView.isExploded()) {
                explosionView.setTranslateX(position.getX() - explosionView.getFitWidth() / 2);
                explosionView.setTranslateY(position.getY() - explosionView.getFitHeight() / 2);
                boom.play();
            }
        }
    }

    /**
     * Set the background picture, create mountain lines, make to set additional information
     */
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

    /**
     * Set one minute timer for the game
     */
    private void setTimer() {
        var timer = new Timer();
        var statusLabel = new Label();
        var progressBar = new ProgressIndicator(0);
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

    /**
     * Values to translate time
     */
    private static final int SECONDS = 60;
    private static final int MILLIS = 1000;

    /**
     * Count how much time left since the game have started
     */
    private class Timer extends Task<Double> {
        @Override
        protected Double call() throws InterruptedException {
            var startTime = System.currentTimeMillis();
            var totalTime = 2.0 * SECONDS * MILLIS;
            var timeLeft = totalTime;
            while (timeLeft > 0) {
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

    /**
     * Set three buttons to choose bullet size
     */
    private void setChooseBullet() {
        var bulletLabel = new Label("Choose bullet size:");
        bulletLabel.setFont(Font.font("Cambria", 15));
        bulletLabel.setLayoutX(20);
        bulletLabel.setLayoutY(sceneHeight - 60);
        var bulletButtons = new HBox(3);
        bulletButtons.setFocusTraversable(false);
        bulletButtons.setTranslateX(20);
        bulletButtons.setTranslateY(sceneHeight - 35);
        var bulletExample = new Circle(Bullet.getSizeById(currentBulletId));
        bulletExample.setCenterX(130);
        bulletExample.setCenterY(sceneHeight - 20);
        for (int i = 0; i < 3; i++) {
            var bulletButton = new Button(String.valueOf(i + 1));
            var id = i;
            bulletButton.setOnMouseClicked(event -> {
                currentBulletId = id;
                bulletExample.setRadius(Bullet.getSizeById(currentBulletId));
            });
            bulletButtons.getChildren().add(bulletButton);
            bulletButton.setFocusTraversable(false);
        }
        gameRoot.getChildren().addAll(bulletButtons, bulletLabel, bulletExample);
    }


    /**
     * Start the game
     * @param primaryStage stage of the game
     */
    @Override
    public void start(Stage primaryStage) {
        initContent();
        primaryStage.setResizable(false);
        var scene = new Scene(gameRoot, sceneWidth, sceneHeight);
        scene.setOnKeyPressed(event -> update(event.getCode()));
        primaryStage.setTitle("ScorchedEarth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
