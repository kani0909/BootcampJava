package com.example.tictac.datasource.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class GameEntity {
    private UUID id;
    private int[][] board;

    public GameEntity() {
    }

    public GameEntity(UUID id, int[][] board) {
        this.id = id;
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public UUID getId() {
        return id;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return Objects.equals(id, that.id) && Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.deepHashCode(board));
    }

}
