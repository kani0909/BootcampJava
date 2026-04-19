package roguelike.presentation.screens;

import roguelike.data.GameRepository;
import roguelike.domain.generator.LevelGenerator;
import roguelike.domain.model.GameData;
import roguelike.domain.service.*;
import roguelike.presentation.util.FogOfWar;
import roguelike.presentation.util.SymbolMapper;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.ArrayList;
import java.util.List;

public class GameScreen {
    private final GameData data;
    private final MovementService movement;
    private final CombatService combat;
    private final ItemService items;
    private final LevelGenerator generator;
    private final FogOfWar fog;
    private final SymbolMapper symbols;
    private final GameData.GameStatistics stats;
    private final GameRepository repo;

    private boolean inventoryMode = false;
    private int selectedType = 0;
    private int selectedIndex = 0;

    private static final int UI_HEIGHT = 10;
    private static final int GAME_AREA_START_Y = 0;

    public GameScreen(GameData data, GameRepository repo) {
        this.data = data;
        this.repo = repo;
        this.movement = new MovementService();
        this.combat = new CombatService();
        this.items = new ItemService();
        this.generator = new LevelGenerator();
        this.fog = new FogOfWar(80, 40);
        this.symbols = new SymbolMapper();
        this.stats = data.statistics;
    }

    public void render(TextGraphics g, TerminalSize size) {
        g.setBackgroundColor(TextColor.ANSI.BLACK);
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        if (inventoryMode) {
            renderInventory(g, size);
        } else {
            renderGame(g, size);
        }
    }

    private void renderGame(TextGraphics g, TerminalSize size) {
        fog.update(data);
        int playerX = data.player.baseStats.coords.x;
        int playerY = data.player.baseStats.coords.y;
        int offsetX = playerX - size.getColumns() / 2;
        int offsetY = playerY - (size.getRows() - UI_HEIGHT) / 2;
        offsetX = Math.max(0, Math.min(offsetX, 80 - size.getColumns()));
        offsetY = Math.max(0, Math.min(offsetY, 40 - (size.getRows() - UI_HEIGHT)));

        g.fillRectangle(new TerminalPosition(0, GAME_AREA_START_Y),
                new TerminalSize(size.getColumns(), size.getRows() - UI_HEIGHT), ' ');

        for (GameData.Room room : data.level.rooms) {
            if (room != null && room.coords != null) {
                if (room.coords.x + room.coords.width > offsetX &&
                        room.coords.x < offsetX + size.getColumns() &&
                        room.coords.y + room.coords.height > offsetY &&
                        room.coords.y < offsetY + size.getRows() - UI_HEIGHT) {

                    if (fog.isVisible(room.coords.x, room.coords.y)) {
                        renderVisibleRoomWithOffset(g, room, -offsetX, -offsetY);
                    } else if (fog.isExplored(room.coords.x, room.coords.y)) {
                        renderExploredRoomWallsOnly(g, room, -offsetX, -offsetY);
                    }
                }
            }
        }

        if (data.level.passages != null && data.level.passages.passages != null) {
            for (GameData.Coords p : data.level.passages.passages) {
                if (p != null) {
                    int screenX = p.x - offsetX;
                    int screenY = p.y - offsetY;
                    if (screenX >= 0 && screenX < size.getColumns() &&
                            screenY >= 0 && screenY < size.getRows() - UI_HEIGHT) {

                        boolean isInRoomInterior = false;  
                        boolean isOnRoomWall = false;

                        for (GameData.Room room : data.level.rooms) {
                            if (room != null && room.coords != null) {
                                if (p.x >= room.coords.x && p.x < room.coords.x + room.coords.width &&
                                        p.y >= room.coords.y && p.y < room.coords.y + room.coords.height) {

                                    if (p.x == room.coords.x ||
                                            p.x == room.coords.x + room.coords.width - 1 ||
                                            p.y == room.coords.y ||
                                            p.y == room.coords.y + room.coords.height - 1) {
                                        isOnRoomWall = true;
                                    } else {
                                        isInRoomInterior = true;
                                    }
                                }
                            }
                        }

                        if (fog.isVisible(p.x, p.y)) {
                            char symbol;
                            if (isOnRoomWall) {
                                g.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
                                symbol = '#';
                            } else if (isInRoomInterior) {
                                g.setForegroundColor(TextColor.ANSI.WHITE);
                                symbol = '.';
                            } else {
                                g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                                symbol = '#';
                            }
                            g.setCharacter(screenX, screenY, symbol);

                        } else if (fog.isExplored(p.x, p.y)) {
                            g.setForegroundColor(new TextColor.RGB(100, 100, 100));
                            char symbol;
                            if (isOnRoomWall) {
                                symbol = '#';
                            } else if (isInRoomInterior) {
                                symbol = '.';
                            } else {
                                symbol = '#';
                            }
                            g.setCharacter(screenX, screenY, symbol);
                        }
                    }
                }
            }
        }


        for (GameData.Room room : data.level.rooms) {
            if (room != null) {
                for (GameData.Monster m : room.monsters) {
                    if (m != null && m.baseStats != null && m.baseStats.coords != null) {
                        int mx = m.baseStats.coords.x;
                        int my = m.baseStats.coords.y;

                        int screenX = mx - offsetX;
                        int screenY = my - offsetY;

                        if (screenX >= 0 && screenX < size.getColumns() &&
                                screenY >= 0 && screenY < size.getRows() - UI_HEIGHT) {

                            if (fog.isMonsterVisible(data, m, playerX, playerY)) {
                                g.setForegroundColor(symbols.monsterColor(m.type));
                                g.setCharacter(screenX, screenY, symbols.monsterChar(m.type));
                            }
                        }
                    }
                }
            }
        }
        for (GameData.Room room : data.level.rooms) {
            if (room == null || room.consumables == null) continue;

            for (GameData.RoomFood f : room.consumables.foods) {
                if (f != null && f.geometry != null) {
                    if (fog.isVisible(f.geometry.x, f.geometry.y)) {
                        int screenX = f.geometry.x - offsetX;
                        int screenY = f.geometry.y - offsetY;
                        if (screenX >= 0 && screenX < size.getColumns() &&
                                screenY >= 0 && screenY < size.getRows() - UI_HEIGHT &&
                                fog.isVisible(f.geometry.x, f.geometry.y)) {
                            g.setForegroundColor(TextColor.ANSI.GREEN);
                            g.setCharacter(screenX, screenY, '%');
                        }
                    }
                }
            }

            for (GameData.RoomElixir e : room.consumables.elixirs) {
                if (e != null && e.geometry != null) {
                    int screenX = e.geometry.x - offsetX;
                    int screenY = e.geometry.y - offsetY;
                    if (screenX >= 0 && screenX < size.getColumns() &&
                            screenY >= 0 && screenY < size.getRows() - UI_HEIGHT &&
                            fog.isVisible(e.geometry.x, e.geometry.y)) {
                        g.setForegroundColor(TextColor.ANSI.BLUE);
                        g.setCharacter(screenX, screenY, '!');
                    }
                }
            }

            for (GameData.RoomScroll s : room.consumables.scrolls) {
                if (s != null && s.geometry != null) {
                    int screenX = s.geometry.x - offsetX;
                    int screenY = s.geometry.y - offsetY;
                    if (screenX >= 0 && screenX < size.getColumns() &&
                            screenY >= 0 && screenY < size.getRows() - UI_HEIGHT &&
                            fog.isVisible(s.geometry.x, s.geometry.y)) {
                        g.setForegroundColor(TextColor.ANSI.MAGENTA);
                        g.setCharacter(screenX, screenY, '?');
                    }
                }
            }

            for (GameData.RoomWeapon w : room.consumables.weapons) {
                if (w != null && w.geometry != null) {
                    int screenX = w.geometry.x - offsetX;
                    int screenY = w.geometry.y - offsetY;
                    if (screenX >= 0 && screenX < size.getColumns() &&
                            screenY >= 0 && screenY < size.getRows() - UI_HEIGHT &&
                            fog.isVisible(w.geometry.x, w.geometry.y)) {
                        g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                        g.setCharacter(screenX, screenY, ')');
                    }
                }
            }
        }
        int screenPlayerX = playerX - offsetX;
        int screenPlayerY = playerY - offsetY;
        if (screenPlayerX >= 0 && screenPlayerX < size.getColumns() &&
                screenPlayerY >= 0 && screenPlayerY < size.getRows() - UI_HEIGHT) {
            g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            g.setCharacter(screenPlayerX, screenPlayerY, '@');
        }

        if (data.level != null && data.level.exit != null) {
            int screenExitX = data.level.exit.x - offsetX;
            int screenExitY = data.level.exit.y - offsetY;
            if (screenExitX >= 0 && screenExitX < size.getColumns() &&
                    screenExitY >= 0 && screenExitY < size.getRows() - UI_HEIGHT &&
                    fog.isVisible(data.level.exit.x, data.level.exit.y)) {
                g.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                g.setCharacter(screenExitX, screenExitY, '>');
            }
        }

        g.setForegroundColor(TextColor.ANSI.WHITE);
        for (int x = 0; x < size.getColumns(); x++) {
            g.setCharacter(x, size.getRows() - UI_HEIGHT - 1, '-');
        }

        renderUI(g, size);

    }

    private void renderVisibleRoomWithOffset(TextGraphics g, GameData.Room room, int offsetX, int offsetY) {
        g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        for (int x = room.coords.x; x < room.coords.x + room.coords.width; x++) {
            int screenX = x + offsetX;
            int screenYTop = room.coords.y + offsetY;
            int screenYBottom = room.coords.y + room.coords.height - 1 + offsetY;

            if (screenX >= 0 && screenX < 80) {
                if (screenYTop >= 0 && screenYTop < 40) {
                    g.setCharacter(screenX, screenYTop, '#');
                }
                if (screenYBottom >= 0 && screenYBottom < 40) {
                    g.setCharacter(screenX, screenYBottom, '#');
                }
            }
        }

        for (int y = room.coords.y; y < room.coords.y + room.coords.height; y++) {
            int screenY = y + offsetY;
            int screenXLeft = room.coords.x + offsetX;
            int screenXRight = room.coords.x + room.coords.width - 1 + offsetX;

            if (screenY >= 0 && screenY < 40) {
                if (screenXLeft >= 0 && screenXLeft < 80) {
                    g.setCharacter(screenXLeft, screenY, '#');
                }
                if (screenXRight >= 0 && screenXRight < 80) {
                    g.setCharacter(screenXRight, screenY, '#');
                }
            }
        }

        g.setForegroundColor(new TextColor.RGB(150, 150, 150));
        for (int x = room.coords.x + 1; x < room.coords.x + room.coords.width - 1; x++) {
            for (int y = room.coords.y + 1; y < room.coords.y + room.coords.height - 1; y++) {
                int screenX = x + offsetX;
                int screenY = y + offsetY;
                if (screenX >= 0 && screenX < 80 && screenY >= 0 && screenY < 40) {
                    g.setCharacter(screenX, screenY, '.');
                }
            }
        }
    }

    private void renderExploredRoomWallsOnly(TextGraphics g, GameData.Room room, int offsetX, int offsetY) {
        g.setForegroundColor(new TextColor.RGB(80, 80, 80));

        for (int x = room.coords.x; x < room.coords.x + room.coords.width; x++) {
            int screenX = x + offsetX;
            int screenYTop = room.coords.y + offsetY;
            int screenYBottom = room.coords.y + room.coords.height - 1 + offsetY;

            if (screenX >= 0 && screenX < 80) {
                if (screenYTop >= 0 && screenYTop < 40) {
                    g.setCharacter(screenX, screenYTop, '#');
                }
                if (screenYBottom >= 0 && screenYBottom < 40) {
                    g.setCharacter(screenX, screenYBottom, '#');
                }
            }
        }
        for (int y = room.coords.y; y < room.coords.y + room.coords.height; y++) {
            int screenY = y + offsetY;
            int screenXLeft = room.coords.x + offsetX;
            int screenXRight = room.coords.x + room.coords.width - 1 + offsetX;

            if (screenY >= 0 && screenY < 40) {
                if (screenXLeft >= 0 && screenXLeft < 80) {
                    g.setCharacter(screenXLeft, screenY, '#');
                }
                if (screenXRight >= 0 && screenXRight < 80) {
                    g.setCharacter(screenXRight, screenY, '#');
                }
            }
        }
    }

    private void renderUI(TextGraphics g, TerminalSize size) {
        int uiY = size.getRows() - UI_HEIGHT;
        GameData.Player p = data.player;

        g.setBackgroundColor(TextColor.ANSI.BLACK);
        g.fillRectangle(new TerminalPosition(0, uiY),
                new TerminalSize(size.getColumns(), UI_HEIGHT), ' ');
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.putString(2, uiY + 1, String.format("HP: %.0f/%d", p.baseStats.health, p.maxHp));
        g.putString(2, uiY + 2, String.format("STR: %d +%d", p.baseStats.strength, p.weapon.strength));
        g.putString(2, uiY + 3, String.format("AGL: %d", p.baseStats.agility));
        g.putString(2, uiY + 4, String.format("GOLD: %d", p.backpack.treasures));
        g.putString(2, uiY + 5, String.format("LVL: %d/21", data.level.levelNum + 1));

        g.putString(20, uiY + 1, "INVENTORY:");
        g.putString(20, uiY + 2, String.format("F:%d/9 E:%d/9 S:%d/9 W:%d/9",
                p.backpack.foods.size(), p.backpack.elixirs.size(),
                p.backpack.scrolls.size(), p.backpack.weapons.size()));

        g.putString(40, uiY + 1, "CONTROLS:");
        g.putString(40, uiY + 2, "WASD/Arrows - move");
        g.putString(40, uiY + 3, "H - weapons  J - food");
        g.putString(40, uiY + 4, "K - elixirs  E - scrolls");
        g.putString(40, uiY + 5, "I - inventory  Q - quit");

        if (!data.messages.isEmpty()) {
            String lastMsg = data.messages.get(data.messages.size() - 1);
            g.setForegroundColor(TextColor.ANSI.YELLOW);
            g.putString(60, uiY + 5, lastMsg.substring(0, Math.min(20, lastMsg.length())));
        }
    }


    private void renderInventory(TextGraphics g, TerminalSize size) {
        g.setBackgroundColor(new TextColor.RGB(20, 20, 20));
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        int x = size.getColumns() / 2 - 20;
        int y = size.getRows() / 2 - 10;

        g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        g.drawRectangle(new TerminalPosition(x, y), new TerminalSize(40, 20), '*');

        String title = "INVENTORY";
        g.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        g.putString(x + 15, y, title);

        String[] types = {"FOOD", "ELIXIR", "SCROLL", "WEAPON"};
        for (int i = 0; i < 4; i++) {
            if (i == selectedType) {
                g.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                g.putString(x + 5, y + 2 + i, "> " + types[i]);
            } else {
                g.setForegroundColor(TextColor.ANSI.WHITE);
                g.putString(x + 7, y + 2 + i, types[i]);
            }
        }

        g.setForegroundColor(TextColor.ANSI.CYAN);
        g.putString(x + 20, y + 2, "ITEMS:");

        List<?> items = getCurrentItems();
        for (int i = 0; i < Math.min(8, items.size()); i++) {
            String itemStr = getItemString(items, i);
            if (i == selectedIndex) {
                g.setForegroundColor(TextColor.ANSI.YELLOW);
                g.putString(x + 20, y + 4 + i, "> " + itemStr);
            } else {
                g.setForegroundColor(TextColor.ANSI.WHITE);
                g.putString(x + 22, y + 4 + i, itemStr);
            }
        }
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.putString(x + 5, y + 17, "Enter - use, ESC - back");
        if (selectedType == 3) { // Weapon
            g.putString(x + 5, y + 18, "0 - unequip");
        }
    }

    private List<?> getCurrentItems() {
        switch (selectedType) {
            case 0:
                return data.player.backpack.foods;
            case 1:
                return data.player.backpack.elixirs;
            case 2:
                return data.player.backpack.scrolls;
            case 3:
                return data.player.backpack.weapons;
            default:
                return new ArrayList<>();
        }
    }

    private String getItemString(List<?> items, int i) {
        if (items.get(i) instanceof GameData.Food) {
            GameData.Food f = (GameData.Food) items.get(i);
            return f.name + " (+" + f.regen + " HP)";
        } else if (items.get(i) instanceof GameData.Elixir) {
            GameData.Elixir e = (GameData.Elixir) items.get(i);
            String[] types = {"Health", "Agility", "Strength"};
            return e.name + " (+" + e.increase + " " + types[e.type] +
                    " for " + e.duration + "s)";
        } else if (items.get(i) instanceof GameData.Scroll) {
            GameData.Scroll s = (GameData.Scroll) items.get(i);
            String[] types = {"Health", "Agility", "Strength"};
            return s.name + " (+" + s.increase + " " + types[s.type] + ")";
        } else if (items.get(i) instanceof GameData.Weapon) {
            GameData.Weapon w = (GameData.Weapon) items.get(i);
            return w.name + " (+" + w.strength + " dmg)";
        }
        return "";
    }

    public boolean handleInput(KeyStroke key) {
        if (key == null) return true;
        System.out.println("GameScreen handling key: " + key.getKeyType() +
                (key.getKeyType() == KeyType.Character ? " char=" + key.getCharacter() : ""));

        if (inventoryMode) {
            return handleInventoryInput(key);
        } else {
            return handleGameInput(key);
        }
    }

    private boolean handleGameInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Character) {
            char c = key.getCharacter();
            System.out.println("GameScreen game input char: '" + c + "' (int: " + (int) c + ")");

            if (c == 'w' || c == 'W') {
                System.out.println("W pressed - moving up");
                processTurn(0, -1);
                return true;
            } else if (c == 's' || c == 'S') {
                System.out.println("S pressed - moving down");
                processTurn(0, 1);
                return true;
            } else if (c == 'a' || c == 'A') {
                System.out.println("A pressed - moving left");
                processTurn(-1, 0);
                return true;
            } else if (c == 'd' || c == 'D') {
                System.out.println("D pressed - moving right");
                processTurn(1, 0);
                return true;
            } else if (c == 'h' || c == 'H') {
                selectedType = 3;
                inventoryMode = true;
                return true;
            } else if (c == 'j' || c == 'J') {
                selectedType = 0;
                inventoryMode = true;
                return true;
            } else if (c == 'k' || c == 'K') {
                selectedType = 1;
                inventoryMode = true;
                return true;
            } else if (c == 'e' || c == 'E') {
                selectedType = 2;
                inventoryMode = true;
                return true;
            } else if (c == 'i' || c == 'I') {
                selectedType = 0;
                inventoryMode = true;
                return true;
            } else if (c == 'q' || c == 'Q') {
                repo.save("save.json", data);
                return false;
            }
        } else if (key.getKeyType() == KeyType.ArrowUp) {
            processTurn(0, -1);
            return true;
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            processTurn(0, 1);
            return true;
        } else if (key.getKeyType() == KeyType.ArrowLeft) {
            processTurn(-1, 0);
            return true;
        } else if (key.getKeyType() == KeyType.ArrowRight) {
            processTurn(1, 0);
            return true;
        }

        return true;
    }

    private void processTurn(int dx, int dy) {
        System.out.println("Processing turn: dx=" + dx + ", dy=" + dy);
        for (GameData.BattleInfo battle : data.battles) {
            if (battle != null && battle.isFight && battle.playerAsleep) {
                battle.playerAsleep = false;
                movement.moveAllMonsters(data);
                combat.updateFights(data);
                data.messages.add("You wake up!");
                System.out.println("Player wakes up");
                return;
            }
        }
        movement.movePlayer(data, dx, dy, stats);
        items.checkPickup(data, stats);

        combat.updateFights(data);

        for (GameData.BattleInfo battle : data.battles) {
            if (battle != null && battle.isFight) {
                if (combat.processPlayerAttack(data, battle, stats)) {
                    break;
                }
            }
        }

        movement.moveAllMonsters(data);

        for (GameData.BattleInfo battle : data.battles) {
            if (battle != null && battle.isFight && battle.enemy != null &&
                    battle.enemy.baseStats != null && battle.enemy.baseStats.health > 0) {
                combat.processMonsterAttack(data, battle, stats);
            }
        }
        items.checkBuffExpiration(data.player);
        if (data.level != null && data.level.exit != null &&
                data.player.baseStats.coords.equals(data.level.exit)) {
            repo.save("save.json", data);
            System.out.println("Game saved before level transition");
            generator.generateNextLevel(data);
            fog.reset();
            repo.save("save.json", data);
            System.out.println("Game saved after level generation");
            data.messages.add("Entering level " + (data.level.levelNum + 1));
        }

        System.out.println("Turn processed. Player now at: (" +
                data.player.baseStats.coords.x + "," +
                data.player.baseStats.coords.y + ")");
    }

    private boolean handleInventoryInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Escape) {
            inventoryMode = false;
            selectedIndex = 0;
            return true;
        }

        if (key.getKeyType() == KeyType.ArrowUp) {
            selectedIndex = Math.max(0, selectedIndex - 1);
            return true;
        }

        if (key.getKeyType() == KeyType.ArrowDown) {
            List<?> items = getCurrentItems();
            selectedIndex = Math.min(items.size() - 1, selectedIndex + 1);
            return true;
        }

        if (key.getKeyType() == KeyType.ArrowLeft) {
            selectedType = (selectedType - 1 + 4) % 4;
            selectedIndex = 0;
            return true;
        }

        if (key.getKeyType() == KeyType.ArrowRight) {
            selectedType = (selectedType + 1) % 4;
            selectedIndex = 0;
            return true;
        }

        if (key.getKeyType() == KeyType.Enter) {
            useSelectedItem();
            inventoryMode = false;
            selectedIndex = 0;
            return true;
        }

        if (key.getKeyType() == KeyType.Character) {
            char c = key.getCharacter();
            if (c >= '1' && c <= '9') {
                int num = c - '1';
                List<?> items = getCurrentItems();
                if (num < items.size()) {
                    selectedIndex = num;
                    useSelectedItem();
                }
                inventoryMode = false;
                return true;
            } else if (c == '0' && selectedType == 3) {
                items.equipWeapon(data.player, -1, data);
                inventoryMode = false;
                return true;
            }
        }

        return true;
    }

    private void useSelectedItem() {
        if (selectedIndex >= 0) {
            switch (selectedType) {
                case 0: // Food
                    if (selectedIndex < data.player.backpack.foods.size()) {
                        items.useFood(data.player, selectedIndex, stats);
                    }
                    break;
                case 1: // Elixir
                    if (selectedIndex < data.player.backpack.elixirs.size()) {
                        items.useElixir(data.player, selectedIndex, stats);
                    }
                    break;
                case 2: // Scroll
                    if (selectedIndex < data.player.backpack.scrolls.size()) {
                        items.useScroll(data.player, selectedIndex, stats);
                    }
                    break;
                case 3: // Weapon
                    if (selectedIndex < data.player.backpack.weapons.size()) {
                        items.equipWeapon(data.player, selectedIndex, data);
                    }
                    break;
            }
        }
    }

}