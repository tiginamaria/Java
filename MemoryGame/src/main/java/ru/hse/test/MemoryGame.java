package ru.hse.test;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class MemoryGame extends Application {
    private static final double SCENE_SIZE = 700;
    private static double CARD_SIZE;
    private static int cardsNumber;
    private static int pairsCounter;
    private static Stage stage;
    private static Button firstCard, secondCard;
    private static Pair<Integer, Integer> firstCardPosition, secondCardPosition;
    private static MemoryGameLogic game;

    public static void main(String[] args) {
        launch(args);
    }

    private static void openDialogWindow() {
        Label label = new Label();
        label.setText("Enter field size. Size should be positive even integer.");
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(10);
        textArea.setPrefRowCount(3);
        Button startGameButton = new Button("Start game");
        startGameButton.setOnAction(event -> {
            try {
                cardsNumber = Integer.parseInt(textArea.getText());
                if (cardsNumber <= 0 || cardsNumber % 2 != 0) {
                    label.setText("Try again. Size should be positive even integer.");
                } else {
                    startGame();
                }
            } catch (NumberFormatException e) {
                label.setText("Try again. Size should be positive even integer.");
            }
        });
        FlowPane dialog = new FlowPane(Orientation.VERTICAL, 10, 10, label, textArea, startGameButton);
        dialog.setAlignment(Pos.CENTER);
        Scene scene = new Scene(dialog, 300, 250);
        stage.setScene(scene);
    }

    private static Scene createCards(int size) {
        HBox[] rows = new HBox[size];
        for (int i = 0; i < size; i++) {
            rows[i] = new HBox();
            for (int j = 0; j < size; j++) {
                Button card = createCard(i, j);
                rows[i].getChildren().add(card);
                rows[i].setSpacing(10);
                rows[i].setPadding(new Insets(10,10, 10,10));
            }
        }
        VBox cards = new VBox();
        cards.setSpacing(10);
        cards.setPadding(new Insets(10,10, 10,10));
        for (var row : rows) {
            cards.getChildren().add(row);
        }
        return new Scene(cards, SCENE_SIZE, SCENE_SIZE);
    }

    private static Button createCard(int i, int j) {
        Button card = new Button();
        card.setStyle("-fx-font-size: 2em; ");
        card.setFocusTraversable(false);
        card.setPrefSize(CARD_SIZE, CARD_SIZE);
        card.setOnMouseClicked(event -> {
            card.setDisable(true);
            card.setText(String.valueOf(game.getCard(i, j)));
            if (firstCard == null) {
                firstCard = card;
                firstCardPosition = new Pair<>(i, j);
            } else {
                secondCard = card;
                secondCardPosition = new Pair<>(i, j);
                if (game.checkCards(firstCardPosition, secondCardPosition)) {
                    onSuccess();
                } else {
                    onFail(firstCard, secondCard);
                }
                firstCard = null;
                secondCard = null;
            }
        });
        return card;
    }

    private static void onSuccess() {
        pairsCounter--;
        if (pairsCounter == 0) {
            endGame();
        }
        System.out.println("success");
        firstCard.setStyle("-fx-font-size: 2em; -fx-background-color: #6c686b");
        secondCard.setStyle("-fx-font-size: 2em; -fx-background-color: #6c686b");
        firstCard.setDisable(true);
        secondCard.setDisable(true);
    }

    private static void onFail(Button first, Button second) {
        System.out.println("fail");
        new Timeline(
                new KeyFrame(Duration.ZERO, actionEvent -> {
                    first.setDisable(true);
                    second.setDisable(true);
                }),
                new KeyFrame(Duration.seconds(1), actionEvent -> {
                    first.setText("");
                    second.setText("");
                    first.setDisable(false);
                    second.setDisable(false);
                })).play();
    }

    private static void startGame() {
        game = new MemoryGameLogic(cardsNumber);
        pairsCounter = cardsNumber * cardsNumber / 2;
        CARD_SIZE = SCENE_SIZE / cardsNumber;
        stage.setScene(createCards(cardsNumber));
    }

    private static void endGame() {
        Label label = new Label();
        label.setText("This is the end of thw game. Start new Game?");
        Button endGameButton = new Button("Start game");
        endGameButton.setOnAction(event -> openDialogWindow());
        FlowPane dialog = new FlowPane(Orientation.VERTICAL, 10, 10, label, endGameButton);
        dialog.setAlignment(Pos.CENTER);
        Scene scene = new Scene(dialog, 300, 250);
        stage.setScene(scene);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Memory Game");
        MemoryGame.stage = stage;
        stage.setMinWidth(SCENE_SIZE);
        stage.setMinHeight(SCENE_SIZE);
        stage.show();
        openDialogWindow();
    }
}

