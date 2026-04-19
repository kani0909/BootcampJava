package roguelike.presentation.screens;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class MenuScreen {
    private int selected = 0;
    private final String[] items = {"NEW GAME", "LOAD GAME", "SCOREBOARD", "EXIT"};

    public void render(TextGraphics g, TerminalSize size) {
        g.setBackgroundColor(TextColor.ANSI.BLACK);
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        String[] title = {
                "  _____   ____    _____  _    _ ______ _____  ",
                " |  __ \\ / __ \\  / ____|| |  | |  ____|  __ \\ ",
                " | |__) | |  | || |  __ | |  | | |__  | |__) |",
                " |  _  /| |  | || | |_ || |  | |  __| |  _  / ",
                " | | \\ \\| |__| || |__| || |__| | |____| | \\ \\ ",
                " |_|  \\_\\\\____/  \\_____| \\____/|______|_|  \\_\\"
        };

        g.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        for (int i = 0; i < title.length; i++) {
            g.putString((size.getColumns() - title[i].length()) / 2,
                    5 + i, title[i]);
        }

        for (int i = 0; i < items.length; i++) {
            String text = (i == selected ? "> " : "  ") + items[i];
            g.setForegroundColor(i == selected ?
                    TextColor.ANSI.YELLOW : TextColor.ANSI.WHITE);
            g.putString((size.getColumns() - text.length()) / 2,
                    15 + i * 2, text);
        }

        g.setForegroundColor(TextColor.ANSI.CYAN);
        String hint = "Use arrow keys and Enter to select";
        g.putString((size.getColumns() - hint.length()) / 2,
                size.getRows() - 3, hint);
    }

    public int handleInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            selected = (selected - 1 + items.length) % items.length;
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            selected = (selected + 1) % items.length;
        } else if (key.getKeyType() == KeyType.Enter) {
            return selected;
        }else if (key.getKeyType() == KeyType.Escape) {
            return 3;
        }
        return -1;
    }
}