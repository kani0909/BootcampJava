package com.example.tictac.web.mapper;

import com.example.tictac.domain.model.Game;
import com.example.tictac.domain.model.GameBoard;
import com.example.tictac.web.model.GameWeb;

import java.util.UUID;

public class GameWebMapper {
    public static GameWeb toWeb(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        return GameWeb.fromDomain(game);
    }

    public static Game toDomain(GameWeb gameWeb) {
        if (gameWeb == null) {
            throw new IllegalArgumentException("GameWeb cannot be null");
        }
        UUID id = gameWeb.getId();
        int[][] board = gameWeb.getBoard();
        GameBoard gameBoard = GameBoard.fromArray(board);
        Game game = Game.restore(id, gameBoard);
        return  game;

    }

}
