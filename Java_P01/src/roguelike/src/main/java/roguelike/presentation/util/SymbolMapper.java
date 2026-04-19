package roguelike.presentation.util;

import com.googlecode.lanterna.TextColor;

public class SymbolMapper {

    public char monsterChar(int type) {
        switch (type) {
            case 0: return 'z'; // Zombie
            case 1: return 'v'; // Vampire
            case 2: return 'g'; // Ghost
            case 3: return 'O'; // Ogre
            case 4: return 's'; // Snake
            default: return '?';
        }
    }

    public TextColor monsterColor(int type) {
        switch (type) {
            case 0: return TextColor.ANSI.GREEN;
            case 1: return TextColor.ANSI.RED;
            case 2: return TextColor.ANSI.WHITE;
            case 3: return TextColor.ANSI.YELLOW;
            case 4: return TextColor.ANSI.WHITE_BRIGHT;
            default: return TextColor.ANSI.WHITE;
        }
    }
}