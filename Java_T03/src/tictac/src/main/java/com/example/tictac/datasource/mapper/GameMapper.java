package com.example.tictac.datasource.mapper;

import com.example.tictac.datasource.model.GameEntity;
import com.example.tictac.domain.model.Game;
import com.example.tictac.domain.model.GameBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class GameMapper {
   public static  GameEntity toEntity(Game game) {
       if (game == null) {
           throw new IllegalArgumentException("Game cannot be null");
       }
       GameEntity gameEntity = new GameEntity();
       gameEntity.setId(game.getId());
       UUID id = game.getId();
       GameBoard newBoard = game.getBoard();
       int[][] board = newBoard.getBoard();
       gameEntity.setBoard(newBoard.getBoard());

       return gameEntity;
   }

   public static Game toDomain(GameEntity entity) {
       if (entity == null) {
           throw new IllegalArgumentException("Entity cannot be null");
       }
       UUID id = entity.getId();
       int[][] boardArray = entity.getBoard();
       GameBoard board = GameBoard.fromArray(boardArray);
       Game game = Game.restore(id, board);
       return game;
   }
}
