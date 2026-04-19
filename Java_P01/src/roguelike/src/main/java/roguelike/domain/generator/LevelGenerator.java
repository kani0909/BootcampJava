package roguelike.domain.generator;

import roguelike.domain.model.GameData;
import java.util.*;

public class LevelGenerator {
    private static final Random rand = new Random();

    private static final int ROOMS_IN_WIDTH = 3;
    private static final int ROOMS_IN_HEIGHT = 3;
    private static final int ROOMS_NUM = 9;
    private static final int REGION_WIDTH = 27;
    private static final int REGION_HEIGHT = 10;
    private static final int MIN_ROOM_WIDTH = 6;
    private static final int MAX_ROOM_WIDTH = 25;
    private static final int MIN_ROOM_HEIGHT = 5;
    private static final int MAX_ROOM_HEIGHT = 8;

    private static final int MAX_MONSTERS_PER_ROOM = 2;
    private static final int MAX_ITEMS_PER_ROOM = 3;
    private static final int LEVEL_UPDATE_DIFFICULTY = 10;
    //private static final int PERCENTS_UPDATE = 2;
    private static final int MAX_LEVEL = 21;

    public GameData generateNewGame() {
        GameData data = new GameData();
        data.player = new GameData.Player();
        data.player.baseStats = new GameData.CharacterStats();
        data.player.baseStats.health = 500;
        data.player.baseStats.strength = 100;
        data.player.baseStats.agility = 100;
        data.player.maxHp = 500;
        data.player.backpack = new GameData.Backpack();
        data.player.elixirBuffs = new GameData.Buffs();

        data.level = new GameData.Level();
        data.level.levelNum = 0;
        data.statistics.level = 0;

        generateLevel(data);

        return data;
    }

    public void generateNextLevel(GameData data) {
        if (data.level.levelNum >= MAX_LEVEL - 1) {
            data.victory = true;
            data.gameOver = true;
            return;
        }

        GameData.Player oldPlayer = data.player;
        data.player = new GameData.Player();
        data.player.baseStats.health = oldPlayer.baseStats.health;
        data.player.baseStats.strength = oldPlayer.baseStats.strength;
        data.player.baseStats.agility = oldPlayer.baseStats.agility;
        data.player.maxHp = oldPlayer.maxHp;
        data.player.backpack = oldPlayer.backpack;
        data.player.weapon = oldPlayer.weapon;
        data.player.elixirBuffs = oldPlayer.elixirBuffs;

        data.level.levelNum++;
        data.statistics.level = data.level.levelNum;
        generateLevel(data);
    }

    private void generateLevel(GameData data) {
        generateRooms(data.level.rooms);
        generatePassages(data.level);
        int playerRoom = placePlayer(data);
        generateMonsters(data, playerRoom);
        generateItems(data, playerRoom);
        generateExit(data, playerRoom);
    }

    private void generateRooms(GameData.Room[] rooms) {
        for (int i = 0; i < ROOMS_NUM; i++) {
            int width = MIN_ROOM_WIDTH + rand.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1);
            int height = MIN_ROOM_HEIGHT + rand.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1);

            int gridX = i % ROOMS_IN_WIDTH;
            int gridY = i / ROOMS_IN_WIDTH;

            int minX = gridX * REGION_WIDTH + 1;
            int maxX = (gridX + 1) * REGION_WIDTH - width - 1;
            int minY = gridY * REGION_HEIGHT + 1;
            int maxY = (gridY + 1) * REGION_HEIGHT - height - 1;

            rooms[i].coords.x = minX + rand.nextInt(maxX - minX + 1);
            rooms[i].coords.y = minY + rand.nextInt(maxY - minY + 1);
            rooms[i].coords.width = width;
            rooms[i].coords.height = height;

            System.out.println("Room " + i + ": x=" + rooms[i].coords.x +
                    ", y=" + rooms[i].coords.y +
                    ", w=" + width + ", h=" + height);
        }
    }

    private void generatePassages(GameData.Level level) {
        int[] parent = new int[ROOMS_NUM];
        int[] rank = new int[ROOMS_NUM];
        for (int i = 0; i < ROOMS_NUM; i++) parent[i] = i;

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < ROOMS_NUM; i++) {
            if (i % 3 != 2) edges.add(new int[]{i, i + 1});
            if (i < 6) edges.add(new int[]{i, i + 3});
        }
        Collections.shuffle(edges);

        for (int[] edge : edges) {
            int root1 = find(parent, edge[0]);
            int root2 = find(parent, edge[1]);
            if (root1 != root2) {
                parent[root1] = root2;
                createPassage(level, edge[0], edge[1]);
            }
        }
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private void createPassage(GameData.Level level, int r1, int r2) {
        GameData.Room room1 = level.rooms[r1];
        GameData.Room room2 = level.rooms[r2];

        int x1 = room1.coords.x + room1.coords.width / 2;
        int y1 = room1.coords.y + room1.coords.height / 2;
        int x2 = room2.coords.x + room2.coords.width / 2;
        int y2 = room2.coords.y + room2.coords.height / 2;

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            GameData.Coords passage = new GameData.Coords();
            passage.x = x;
            passage.y = y1;
            passage.width = 1;
            passage.height = 1;
            level.passages.passages.add(passage);
        }

        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            GameData.Coords passage = new GameData.Coords();
            passage.x = x2;
            passage.y = y;
            passage.width = 1;
            passage.height = 1;
            level.passages.passages.add(passage);
        }
    }

    private int placePlayer(GameData data) {
        int roomNum = rand.nextInt(ROOMS_NUM);
        GameData.Room room = data.level.rooms[roomNum];
        data.player.baseStats.coords.x = room.coords.x + 1 + rand.nextInt(room.coords.width - 2);
        data.player.baseStats.coords.y = room.coords.y + 1 + rand.nextInt(room.coords.height - 2);
        return roomNum;
    }

    private void generateMonsters(GameData data, int playerRoom) {
        int difficulty = data.level.levelNum / 2;

        for (int i = 0; i < ROOMS_NUM; i++) {
            if (i == playerRoom) continue;

            GameData.Room room = data.level.rooms[i];
            int count = rand.nextInt(MAX_MONSTERS_PER_ROOM + 1);

            for (int m = 0; m < count; m++) {
                GameData.Monster monster = new GameData.Monster();
                monster.type = rand.nextInt(5);

                switch (monster.type) {
                    case 0: // ZOMBIE
                        monster.hostility = 1; // средняя враждебность
                        monster.baseStats.agility = 20; //низкая ловкость;
                        monster.baseStats.strength = 40; // средняя сила
                        monster.baseStats.health = 60; //высокое здоровье.
                        break;
                    case 1: // VAMPIRE
                        monster.hostility = 2; // высокая враждебность
                        monster.baseStats.agility = 50; // высокая ловкость,
                        monster.baseStats.strength = 40; // средняя сила
                        monster.baseStats.health = 60; // высокое здоровье
                        monster.firstStrikeUsed = false;
                        break;
                    case 2: // GHOST
                        monster.hostility = 0; // низкая враждебность
                        monster.baseStats.agility = 50; // высокая ловкость
                        monster.baseStats.strength = 25; // низкая сила
                        monster.baseStats.health = 35; // низкое здоровье
                        break;
                    case 3: // OGRE
                        monster.hostility = 1; // средняя враждебность.
                        monster.baseStats.agility = 15; // низкая ловкость
                        monster.baseStats.strength = 50; // Очень высокая сила
                        monster.baseStats.health = 100; // Очень высокое здоровье
                        break;
                    case 4: // SNAKE
                        monster.hostility = 2; // высокая враждебность
                        monster.baseStats.agility = 60; // очень высокая ловкость
                        monster.baseStats.strength = 20;
                        monster.baseStats.health = 30;
                        break;
                }

                monster.baseStats.agility += monster.baseStats.agility * difficulty / 100;
                monster.baseStats.strength += monster.baseStats.strength * difficulty / 100;
                monster.baseStats.health += monster.baseStats.health * difficulty / 100;

                int attempts = 0;
                boolean placed = false;

                while (!placed && attempts < 100) {
                    int x = room.coords.x + 1 + rand.nextInt(room.coords.width - 2);
                    int y = room.coords.y + 1 + rand.nextInt(room.coords.height - 2);

                        monster.baseStats.coords.x = x;
                        monster.baseStats.coords.y = y;

                    if (monster.baseStats.coords.x > room.coords.x &&
                            monster.baseStats.coords.x < room.coords.x + room.coords.width - 1 &&
                            monster.baseStats.coords.y > room.coords.y &&
                            monster.baseStats.coords.y < room.coords.y + room.coords.height - 1) {

                        if (isPositionFree(room, monster.baseStats.coords)) {
                            placed = true;
                        }
                    }
                    attempts++;
                }
                if (placed) {
                    room.monsters.add(monster);
                }
            }
        }
    }

    private void generateItems(GameData data, int playerRoom) {
        int itemCount = MAX_ITEMS_PER_ROOM - data.level.levelNum / LEVEL_UPDATE_DIFFICULTY;
        if (itemCount < 0) itemCount = 0;

        String[][] names = {
                {"Ration", "Berries", "Bread", "Apple", "Mushrooms"},
                {"Health Elixir", "Agility Elixir", "Strength Elixir"},
                {"Health Scroll", "Agility Scroll", "Strength Scroll"},
                {"Sword", "Axe", "Dagger", "Spear", "Mace"}
        };

        for (int i = 0; i < ROOMS_NUM; i++) {
            if (i == playerRoom) continue;

            GameData.Room room = data.level.rooms[i];
            int count = rand.nextInt(itemCount + 1);

            for (int c = 0; c < count; c++) {
                int type = rand.nextInt(4);
                GameData.Coords pos = new GameData.Coords();
                int attempts = 0;
                boolean placed = false;


                while (!placed && attempts < 100) {
                    pos.x = room.coords.x + 1 + rand.nextInt(room.coords.width - 2);
                    pos.y = room.coords.y + 1 + rand.nextInt(room.coords.height - 2);

                    if (pos.x > room.coords.x &&
                            pos.x < room.coords.x + room.coords.width - 1 &&
                            pos.y > room.coords.y &&
                            pos.y < room.coords.y + room.coords.height - 1) {

                        if (isPositionFree(room, pos)) {
                            placed = true;
                        }
                    }
                    attempts++;
                }

                if (!placed) continue;

                switch (type) {
                    case 0: // Food
                        GameData.RoomFood food = new GameData.RoomFood();
                        food.food = new GameData.Food();
                        food.food.regen = 10 + rand.nextInt(40);
                        food.food.name = names[0][rand.nextInt(5)];
                        food.geometry = pos;
                        room.consumables.foods.add(food);
                        break;

                    case 1: // Elixir
                        GameData.RoomElixir elixir = new GameData.RoomElixir();
                        elixir.elixir = new GameData.Elixir();
                        elixir.elixir.type = rand.nextInt(3);
                        elixir.elixir.increase = 5 + rand.nextInt(15);
                        elixir.elixir.duration = 30 + rand.nextInt(30);
                        elixir.elixir.name = names[1][rand.nextInt(3)];
                        elixir.geometry = pos;
                        room.consumables.elixirs.add(elixir);
                        break;

                    case 2: // Scroll
                        GameData.RoomScroll scroll = new GameData.RoomScroll();
                        scroll.scroll = new GameData.Scroll();
                        scroll.scroll.type = rand.nextInt(3);
                        scroll.scroll.increase = 3 + rand.nextInt(10);
                        scroll.scroll.name = names[2][rand.nextInt(3)];
                        scroll.geometry = pos;
                        room.consumables.scrolls.add(scroll);
                        break;

                    case 3: // Weapon
                        GameData.RoomWeapon weapon = new GameData.RoomWeapon();
                        weapon.weapon = new GameData.Weapon();
                        weapon.weapon.strength = 20 + rand.nextInt(30);
                        weapon.weapon.name = names[3][rand.nextInt(5)];
                        weapon.geometry = pos;
                        room.consumables.weapons.add(weapon);
                        break;
                }
            }
        }
    }

    private void generateExit(GameData data, int playerRoom) {
        int exitRoom;
        do {
            exitRoom = rand.nextInt(ROOMS_NUM);
        } while (exitRoom == playerRoom);

        GameData.Room room = data.level.rooms[exitRoom];

        int attempts = 0;
        boolean placed = false;

        while (!placed && attempts < 100) {
            data.level.exit.x = room.coords.x + 1 + rand.nextInt(room.coords.width - 2);
            data.level.exit.y = room.coords.y + 1 + rand.nextInt(room.coords.height - 2);

            if (data.level.exit.x > room.coords.x &&
                    data.level.exit.x < room.coords.x + room.coords.width - 1 &&
                    data.level.exit.y > room.coords.y &&
                    data.level.exit.y < room.coords.y + room.coords.height - 1) {

                if (isPositionFree(room, data.level.exit)) {
                    placed = true;
                }
            }
            attempts++;
        }

        data.level.exit.width = 1;
        data.level.exit.height = 1;
    }

    private boolean isPositionFree(GameData.Room room, GameData.Coords pos) {
        if (pos.x <= room.coords.x ||
                pos.x >= room.coords.x + room.coords.width - 1 ||
                pos.y <= room.coords.y ||
                pos.y >= room.coords.y + room.coords.height - 1) {
            return false;
        }

        for (GameData.Monster m : room.monsters) {
            if (m.baseStats.coords.equals(pos)) return false;
        }
        for (GameData.RoomFood f : room.consumables.foods) {
            if (f.geometry.equals(pos)) return false;
        }
        for (GameData.RoomElixir e : room.consumables.elixirs) {
            if (e.geometry.equals(pos)) return false;
        }
        for (GameData.RoomScroll s : room.consumables.scrolls) {
            if (s.geometry.equals(pos)) return false;
        }
        for (GameData.RoomWeapon w : room.consumables.weapons) {
            if (w.geometry.equals(pos)) return false;
        }
        return true;
    }
}
