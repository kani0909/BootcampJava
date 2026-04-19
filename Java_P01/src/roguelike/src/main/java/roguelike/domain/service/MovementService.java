package roguelike.domain.service;

import roguelike.domain.model.GameData;
import java.util.*;

public class MovementService {
    private static final Random rand = new Random();
    private static final int SIMPLE_DIRECTIONS = 4;
    private static final int ALL_DIRECTIONS = 8;
    private static final int MAX_TRIES = 16;
    private static final int LOW_HOSTILITY_RADIUS = 2;
    private static final int AVERAGE_HOSTILITY_RADIUS = 4;
    private static final int HIGH_HOSTILITY_RADIUS = 6;

    public void movePlayer(GameData data, int dx, int dy, GameData.GameStatistics stats) {
        if (data.gameOver) return;

        for (GameData.BattleInfo battle : data.battles) {
            if (battle.isFight && battle.playerAsleep) {
                battle.playerAsleep = false;
                data.messages.add("You wake up!");
                return;
            }
        }

        GameData.Coords newPos = new GameData.Coords();
        newPos.x = data.player.baseStats.coords.x + dx;
        newPos.y = data.player.baseStats.coords.y + dy;

        if (canMove(data, newPos)) {
            data.player.baseStats.coords = newPos;
            stats.moves++;
        }
    }

    private boolean canMove(GameData data, GameData.Coords pos) {
        System.out.println("Checking canMove to: (" + pos.x + "," + pos.y + ")");

        if (pos.x < 0 || pos.x >= 80 || pos.y < 0 || pos.y >= 40) {
            return false;
        }

        if (isPassage(data, pos)) {
            System.out.println("Can move - in passage");
            return true;
        }

        if (isInsideRoom(data, pos)) {
            System.out.println("Can move - inside room");
            return true;
        }

        System.out.println("Cannot move to (" + pos.x + "," + pos.y + ")");
        return false;
    }

    private boolean isPassage(GameData data, GameData.Coords pos) {
        if (data.level.passages == null || data.level.passages.passages == null) {
            return false;
        }

        for (GameData.Coords passage : data.level.passages.passages) {
            if (passage == null) continue;

            if (pos.x >= passage.x &&
                    pos.x < passage.x + passage.width &&
                    pos.y >= passage.y &&
                    pos.y < passage.y + passage.height) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideRoom(GameData data, GameData.Coords pos) {
        for (GameData.Room room : data.level.rooms) {
            if (room == null || room.coords == null) continue;

            if (pos.x > room.coords.x &&
                    pos.x < room.coords.x + room.coords.width - 1 &&
                    pos.y > room.coords.y &&
                    pos.y < room.coords.y + room.coords.height - 1) {
                return true;
            }
        }
        return false;
    }

    public void moveAllMonsters(GameData data) {
        for (GameData.Room room : data.level.rooms) {
            for (GameData.Monster monster : room.monsters) {
                moveMonster(data, monster);
            }
        }
    }

    private void moveMonster(GameData data, GameData.Monster monster) {
        boolean seesPlayer = isPlayerInRange(data, monster);

        if (seesPlayer && !monster.isChasing) {
            monster.isChasing = true;
        }

        if (monster.isChasing) {
            chasePlayer(data, monster);
        } else {
            moveByPattern(data, monster);
        }
    }

    private boolean isPlayerInRange(GameData data, GameData.Monster monster) {
        int dist = monster.baseStats.coords.distance(data.player.baseStats.coords);

        switch (monster.hostility) {
            case 0: return dist <= LOW_HOSTILITY_RADIUS;
            case 1: return dist <= AVERAGE_HOSTILITY_RADIUS;
            case 2: return dist <= HIGH_HOSTILITY_RADIUS;
            default: return false;
        }
    }

    private void chasePlayer(GameData data, GameData.Monster monster) {
        int dx = Integer.compare(data.player.baseStats.coords.x, monster.baseStats.coords.x);
        int dy = Integer.compare(data.player.baseStats.coords.y, monster.baseStats.coords.y);

        int steps = 1;
        if (monster.type == 1) steps = 2;
        if (monster.type == 3) steps = 2;

        for (int i = 0; i < steps; i++) {
            GameData.Coords newPos = new GameData.Coords();
            newPos.x = monster.baseStats.coords.x + dx;
            newPos.y = monster.baseStats.coords.y + dy;

            if (canMove(data, newPos) &&
                    !newPos.equals(data.player.baseStats.coords)) {
                monster.baseStats.coords = newPos;
            }
        }
    }

    private void moveByPattern(GameData data, GameData.Monster monster) {
        switch (monster.type) {
            case 0: // Zombie - медленный
                moveZombie(data, monster);
                break;
            case 1: // Vampire - случайный
                moveRandom(data, monster, ALL_DIRECTIONS);
                break;
            case 2: // Ghost - телепортация
                moveGhost(data, monster);
                break;
            case 3: // Ogre - 2 шага
                moveOgre(data, monster);
                break;
            case 4: // Snake - диагональ
                moveSnake(data, monster);
                break;
        }
    }

    private void moveZombie(GameData data, GameData.Monster monster) {
        if (rand.nextInt(2) == 0) return;
        moveRandom(data, monster, SIMPLE_DIRECTIONS);
    }

    private void moveRandom(GameData data, GameData.Monster monster, int maxDir) {
        for (int attempt = 0; attempt < MAX_TRIES; attempt++) {
            int dir = rand.nextInt(maxDir);
            int dx = 0, dy = 0;

            switch (dir) {
                case 0: dy = -1; break; // FORWARD
                case 1: dx = -1; break; // LEFT
                case 2: dx = 1; break;  // RIGHT
                case 3: dy = 1; break;  // BACK
                case 4: dx = -1; dy = -1; break; // DIAGONAL
                case 5: dx = 1; dy = -1; break;
                case 6: dx = -1; dy = 1; break;
                case 7: dx = 1; dy = 1; break;
            }

            GameData.Coords newPos = new GameData.Coords();
            newPos.x = monster.baseStats.coords.x + dx;
            newPos.y = monster.baseStats.coords.y + dy;

            if (canMove(data, newPos)) {
                monster.baseStats.coords = newPos;
                monster.direction = dir;
                return;
            }
        }
    }

    private void moveGhost(GameData data, GameData.Monster monster) {
        if (rand.nextInt(10) < 3) {
            GameData.Room room = findRoom(data, monster.baseStats.coords);
            if (room != null) {
                monster.baseStats.coords.x = room.coords.x + 1 +
                        rand.nextInt(room.coords.width - 2);
                monster.baseStats.coords.y = room.coords.y + 1 +
                        rand.nextInt(room.coords.height - 2);
                monster.isInvisible = rand.nextBoolean();
                return;
            }
        }
        moveRandom(data, monster, ALL_DIRECTIONS);
    }

    private void moveOgre(GameData data, GameData.Monster monster) {
        for (int step = 0; step < 2; step++) {
            int dir = rand.nextInt(SIMPLE_DIRECTIONS);
            int dx = 0, dy = 0;
            switch (dir) {
                case 0: dy = -1; break;
                case 1: dx = -1; break;
                case 2: dx = 1; break;
                case 3: dy = 1; break;
            }

            GameData.Coords newPos = new GameData.Coords();
            newPos.x = monster.baseStats.coords.x + dx;
            newPos.y = monster.baseStats.coords.y + dy;

            if (canMove(data, newPos)) {
                monster.baseStats.coords = newPos;
            }
        }
    }

    private void moveSnake(GameData data, GameData.Monster monster) {
        int[][] diagDirs = {{-1,-1}, {1,-1}, {-1,1}, {1,1}};

        if (monster.direction == 0 || !tryMoveSnakeDirection(data, monster, monster.direction)) {
            List<int[]> availableDirs = new ArrayList<>();
            for (int[] dir : diagDirs) {
                GameData.Coords newPos = new GameData.Coords();
                newPos.x = monster.baseStats.coords.x + dir[0];
                newPos.y = monster.baseStats.coords.y + dir[1];

                if (canMove(data, newPos) && !newPos.equals(data.player.baseStats.coords)) {
                    availableDirs.add(dir);
                }
            }

            if (!availableDirs.isEmpty()) {
                int[] chosen = availableDirs.get(rand.nextInt(availableDirs.size()));
                monster.baseStats.coords.x += chosen[0];
                monster.baseStats.coords.y += chosen[1];
                monster.direction = chosen[0] + chosen[1];
            }
        }
    }

    private boolean tryMoveSnakeDirection(GameData data, GameData.Monster monster, int direction) {
        if (direction == 0) return false;

        int dx = 0, dy = 0;
        if (direction == -2) { dx = -1; dy = -1; }
        else if (direction == 0) { return false; }
        else if (direction == 2) { // может быть 1+1=2 или -1+1=0 или 1+(-1)=0
            if (tryMoveOffset(data, monster, 1, 1)) return true;
            if (tryMoveOffset(data, monster, -1, 1)) return true;
            if (tryMoveOffset(data, monster, 1, -1)) return true;
            return false;
        }

        return tryMoveOffset(data, monster, dx, dy);
    }

    private boolean tryMoveOffset(GameData data, GameData.Monster monster, int dx, int dy) {
        GameData.Coords newPos = new GameData.Coords();
        newPos.x = monster.baseStats.coords.x + dx;
        newPos.y = monster.baseStats.coords.y + dy;

        if (canMove(data, newPos) && !newPos.equals(data.player.baseStats.coords)) {
            monster.baseStats.coords = newPos;
            monster.direction = dx + dy;
            return true;
        }
        return false;
    }

    private GameData.Room findRoom(GameData data, GameData.Coords pos) {
        for (GameData.Room room : data.level.rooms) {
            if (pos.x >= room.coords.x && pos.x < room.coords.x + room.coords.width &&
                    pos.y >= room.coords.y && pos.y < room.coords.y + room.coords.height) {
                return room;
            }
        }
        return null;
    }
}