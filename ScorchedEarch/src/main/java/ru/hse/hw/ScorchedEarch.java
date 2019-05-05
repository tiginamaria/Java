package ru.hse.hw;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javafx.geometry.Side.*;

public class ScorchedEarch extends Application {

    private static final double sceneWidth = 700;
    private static final double sceneHeight = 600;

    private static Pane gameRoot = new Pane();

    private List<Mountain> mountains = new ArrayList<>();
    private List<BulletView> bulletViews = new ArrayList<>();
    private TargetView targetView;
    private TankView tankView;

    private void initContent() throws FileNotFoundException {
        tankView = new TankView(0, 400);
        gameRoot.getChildren().addAll(tankView);
        createBackGround();
        createTarget(10);
    }

    private BulletView createBullet(double size) {
        var bulletView = new BulletView(tankView.getX(), tankView.getY(), size);
        gameRoot.getChildren().addAll(bulletView);
        bulletViews.add(bulletView);
        return bulletView;
    }

    private void createTarget(double size) { ;
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
                var bullet = createBullet(5);
                new Thread(new bulletBehavior(bullet)).start();
                if (targetView.isDone()) {
                    endGame();
                }
                gameRoot.getChildren().remove(bullet);
                break;
        }
    }

    private void endGame() {

    }

    private class bulletBehavior implements Runnable {

        private BulletView bulletView;

        public bulletBehavior(BulletView bulletView) {
            this.bulletView = bulletView;
        }

        @Override
        public void run() {
            System.out.println("hello");
            bulletView.fire(0.1, tankView.getBarrelAngle());
            bulletView.makeBulletMove();
            while (bulletView.onScene(sceneWidth, sceneHeight)) {
                if (bulletView.hit(mountains)) {
                    return;
                }
                if (targetView.contains(bulletView)) {
                    targetView.markDone();
                    return;
                }
            }
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
        primaryStage.setTitle("ScorchedEarth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
