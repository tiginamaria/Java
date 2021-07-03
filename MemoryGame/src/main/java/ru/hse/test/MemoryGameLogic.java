package ru.hse.test;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryGameLogic {

    private final int[][] cards;
    private final int size;

    public MemoryGameLogic(int size) {
        this.size = size;
        cards = new int[size][size];
        generateCards();
    }

    private void generateCards() {
        var randomCards = new ArrayList<Integer>();
        for (int i = 0; i < size * size / 2; i++) {
            randomCards.add(i);
            randomCards.add(i);
        }
        Collections.shuffle(randomCards);
        var pos = 0;
        for (var randomCard : randomCards) {
            cards[pos / size][pos % size] = randomCard;
            pos++;
        }
    }

    public boolean checkCards(Pair<Integer, Integer> firstCard, Pair<Integer, Integer> secondCard) {
        return cards[firstCard.getKey()][firstCard.getValue()] == cards[secondCard.getKey()][secondCard.getValue()];
    }

    public int getCard(int i, int j) {
        return cards[i][j];
    }
}

