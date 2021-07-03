package ru.hse.test;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameLogicTest {

    private final int SIZE = 4;

    private MemoryGameLogic gameLogic;

    @BeforeEach
    private void init() {
        gameLogic = new MemoryGameLogic(SIZE);
    }

    @Test
    void checkCardsGenerationTest() {
        var cardsCounter = new LinkedHashMap<Integer, Integer>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                var key = gameLogic.getCard(i, j);
                cardsCounter.put(key, (cardsCounter.containsKey(key)) ? cardsCounter.get(key) + 1 : 1);
            }
        }

        var cards = cardsCounter.keySet();
        for (var key : cardsCounter.keySet()) {
            assertEquals(2, (int)cardsCounter.get(key));
        }

        for (int card = 0; card < SIZE * SIZE / 2; card++) {
            assertTrue(cards.contains(card));
        }
    }

    @Test
    void checkCardTest() {
        var cardsPairs = new LinkedHashMap<Integer, List<Pair<Integer, Integer>>>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                var key = gameLogic.getCard(i, j);
                List<Pair<Integer, Integer>> cardsWithKay;
                if (cardsPairs.containsKey(key)) {
                    cardsWithKay = cardsPairs.get(key);
                } else {
                    cardsWithKay = new ArrayList<>();
                }
                cardsWithKay.add(new Pair<>(i, j));
                cardsPairs.put(key, cardsWithKay);
            }
        }

        Pair<Integer, Integer> card1;
        Pair<Integer, Integer> card2;

        for (int i = 0; i < SIZE; i++) {
            card1 = cardsPairs.get(i).get(0);
            card2 = cardsPairs.get(i).get(1);
            assertEquals(2, cardsPairs.get(i).size());
            assertTrue(gameLogic.checkCards(card1, card2));
        }

        card1 = cardsPairs.get(0).get(0);
        card2 = cardsPairs.get(1).get(0);
        assertFalse(gameLogic.checkCards(card1, card2));
    }
}