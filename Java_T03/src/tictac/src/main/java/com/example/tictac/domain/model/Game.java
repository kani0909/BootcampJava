package com.example.tictac.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Game {
    private final UUID id;

    private final GameBoard board;

    private Game(UUID id, GameBoard board) {
        this.id = id;
        this.board = board;
    }

    public UUID getId() {
        return id;
    }

    public GameBoard getBoard() {
        return board;
    }

    public static  Game createNewGame() {
        return new Game(UUID.randomUUID(), GameBoard.empty());
    }

    public static Game restore(UUID id, GameBoard board) {
        return new Game(id, board);
    }

    public Game withBoard(GameBoard newBoard) {
        return new Game(this.id, newBoard);
    }

    public Game withMove(int row, int col, int player) {
        GameBoard newBoard = this.board.withMove(row, col, player);
        return new Game(this.id, newBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(board, game.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", board=" + board +
                '}';
    }
}
