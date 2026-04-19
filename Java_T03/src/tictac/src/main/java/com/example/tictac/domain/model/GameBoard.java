package com.example.tictac.domain.model;

import java.util.Arrays;
import java.util.Objects;

public class GameBoard {
    private final int[][] board;
    public static final int SIZE = 3;
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private GameBoard(int[][] board) {
        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, SIZE);
        }
    }

    public static GameBoard empty() {
        return new GameBoard(new int[SIZE][SIZE]);
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == EMPTY;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public GameBoard withMove(int row, int col, int player) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (!isCellEmpty(row, col)) {
            throw new IllegalArgumentException("Cell is not empty");
        }
        if (player != PLAYER_X && player != PLAYER_O) {
            throw new IllegalArgumentException("Invalid player");
        }

        int[][] newBoard = getBoard();
        newBoard[row][col] = player;
        return new GameBoard(newBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBoard gameBoard = (GameBoard) o;
        return Objects.deepEquals(board, gameBoard.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static GameBoard fromArray(int[][] boardArray) {
        return new GameBoard(boardArray);
    }
}