package com.example.tictac.web.controller;

import com.example.tictac.domain.model.Game;
import com.example.tictac.domain.service.GameService;
import com.example.tictac.web.mapper.GameWebMapper;
import com.example.tictac.web.model.GameWeb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameWeb> createGame() {
        Game game = gameService.createNewGame();
        GameWeb response = GameWebMapper.toWeb(game);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameWeb> getGame(@PathVariable UUID gameId) {
        return gameService.getGame(gameId)
                .map(GameWebMapper::toWeb)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
    @PostMapping("/{gameId}")
    public ResponseEntity<?> makeMove(
            @PathVariable UUID gameId,
            @RequestBody GameWeb gameWeb
            ) {
        if(!gameId.equals(gameWeb.getId())) {
            throw new IllegalArgumentException(
                    String.format("Game ID mismatch: path=%s, body=%s", gameId, gameWeb.getId())
            );
        }

        if (!gameService.validateBoard(gameId, gameWeb.getBoard())) {
            throw new IllegalArgumentException("Invalid move");
        }

        Game game = GameWebMapper.toDomain(gameWeb);
        gameService.saveGame(game);

        if (gameService.isGameOver(gameId)) {
            Game currentGame = gameService.getGame(gameId)
                    .orElseThrow(() -> new IllegalStateException("Game not found after player move"));
            return ResponseEntity.ok(GameWebMapper.toWeb(currentGame));
        }

        Game updatedGame = gameService.makeMinimaxMove(gameId);
        return ResponseEntity.ok(GameWebMapper.toWeb(updatedGame));
    }


}
