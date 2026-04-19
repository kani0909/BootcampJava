package com.example.tictac.domain.service;

import com.example.tictac.datasource.repository.GameRepository;
import com.example.tictac.domain.model.Game;
import com.example.tictac.domain.model.GameBoard;
import com.example.tictac.domain.model.Position;

import java.util.Optional;
import java.util.UUID;

public class GameServiceImpl implements GameService {
   private final GameRepository gameRepository;

   public GameServiceImpl(GameRepository gameRepository) {
       this.gameRepository = gameRepository;
   }

    @Override
    public Game createNewGame() {
        Game game = Game.createNewGame();
        return gameRepository.save(game);
    }

    private Game processMinimaxMove(Game game) {
        GameBoard gameBoard = game.getBoard();
        Position bestMove = findBestMove(gameBoard);
        GameBoard newBoard = gameBoard.withMove(
                bestMove.row(),
                bestMove.col(),
                GameBoard.PLAYER_O
        );
        return gameRepository.save(game.withBoard(newBoard));
    }

    private  Position findBestMove(GameBoard gameBoard) {
        int bestScore = Integer.MIN_VALUE;
        Position bestMove = null;
        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int col = 0; col < GameBoard.SIZE; col++) {
                if (gameBoard.isCellEmpty(row,col)) {
                    GameBoard newBoard = gameBoard.withMove(row, col, GameBoard.PLAYER_O);
                    int moveScore = minimax(newBoard, 0, false);
                    if (moveScore > bestScore) {
                        bestScore = moveScore;
                        bestMove = new Position(row, col);
                    }
                }
            }
        }

        if (bestMove == null) {
            throw new IllegalStateException("No empty cells available");
        }
        return bestMove;
    }
    private int minimax(GameBoard board, int depth, boolean isMaximizing){
            if(hasWinner(board, GameBoard.PLAYER_O)) {
                return 10 - depth;
            }
            if(hasWinner(board, GameBoard.PLAYER_X)) {
                return depth - 10;
            }
            if (board.isFull()) {
                return 0;
            }
            if (isMaximizing) {
                int bestStore = Integer.MIN_VALUE;
                for (int row = 0; row < GameBoard.SIZE; row++) {
                    for (int col = 0; col < GameBoard.SIZE; col++) {
                        if (board.isCellEmpty(row, col)) {
                            GameBoard newBoard = board.withMove(row, col, GameBoard.PLAYER_O);
                            int score = minimax(newBoard, depth +1, false);
                            bestStore = Math.max(score, bestStore);
                        }
                    }
                }
                return bestStore;
            } else {
                int bestScore = Integer.MAX_VALUE;
                for (int row = 0; row < GameBoard.SIZE; row++) {
                    for (int col = 0; col < GameBoard.SIZE; col++) {
                        if (board.isCellEmpty(row, col)) {
                            GameBoard newBoard = board.withMove(row, col, GameBoard.PLAYER_X);
                            int score = minimax(newBoard, depth +1, true);
                            bestScore = Math.min(score, bestScore);
                        }
                    }
                }
                return bestScore;
            }
    }

    @Override
    public boolean validateBoard(UUID gameId, int[][] newBoard) {
       if (newBoard == null) {
           throw new IllegalArgumentException("New board cannot be null");
       }
       Game game = gameRepository.findById(gameId).orElseThrow(()-> new IllegalStateException("Game not found"));
       GameBoard currentBoard = game.getBoard();
        int changes = 0;
        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int col = 0; col < GameBoard.SIZE; col++) {
                int currentValue  = currentBoard.getCell(row, col);
                int newValue = newBoard[row][col];
                if (currentValue != newValue) {
                    if (currentValue != GameBoard.EMPTY) {
                        return false;
                    }

                    if (newValue != GameBoard.PLAYER_X) {
                        return false;
                    }

                    changes++;
                }
            }

        }

        return changes == 1 && !isGameOver(gameId);
    }

    @Override
    public Game makeMinimaxMove(UUID gameId){
        if (!gameRepository.exists(gameId)){
            throw new IllegalArgumentException("Game not found with id: " + gameId);
        }
        if (isGameOver(gameId)){
            throw new IllegalStateException("Cannot make move - game is already over");
        }
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalStateException("Game disappeared unexpectedl"));
        return processMinimaxMove(game);
    }

    @Override
    public boolean isGameOver(UUID gameId) {
       if(!gameRepository.exists(gameId)) {
         throw new IllegalArgumentException("Not a game");
       }
       Game game = gameRepository.findById(gameId)
               .orElseThrow(()-> new IllegalStateException("Game disappeared"));
        GameBoard gameBoard = game.getBoard();
        if (hasWinner(gameBoard, GameBoard.PLAYER_X) || hasWinner(gameBoard, GameBoard.PLAYER_O)) {
            return true;
        }
        return gameBoard.isFull();
    }


    private boolean hasWinner(GameBoard board, int player) {
        for (int row = 0; row < GameBoard.SIZE; row++) {
            if (board.getCell(row, 0) == player &&
                    board.getCell(row, 1) == player &&
                    board.getCell(row, 2) == player) {
                return true;
            }
        }

        for (int col = 0; col < GameBoard.SIZE; col++) {
            if (board.getCell(0, col) == player &&
                    board.getCell(1, col) == player &&
                    board.getCell(2, col) == player) {
                return true;
            }
        }

        if (board.getCell(0, 0) == player &&
                board.getCell(1, 1) == player &&
                board.getCell(2, 2) == player) {
            return true;
        }

        if (board.getCell(0, 2) == player &&
                board.getCell(1, 1) == player &&
                board.getCell(2, 0) == player) {
            return true;
        }

        return false;
    }

    @Override
    public Optional<Game> getGame(UUID gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public Game saveGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        return gameRepository.save(game);
    }
}
