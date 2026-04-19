package roguelike.presentation.util;

import roguelike.domain.model.GameData;
import java.util.*;

public class FogOfWar {
    private boolean[][] explored;
    private boolean[][] visible;
    private int sightRadius = 8;
    private int width, height;

    private GameData.Room currentPlayerRoom = null;

    public FogOfWar(int width, int height) {
        this.width = width;
        this.height = height;
        this.explored = new boolean[height][width];
        this.visible = new boolean[height][width];
    }

    public void update(GameData data) {
        for (int y = 0; y < height; y++) {
            Arrays.fill(visible[y], false);
        }

        int px = data.player.baseStats.coords.x;
        int py = data.player.baseStats.coords.y;

        GameData.Room currentRoom = findRoom(data, px, py);
        if (currentRoom != null) {
            markRoomExplored(currentRoom);
            makeRoomVisible(currentRoom);
            currentPlayerRoom = currentRoom;
        }

        calculateVisibility(data, px, py);
    }

    private void makeRoomVisible(GameData.Room room) {
        if (room == null || room.coords == null) return;

        for (int x = room.coords.x; x < room.coords.x + room.coords.width; x++) {
            for (int y = room.coords.y; y < room.coords.y + room.coords.height; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    visible[y][x] = true;
                    explored[y][x] = true;
                }
            }
        }
    }

    private void calculateVisibility(GameData data, int px, int py) {
        for (int dx = -sightRadius; dx <= sightRadius; dx++) {
            for (int dy = -sightRadius; dy <= sightRadius; dy++) {
                if (dx == 0 && dy == 0) {
                    setVisible(px, py, true);
                    continue;
                }

                int targetX = px + dx;
                int targetY = py + dy;

                if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) {
                    continue;
                }

                List<int[]> line = bresenhamLine(px, py, targetX, targetY);
                boolean visible = true;

                for (int[] point : line) {
                    if (point[0] == px && point[1] == py) continue;

                    if (distance(px, py, point[0], point[1]) > sightRadius) {
                        visible = false;
                        break;
                    }

                    if (isWall(data, point[0], point[1])) {
                        setVisible(point[0], point[1], true);
                        visible = false;
                        break;
                    }

                    setVisible(point[0], point[1], true);
                }
            }
        }
    }



    private List<int[]> bresenhamLine(int x0, int y0, int x1, int y1) {
        List<int[]> line = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            line.add(new int[]{x0, y0});
            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return line;
    }

    private void markRoomExplored(GameData.Room room) {
        for (int x = room.coords.x; x < room.coords.x + room.coords.width; x++) {
            for (int y = room.coords.y; y < room.coords.y + room.coords.height; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    explored[y][x] = true;
                }
            }
        }
    }

    private GameData.Room findRoom(GameData data, int x, int y) {
        for (GameData.Room room : data.level.rooms) {
            if (x >= room.coords.x && x < room.coords.x + room.coords.width &&
                    y >= room.coords.y && y < room.coords.y + room.coords.height) {
                return room;
            }
        }
        return null;
    }


    private boolean isWall(GameData data, int x, int y) {
        for (GameData.Room room : data.level.rooms) {
            if (x >= room.coords.x && x < room.coords.x + room.coords.width &&
                    y >= room.coords.y && y < room.coords.y + room.coords.height) {

                if (x == room.coords.x || x == room.coords.x + room.coords.width - 1 ||
                        y == room.coords.y || y == room.coords.y + room.coords.height - 1) {
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    private void setVisible(int x, int y, boolean value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            visible[y][x] = value;
            if (value) {
                explored[y][x] = true;
            }
        }
    }

    public boolean isVisible(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return visible[y][x];
    }

    public boolean isExplored(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return explored[y][x];
    }

    public boolean isMonsterVisible(GameData data, GameData.Monster monster, int playerX, int playerY) {
        int mx = monster.baseStats.coords.x;
        int my = monster.baseStats.coords.y;
        if (monster.type == 2 && monster.isInvisible && !monster.isChasing) {
            return false;
        }

        if (!isVisible(mx, my)) return false;

        List<int[]> line = bresenhamLine(playerX, playerY, mx, my);
        for (int[] point : line) {
            if (point[0] == playerX && point[1] == playerY) continue;
            if (point[0] == mx && point[1] == my) break;

            if (isWall(data, point[0], point[1])) {
                return false;
            }
        }

        return true;
    }

    public void reset() {
        for (int y = 0; y < height; y++) {
            Arrays.fill(explored[y], false);
            Arrays.fill(visible[y], false);
        }
    }
}