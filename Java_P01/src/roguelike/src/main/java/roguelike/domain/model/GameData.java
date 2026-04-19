package roguelike.domain.model;

import java.util.*;

public class GameData {
    public Player player = new Player();
    public Level level = new Level();
    public BattleInfo[] battles = new BattleInfo[8];
    public GameStatistics statistics = new GameStatistics();
    public boolean gameOver = false;
    public boolean victory = false;
    public List<String> messages = new ArrayList<>();

    public GameData() {
        for (int i = 0; i < 8; i++) battles[i] = new BattleInfo();
    }

    public static class Player {
        public CharacterStats baseStats = new CharacterStats();
        public int maxHp;
        public Backpack backpack = new Backpack();
        public Weapon weapon = new Weapon();
        public Buffs elixirBuffs = new Buffs();
    }

    public static class CharacterStats {
        public double health;
        public int strength, agility;
        public Coords coords = new Coords();
    }

    public static class Coords {
        public int x, y;
        public int width = 1, height = 1;

        public boolean equals(Coords other) {
            return x == other.x && y == other.y;
        }

        public int distance(Coords other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    public static class Backpack {
        public static final int MAX_PER_TYPE = 9;

        public List<Food> foods = new ArrayList<>();
        public List<Elixir> elixirs = new ArrayList<>();
        public List<Scroll> scrolls = new ArrayList<>();
        public List<Weapon> weapons = new ArrayList<>();
        public int treasures;

        public boolean canAddFood() { return foods.size() < MAX_PER_TYPE; }
        public boolean canAddElixir() { return elixirs.size() < MAX_PER_TYPE; }
        public boolean canAddScroll() { return scrolls.size() < MAX_PER_TYPE; }
        public boolean canAddWeapon() { return weapons.size() < MAX_PER_TYPE; }
    }

    public static class Food {
        public int regen;
        public String name;
    }

    public static class Elixir {
        public int type; // 0-HEALTH, 1-AGILITY, 2-STRENGTH
        public int increase;
        public int duration;
        public String name;
    }

    public static class Scroll {
        public int type;
        public int increase;
        public String name;
    }

    public static class Weapon {
        public int strength;
        public String name;
    }

    public static class Buffs {
        public List<Buff> maxHealth = new ArrayList<>();
        public List<Buff> agility = new ArrayList<>();
        public List<Buff> strength = new ArrayList<>();
    }

    public static class Buff {
        public int increase;
        public long endTime;
    }

    public static class Level {
        public Coords coords = new Coords();
        public Room[] rooms = new Room[9];
        public Passages passages = new Passages();
        public int levelNum;
        public Coords exit = new Coords();

        public Level() {
            for (int i = 0; i < 9; i++) rooms[i] = new Room();
        }
    }

    public static class Room {
        public Coords coords = new Coords();
        public RoomConsumables consumables = new RoomConsumables();
        public List<Monster> monsters = new ArrayList<>();

        public Room() {
            this.coords = new Coords();
            this.consumables = new RoomConsumables();
            this.monsters = new ArrayList<>();
        }
    }

    public static class RoomConsumables {
        public List<RoomFood> foods = new ArrayList<>();
        public List<RoomElixir> elixirs = new ArrayList<>();
        public List<RoomScroll> scrolls = new ArrayList<>();
        public List<RoomWeapon> weapons = new ArrayList<>();

        public RoomConsumables() {
            this.foods = new ArrayList<>();
            this.elixirs = new ArrayList<>();
            this.scrolls = new ArrayList<>();
            this.weapons = new ArrayList<>();
        }
    }

    public static class RoomFood {
        public Food food;
        public Coords geometry = new Coords();
    }

    public static class RoomElixir {
        public Elixir elixir;
        public Coords geometry = new Coords();
    }

    public static class RoomScroll {
        public Scroll scroll;
        public Coords geometry = new Coords();
    }

    public static class RoomWeapon {
        public Weapon weapon;
        public Coords geometry = new Coords();
    }

    public static class Monster {
        public CharacterStats baseStats = new CharacterStats();
        public int type; // 0-ZOMBIE, 1-VAMPIRE, 2-GHOST, 3-OGRE, 4-SNAKE
        public int hostility; // 0-LOW, 1-AVERAGE, 2-HIGH
        public boolean isChasing;
        public int direction;

        public boolean firstStrikeUsed;
        public boolean isResting;
        public boolean isInvisible;
    }

    public static class Passages {
        public List<Coords> passages = new ArrayList<>();

        public Passages() {
            this.passages = new ArrayList<>();
        }
    }


    public static class BattleInfo {
        public boolean isFight;
        public Monster enemy;
        public boolean vampireFirstAttack = true;
        public boolean ogreCooldown;
        public boolean playerAsleep;
    }

    public static class GameStatistics {
        public int level;
        public int treasures;
        public int enemiesKilled;
        public int foodEaten;
        public int potionsDrunk;
        public int scrollsRead;
        public int hitsDealt;
        public int hitsTaken;
        public int moves;
        public long timestamp = System.currentTimeMillis();
    }
}