package roguelike.domain.service;

import roguelike.domain.model.GameData;
import java.util.Iterator;

public class ItemService {

    public void checkPickup(GameData data, GameData.GameStatistics stats) {
        GameData.Player player = data.player;
        GameData.Room room = findPlayerRoom(data);
        if (room == null) return;

        Iterator<GameData.RoomFood> foodIter = room.consumables.foods.iterator();
        while (foodIter.hasNext()) {
            GameData.RoomFood rf = foodIter.next();
            if (rf.geometry.equals(player.baseStats.coords)) {
                if (player.backpack.canAddFood()) {
                    player.backpack.foods.add(rf.food);
                    data.messages.add("Picked up: " + rf.food.name);
                    foodIter.remove();
                } else {
                    data.messages.add("Food backpack is full!");
                }
                return;
            }
        }
        Iterator<GameData.RoomElixir> elixirIter = room.consumables.elixirs.iterator();
        while (elixirIter.hasNext()) {
            GameData.RoomElixir re = elixirIter.next();
            if (re.geometry.equals(player.baseStats.coords)) {
                if (player.backpack.canAddElixir()) {
                    player.backpack.elixirs.add(re.elixir);
                    data.messages.add("Picked up: " + re.elixir.name);
                    elixirIter.remove();
                } else {
                    data.messages.add("Elixir backpack is full!");
                }
                return;
            }
        }

        Iterator<GameData.RoomScroll> scrollIter = room.consumables.scrolls.iterator();
        while (scrollIter.hasNext()) {
            GameData.RoomScroll rs = scrollIter.next();
            if (rs.geometry.equals(player.baseStats.coords)) {
                if (player.backpack.canAddScroll()) {
                    player.backpack.scrolls.add(rs.scroll);
                    data.messages.add("Picked up: " + rs.scroll.name);
                    scrollIter.remove();
                } else {
                    data.messages.add("Scroll backpack is full!");
                }
                return;
            }
        }

        Iterator<GameData.RoomWeapon> weaponIter = room.consumables.weapons.iterator();
        while (weaponIter.hasNext()) {
            GameData.RoomWeapon rw = weaponIter.next();
            if (rw.geometry.equals(player.baseStats.coords)) {
                if (player.backpack.canAddWeapon()) {
                    player.backpack.weapons.add(rw.weapon);
                    data.messages.add("Picked up: " + rw.weapon.name);
                    weaponIter.remove();
                } else {
                    data.messages.add("Weapon backpack is full!");
                }
                return;
            }
        }
    }

    public void useFood(GameData.Player player, int index, GameData.GameStatistics stats) {
        if (index >= 0 && index < player.backpack.foods.size()) {
            GameData.Food food = player.backpack.foods.get(index);
            player.baseStats.health = Math.min(
                    player.baseStats.health + food.regen,
                    player.maxHp
            );
            player.backpack.foods.remove(index);
            stats.foodEaten++;
        }
    }

    public void useElixir(GameData.Player player, int index, GameData.GameStatistics stats) {
        if (index >= 0 && index < player.backpack.elixirs.size()) {
            GameData.Elixir elixir = player.backpack.elixirs.get(index);

            GameData.Buff buff = new GameData.Buff();
            buff.increase = elixir.increase;
            buff.endTime = System.currentTimeMillis() / 1000 + elixir.duration;

            switch (elixir.type) {
                case 0: // HEALTH
                    player.maxHp += elixir.increase;
                    player.baseStats.health += elixir.increase;
                    player.elixirBuffs.maxHealth.add(buff);
                    break;
                case 1: // AGILITY
                    player.baseStats.agility += elixir.increase;
                    player.elixirBuffs.agility.add(buff);
                    break;
                case 2: // STRENGTH
                    player.baseStats.strength += elixir.increase;
                    player.elixirBuffs.strength.add(buff);
                    break;
            }

            player.backpack.elixirs.remove(index);
            stats.potionsDrunk++;
        }
    }

    public void useScroll(GameData.Player player, int index, GameData.GameStatistics stats) {
        if (index >= 0 && index < player.backpack.scrolls.size()) {
            GameData.Scroll scroll = player.backpack.scrolls.get(index);

            switch (scroll.type) {
                case 0: // HEALTH
                    player.maxHp += scroll.increase;
                    player.baseStats.health += scroll.increase;
                    break;
                case 1: // AGILITY
                    player.baseStats.agility += scroll.increase;
                    break;
                case 2: // STRENGTH
                    player.baseStats.strength += scroll.increase;
                    break;
            }

            player.backpack.scrolls.remove(index);
            stats.scrollsRead++;
        }
    }

    public void equipWeapon(GameData.Player player, int index, GameData data) {
        if (index >= 0 && index < player.backpack.weapons.size()) {
            GameData.Weapon newWeapon = player.backpack.weapons.get(index);

            if (player.weapon.strength > 0) {
                if (player.backpack.canAddWeapon()) {
                    player.backpack.weapons.add(player.weapon);
                    data.messages.add("Previous weapon returned to inventory");
                } else {
                    dropWeapon(player, data);
                    data.messages.add("Previous weapon dropped on floor");
                }
            }

            player.weapon = newWeapon;
            player.backpack.weapons.remove(index);
            data.messages.add("Equipped: " + newWeapon.name);
        } else if (index == -1) {
            if (player.weapon.strength > 0) {
                if (player.backpack.canAddWeapon()) {
                    player.backpack.weapons.add(player.weapon);
                    data.messages.add("Weapon returned to inventory");
                } else {
                    dropWeapon(player, data);
                    data.messages.add("No space in inventory, weapon dropped on floor");
                }

                player.weapon = new GameData.Weapon();
                data.messages.add("Weapon unequipped");
            }
        }
    }

    private void dropWeapon(GameData.Player player, GameData data) {
        GameData.Room room = findPlayerRoom(data);
        if (room == null) return;

        GameData.RoomWeapon dropped = new GameData.RoomWeapon();
        dropped.weapon = player.weapon;

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] dir : dirs) {
            int nx = player.baseStats.coords.x + dir[0];
            int ny = player.baseStats.coords.y + dir[1];

            if (isPositionFree(room, nx, ny)) {
                dropped.geometry.x = nx;
                dropped.geometry.y = ny;
                room.consumables.weapons.add(dropped);
                return;
            }
        }

        dropped.geometry.x = player.baseStats.coords.x;
        dropped.geometry.y = player.baseStats.coords.y;
        room.consumables.weapons.add(dropped);
    }

    private boolean isPositionFree(GameData.Room room, int x, int y) {
        for (GameData.Monster m : room.monsters) {
            if (m.baseStats.coords.x == x && m.baseStats.coords.y == y)
                return false;
        }

        for (GameData.RoomWeapon w : room.consumables.weapons) {
            if (w.geometry.x == x && w.geometry.y == y)
                return false;
        }

        for (GameData.RoomFood f : room.consumables.foods) {
            if (f.geometry.x == x && f.geometry.y == y)
                return false;
        }

        for (GameData.RoomElixir e : room.consumables.elixirs) {
            if (e.geometry.x == x && e.geometry.y == y)
                return false;
        }
        for (GameData.RoomScroll s : room.consumables.scrolls) {
            if (s.geometry.x == x && s.geometry.y == y)
                return false;
        }

        return true;
    }

    public void checkBuffExpiration(GameData.Player player) {
        long now = System.currentTimeMillis() / 1000;

        Iterator<GameData.Buff> healthIter = player.elixirBuffs.maxHealth.iterator();
        while (healthIter.hasNext()) {
            GameData.Buff buff = healthIter.next();
            if (now >= buff.endTime) {
                player.maxHp -= buff.increase;
                player.baseStats.health = Math.max(1, player.baseStats.health - buff.increase);
                healthIter.remove();
            }
        }

        Iterator<GameData.Buff> agilIter = player.elixirBuffs.agility.iterator();
        while (agilIter.hasNext()) {
            GameData.Buff buff = agilIter.next();
            if (now >= buff.endTime) {
                player.baseStats.agility -= buff.increase;
                agilIter.remove();
            }
        }

        Iterator<GameData.Buff> strIter = player.elixirBuffs.strength.iterator();
        while (strIter.hasNext()) {
            GameData.Buff buff = strIter.next();
            if (now >= buff.endTime) {
                player.baseStats.strength -= buff.increase;
                strIter.remove();
            }
        }
    }

    private GameData.Room findPlayerRoom(GameData data) {
        for (GameData.Room room : data.level.rooms) {
            if (data.player.baseStats.coords.x >= room.coords.x &&
                    data.player.baseStats.coords.x < room.coords.x + room.coords.width &&
                    data.player.baseStats.coords.y >= room.coords.y &&
                    data.player.baseStats.coords.y < room.coords.y + room.coords.height) {
                return room;
            }
        }
        return null;
    }
}