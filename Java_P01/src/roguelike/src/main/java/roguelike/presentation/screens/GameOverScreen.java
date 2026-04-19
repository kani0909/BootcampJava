package roguelike.presentation.screens;

import roguelike.domain.model.GameData;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class GameOverScreen {
    private final boolean victory;
    private final GameData.GameStatistics stats;
    private int selected = 0;
    private final String[] options;

    public GameOverScreen(boolean victory, GameData.GameStatistics stats) {
        this.victory = victory;
        this.stats = stats;
        this.options = victory ?
                new String[]{"NEW GAME", "STATISTICS", "MAIN MENU"} :
                new String[]{"NEW GAME", "STATISTICS", "MAIN MENU"};
    }

    public void render(TextGraphics g, TerminalSize size) {
        g.setBackgroundColor(TextColor.ANSI.BLACK);
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        // Заголовок
        String title = victory ? "VICTORY!" : "GAME OVER";
        g.setForegroundColor(victory ?
                TextColor.ANSI.GREEN_BRIGHT : TextColor.ANSI.RED_BRIGHT);
        g.putString((size.getColumns() - title.length()) / 2, 5, title);

        int y = 10;
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.putString((size.getColumns() - 20) / 2, y, "FINAL STATS:");

        String[][] stats_data = {
                {"Level Reached:", String.valueOf(stats.level + 1)},
                {"Gold Collected:", String.valueOf(stats.treasures)},
                {"Enemies Killed:", String.valueOf(stats.enemiesKilled)},
                {"Food Eaten:", String.valueOf(stats.foodEaten)},
                {"Potions Drunk:", String.valueOf(stats.potionsDrunk)},
                {"Scrolls Read:", String.valueOf(stats.scrollsRead)},
                {"Hits Dealt:", String.valueOf(stats.hitsDealt)},
                {"Hits Taken:", String.valueOf(stats.hitsTaken)},
                {"Steps Made:", String.valueOf(stats.moves)}
        };

        for (int i = 0; i < stats_data.length; i++) {
            g.putString((size.getColumns() - 40) / 2, y + 2 + i,
                    stats_data[i][0] + " " + stats_data[i][1]);
        }
        int menuY = y + 2 + stats_data.length + 2;
        for (int i = 0; i < options.length; i++) {
            String text = (i == selected ? "> " : "  ") + options[i];
            g.setForegroundColor(i == selected ?
                    TextColor.ANSI.YELLOW : TextColor.ANSI.WHITE);
            g.putString((size.getColumns() - text.length()) / 2,
                    menuY + i * 2, text);
        }
    }

    public int handleInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            selected = (selected - 1 + options.length) % options.length;
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            selected = (selected + 1) % options.length;
        } else if (key.getKeyType() == KeyType.Enter) {
            return selected;
        }
        return -1;
    }
}