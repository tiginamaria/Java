package ru.hse.hw;

import javafx.animation.AnimationTimer;
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

    private HashMap<KeyCode,Boolean> keys = new HashMap<>(); //нажатые клавиши
    private static Pane appRoot = new Pane(); //корневой узел
    private static Pane gameRoot = new Pane(); //для всех элементов игры

    private Game game;

    private GameUI gameUI;

    private void initContent() throws FileNotFoundException {
        gameUI = new GameUI(gameRoot);
        game = new Game(gameUI);
        createBackGround();
        appRoot.getChildren().addAll(gameRoot);
    }

    private void update() {
        if (isPressed(KeyCode.UP)) {
            game.moveBarrelToSide(TOP);
        }
        if (isPressed(KeyCode.DOWN)) {
            game.moveBarrelToSide(BOTTOM);
        }
        if (isPressed(KeyCode.LEFT)) {
            game.moveTankToSide(LEFT);
        }
        if(isPressed(KeyCode.RIGHT)) {
            game.moveTankToSide(RIGHT);
        }
        if(isPressed(KeyCode.SPACE)) {
            //game.makeFire();
        }
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key,false);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        initContent();
        Scene scene = new Scene(appRoot, 700, 600);
        scene.setOnKeyPressed(event-> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
        });
        primaryStage.setTitle("ScorchedEarth");
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameTimer.start();
    }

    private void createBackGround() {
        List<Mountain> mountains = new ArrayList<>();
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
        game.addMountains(mountains);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
