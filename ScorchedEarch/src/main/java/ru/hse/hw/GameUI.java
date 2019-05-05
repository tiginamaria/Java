package ru.hse.hw;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class GameUI {

    private Pane gamePane;

    public GameUI(Pane gameRoot) {
        this.gamePane = gameRoot;
    }

    public void addToPane(Node node) {
        gamePane.getChildren().add(node);
    }




}
