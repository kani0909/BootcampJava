package com.example.tictac.domain.model;

public record Position(int row, int col) {
    public Position {
        if (row < 0 || row >= GameBoard.SIZE || col < 0 || col >= GameBoard.SIZE) {
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
