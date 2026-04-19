package domain.model;

import java.util.Arrays;

public class Board {
    public static final int SIZE = 3;
    private final int[][] cells;

    private Board(int[][] cells) {
        this.cells = cells;
    }

    public static Board empty() {
        int[][] cells = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(cells[i], 0);
        }
        return new Board(cells);
    }
}
