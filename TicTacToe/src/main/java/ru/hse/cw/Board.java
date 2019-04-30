package ru.hse.cw;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Board {
    private List<List<Integer>> board = new ArrayList<>();
    private boolean currentTurn;

    public Board() {
        for (int i = 0; i < 3; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                row.add(0);
            }
            board.add(row);
        }
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.get(i).get(j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRawsCols() {
        int rowScore;
        int colScore;
        for (int i = 0; i < 3; i++) {
            rowScore = 0;
            colScore = 0;
            for (int j = 0; j < 3; j++) {
                rowScore += board.get(i).get(j);
                colScore += board.get(j).get(i);
            }
            if (abs(rowScore) == 3 || abs(colScore) == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiags() {
        int mainDiagScore = 0;
        int antiDiagScore = 0;
        for (int i = 0; i < 3; i++) {
            mainDiagScore += board.get(i).get(i);
            antiDiagScore += board.get(i).get(2 - i);
        }
        if (abs(mainDiagScore) == 3 || abs(antiDiagScore) == 3) {
            return true;
        }
        return false;
    }

    public GameStatus getStatus() {
        if (checkRawsCols() || checkDiags()) {
            return currentTurn ? GameStatus.X_WIN : GameStatus.O_WIN;
        }

        if (checkDraw()) {
            return GameStatus.DRAW;
        }

        return GameStatus.CONTINUE;
    }

    public String makeMove(int i, int j) {
        currentTurn = !currentTurn;
        if (board.get(i).get(j) == 0) {
            if (currentTurn) {
                var newRow = board.get(i);
                newRow.set(j, -1);
                board.set(i, newRow);
                return "X";
            } else {
                var newRow = board.get(i);
                newRow.set(j, 1);
                board.set(i, newRow);
                return "O";
            }
        } else {
            if (board.get(i).get(j) == 1) {
                return "X";
            } else {
                return "O";
            }
        }
    }
}
