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
    void getCardTest() {
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

        var card1 = cardsPairs.get(0).get(0);
        var card2 = cardsPairs.get(0).get(1);
        assertEquals(gameLogic.getCard(card1.getKey(), card1.getKey()), gameLogic.getCard(card2.getKey(), card2.getKey()));

        card1 = cardsPairs.get(0).get(0);
        card2 = cardsPairs.get(1).get(0);
        assertNotEquals(gameLogic.getCard(card1.getKey(), card1.getKey()), gameLogic.getCard(card2.getKey(), card2.getKey()));
    }


}