package com.example.tictac.web.model;

import com.example.tictac.domain.model.Game;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class GameWeb {
    private final UUID id;
    private final int[][] board;

    private GameWeb(UUID id,  int[][] board) {
        this.id = id;
        this.board = board;
    }

    public static GameWeb fromDomain(Game game) {
        return new GameWeb(game.getId()
        , game.getBoard().getBoard());
    }


    public UUID getId() {
        return id;
    }

    public int[][] getBoard() {
        return board;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameWeb gameWeb = (GameWeb) o;
        return Objects.equals(id, gameWeb.id) && Objects.deepEquals(board, gameWeb.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.deepHashCode(board));
    }

    @Override
    public String toString() {
        return "GameWeb{" +
                "id=" + id +
                ", board=" + Arrays.toString(board) +
                '}';
    }
}
