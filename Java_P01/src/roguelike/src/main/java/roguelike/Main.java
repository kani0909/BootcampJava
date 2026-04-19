package roguelike;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import roguelike.domain.model.GameData;
import roguelike.domain.generator.LevelGenerator;
import roguelike.data.GameRepository;
import roguelike.presentation.screens.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import java.util.*;

public class Main {
    private Screen screen;
    private final GameRepository repo = new GameRepository();
    private final LevelGenerator generator = new LevelGenerator();
    private GameData currentGame;
    private List<GameData.GameStatistics> allStats = new ArrayList<>();

    private boolean inMenu = true;
    private boolean inGame = false;
    private boolean inGameOver = false;
    private boolean inScoreboard = false;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private StatisticsScreen statsScreen;

    public void start() throws IOException {
        var terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);

        allStats = repo.loadStatistics("scoreboard.json");

        menuScreen = new MenuScreen();

        mainLoop();
    }

    private void mainLoop() throws IOException {
        while (true) {
            render();

            KeyStroke keyStroke = screen.readInput();

            if (keyStroke != null) {
                handleInput(keyStroke);
            }
        }
    }

    private void handleInput(KeyStroke key) {
        System.out.println("Main handling key: " + key.getKeyType() +
                (key.getKeyType() == KeyType.Character ? " char=" + key.getCharacter() : ""));

        if (inMenu) {
            int choice = menuScreen.handleInput(key);
            if (choice >= 0) {
                handleMenuChoice(choice);
            }
        } else if (inGame) {
            if (gameScreen != null) {
                boolean running = gameScreen.handleInput(key);
                if (!running) {
                    inGame = false;
                    inMenu = true;
                    currentGame = null;
                    gameScreen = null;
                } else if (currentGame != null && currentGame.gameOver) {
                    inGame = false;
                    inGameOver = true;
                    gameOverScreen = new GameOverScreen(currentGame.victory, currentGame.statistics);

                    allStats.add(currentGame.statistics);
                    repo.saveStatistics("scoreboard.json", allStats);
                }
            }
        } else if (inGameOver) {
            if (gameOverScreen != null) {
                int choice = gameOverScreen.handleInput(key);
                if (choice >= 0) {
                    handleGameOverChoice(choice);
                }
            }
        } else if (inScoreboard) {
            if (statsScreen != null && statsScreen.handleInput(key)) {
                inScoreboard = false;
                inMenu = true;
            }
        }
    }

    private void render() throws IOException {
        TextGraphics graphics = screen.newTextGraphics();
        TerminalSize size = screen.getTerminalSize();

        if (inMenu) {
            menuScreen.render(graphics, size);
        } else if (inGame) {
            if (gameScreen != null) {
                gameScreen.render(graphics, size);
            }
        } else if (inGameOver) {
            if (gameOverScreen != null) {
                gameOverScreen.render(graphics, size);
            }
        } else if (inScoreboard) {
            if (statsScreen != null) {
                statsScreen.render(graphics, size);
            }
        }

        screen.refresh();
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 0: // New Game
                currentGame = generator.generateNewGame();
                gameScreen = new GameScreen(currentGame, repo);
                inMenu = false;
                inGame = true;
                System.out.println("New game started");
                break;

            case 1: // Load Game
                currentGame = repo.load("save.json");
                if (currentGame == null) {
                    currentGame = generator.generateNewGame();
                }
                gameScreen = new GameScreen(currentGame, repo);
                inMenu = false;
                inGame = true;
                System.out.println("Game loaded");
                break;

            case 2: // Scoreboard
                statsScreen = new StatisticsScreen(allStats);
                inMenu = false;
                inScoreboard = true;
                break;

            case 3: // Exit
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
        }
    }

    private void handleGameOverChoice(int choice) {
        switch (choice) {
            case 0: // New Game
                currentGame = generator.generateNewGame();
                gameScreen = new GameScreen(currentGame, repo);
                inGameOver = false;
                inGame = true;
                break;

            case 1: // Statistics
                statsScreen = new StatisticsScreen(allStats);
                inGameOver = false;
                inScoreboard = true;
                break;

            case 2: // Main Menu
                currentGame = null;
                gameScreen = null;
                inGameOver = false;
                inMenu = true;
                break;
        }
    }

    public static void main(String[] args) {
        try {
            new Main().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

