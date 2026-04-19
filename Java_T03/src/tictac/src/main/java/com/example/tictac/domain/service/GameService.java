package com.example.tictac.domain.service;

import com.example.tictac.domain.model.Game;

import java.util.Optional;
import java.util.UUID;

public interface GameService {
    Game createNewGame();
    Game makeMinimaxMove(UUID gameId);
    boolean validateBoard(UUID gameId, int[][] newBoard);
    boolean isGameOver(UUID gameId);
    Optional<Game> getGame(UUID gameId);
    Game saveGame(Game game);
}
