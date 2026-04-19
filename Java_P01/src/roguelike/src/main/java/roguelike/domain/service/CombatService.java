package roguelike.domain.service;

import roguelike.domain.model.GameData;
import java.util.Random;

public class CombatService {
    private static final Random rand = new Random();
    private static final int INITIAL_HIT_CHANCE = 70;
    private static final double AGILITY_FACTOR = 0.3;
    private static final int INITIAL_DAMAGE = 30;
    private static final int STANDART_STRENGTH = 50;
    private static final double STRENGTH_FACTOR = 0.3;
    private static final int STRENGTH_ADDITION = 65;
    private static final int SLEEP_CHANCE = 15;
    private static final double LOOT_AGILITY_FACTOR = 0.2;
    private static final double LOOT_HP_FACTOR = 0.5;
    private static final double LOOT_STRENGTH_FACTOR = 0.5;

    public void updateFights(GameData data) {
        GameData.Player player = data.player;

        for (GameData.Room room : data.level.rooms) {
            for (GameData.Monster monster : room.monsters) {
                if (checkContact(player.baseStats.coords, monster.baseStats.coords) &&
                        !isInBattle(monster, data.battles)) {
                    startBattle(data, monster);
                }
            }
        }
        for (GameData.BattleInfo battle : data.battles) {
            if (battle.isFight) {
                if (!checkContact(player.baseStats.coords, battle.enemy.baseStats.coords) ||
                        battle.enemy.baseStats.health <= 0) {
                    battle.isFight = false;
                }
            }
        }
    }

    private boolean checkContact(GameData.Coords player, GameData.Coords monster) {
        return Math.abs(player.x - monster.x) <= 1 &&
                Math.abs(player.y - monster.y) <= 1;
    }

    private boolean isInBattle(GameData.Monster monster, GameData.BattleInfo[] battles) {
        for (GameData.BattleInfo b : battles) {
            if (b.isFight && b.enemy == monster) return true;
        }
        return false;
    }

    private void startBattle(GameData data, GameData.Monster monster) {
        for (GameData.BattleInfo battle : data.battles) {
            if (!battle.isFight) {
                battle.isFight = true;
                battle.enemy = monster;
                battle.vampireFirstAttack = true;
                battle.ogreCooldown = false;
                battle.playerAsleep = false;
                if (monster.type == 1) {
                    monster.firstStrikeUsed = false;
                }
                break;
            }
        }
    }

    public boolean processPlayerAttack(GameData data, GameData.BattleInfo battle, GameData.GameStatistics stats) {
        GameData.Player player = data.player;
        GameData.Monster monster = battle.enemy;

        if (monster.type == 1 && !monster.firstStrikeUsed) {
            monster.firstStrikeUsed = true;
            data.messages.add("Vampire dodges first strike!");
            return false;
        }

        int hitChance = INITIAL_HIT_CHANCE +
                (int)((player.baseStats.agility - monster.baseStats.agility) * AGILITY_FACTOR);
        hitChance = Math.max(20, Math.min(95, hitChance));

        if (rand.nextInt(100) < hitChance) {
            int damage;
            if (player.weapon.strength == 0) {
                damage = INITIAL_DAMAGE +
                        (int)((player.baseStats.strength - STANDART_STRENGTH) * STRENGTH_FACTOR);
            } else {
                damage = player.weapon.strength *
                        (player.baseStats.strength + STRENGTH_ADDITION) / 100;
            }

            damage = damage * (75 + rand.nextInt(51)) / 100;

            monster.baseStats.health -= damage;
            stats.hitsDealt++;
            data.messages.add("You hit for " + damage + " damage!");

            if (monster.baseStats.health <= 0) {
                int loot = calculateLoot(monster);
                player.backpack.treasures += loot;
                stats.treasures += loot;
                stats.enemiesKilled++;
                data.messages.add("Monster killed! +" + loot + " gold");
                removeMonster(data, monster);
                return true;
            }
        } else {
            data.messages.add("You missed!");
        }
        return false;
    }

    public void processMonsterAttack(GameData data, GameData.BattleInfo battle, GameData.GameStatistics stats) {
        GameData.Player player = data.player;
        GameData.Monster monster = battle.enemy;

        if (monster.baseStats.health <= 0) return;

        if (battle.playerAsleep) {
            data.messages.add("You are asleep and cannot fight!");
            return;
        }

        if (monster.type == 3 && battle.ogreCooldown) {
            battle.ogreCooldown = false;
            data.messages.add("Ogre is resting...");
            return;
        }

        int hitChance = 40 + (int)((monster.baseStats.agility - player.baseStats.agility) * AGILITY_FACTOR);
        hitChance = Math.max(20, Math.min(95, hitChance));

        if (monster.type == 3 || rand.nextInt(100) < hitChance) {
            int damage = calculateMonsterDamage(monster, battle);

            switch (monster.type) {
                case 1: // Vampire
                    player.maxHp = Math.max(1, player.maxHp - damage / 2);
                    player.baseStats.health -= damage;
                    data.messages.add("Vampire drains your max HP!");
                    break;

                case 2: // Ghost
                    player.baseStats.health -= damage;
                    if (rand.nextInt(100) < 30) {
                        monster.isInvisible = !monster.isInvisible;
                        data.messages.add("Ghost becomes " +
                                (monster.isInvisible ? "invisible!" : "visible!"));
                    }
                    break;

                case 3: // Ogre
                    player.baseStats.health -= damage;
                    battle.ogreCooldown = true;
                    data.messages.add("Ogre smashes you and rests!");
                    break;

                case 4: // Snake
                    player.baseStats.health -= damage;
                    data.messages.add("Snake Mage hits for " + damage + " damage!");
                    if (rand.nextInt(100) < SLEEP_CHANCE) {
                        battle.playerAsleep = true;
                        data.messages.add("Snake Mage puts you to sleep! You will miss your next turn!");
                    }
                    break;

                default:
                    player.baseStats.health -= damage;
                    data.messages.add("Monster hits for " + damage + " damage!");
            }

            stats.hitsTaken++;

            if (player.baseStats.health <= 0) {
                data.gameOver = true;
                data.messages.add("YOU DIED!");
            }
        } else {
            data.messages.add("Monster missed!");
        }
    }

    private int calculateMonsterDamage(GameData.Monster monster, GameData.BattleInfo battle) {
        int baseDamage = INITIAL_DAMAGE +
                (int)((monster.baseStats.strength - STANDART_STRENGTH) * STRENGTH_FACTOR);

        switch (monster.type) {
            case 1: // Vampire
                return monster.baseStats.strength;
            case 3: // Ogre
                return (int)(monster.baseStats.strength * 1.5);
            default:
                return baseDamage;
        }
    }

    private int calculateLoot(GameData.Monster monster) {
        return (int)(monster.baseStats.agility * LOOT_AGILITY_FACTOR +
                monster.baseStats.health * LOOT_HP_FACTOR +
                monster.baseStats.strength * LOOT_STRENGTH_FACTOR +
                rand.nextInt(20));
    }

    private void removeMonster(GameData data, GameData.Monster monster) {
        for (GameData.Room room : data.level.rooms) {
            room.monsters.removeIf(m -> m == monster);
        }
    }
}