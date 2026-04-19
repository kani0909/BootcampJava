package roguelike.presentation.screens;

import roguelike.domain.model.GameData;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;

public class StatisticsScreen {
    private List<GameData.GameStatistics> stats;

    public StatisticsScreen(List<GameData.GameStatistics> stats) {
        this.stats = stats;
        stats.sort((a, b) -> b.treasures - a.treasures);
    }

    public void render(TextGraphics g, TerminalSize size) {
        g.setBackgroundColor(TextColor.ANSI.BLACK);
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        String title = "STATISTICS";
        g.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        g.putString((size.getColumns() - title.length()) / 2, 3, title);

        if (stats.isEmpty()) {
            g.setForegroundColor(TextColor.ANSI.WHITE);
            String msg = "No games played yet";
            g.putString((size.getColumns() - msg.length()) / 2,
                    size.getRows() / 2, msg);
        } else {
            int startX = 10;
            int startY = 6;

            g.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
            g.putString(startX, startY,     "LVL");
            g.putString(startX + 6, startY, "GOLD");
            g.putString(startX + 13, startY, "KILLS");
            g.putString(startX + 20, startY, "FOOD");
            g.putString(startX + 27, startY, "POT");
            g.putString(startX + 32, startY, "SCR");
            g.putString(startX + 38, startY, "HITS");
            g.putString(startX + 44, startY, "MISS");
            g.putString(startX + 50, startY, "MOVES");

            for (int i = 0; i < Math.min(15, stats.size()); i++) {
                GameData.GameStatistics s = stats.get(i);
                g.setForegroundColor(i == 0 ? TextColor.ANSI.GREEN_BRIGHT :
                        TextColor.ANSI.WHITE);
                g.putString(startX, startY + 2 + i,
                        String.format("%3d %6d %6d %5d %5d %5d %5d %5d %6d",
                                s.level + 1, s.treasures, s.enemiesKilled, s.foodEaten,
                                s.potionsDrunk, s.scrollsRead, s.hitsDealt,
                                s.hitsTaken, s.moves));
            }
        }

        g.setForegroundColor(TextColor.ANSI.CYAN);
        String hint = "Press ESC to return to menu";
        g.putString((size.getColumns() - hint.length()) / 2,
                size.getRows() - 3, hint);
    }

    public boolean handleInput(KeyStroke key) {
        return key.getKeyType() == KeyType.Escape;
    }
}