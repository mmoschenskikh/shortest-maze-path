package ru.spbstu.shortestmazepath.model;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    private final int height;
    private final int width;
    private final MazeCell[][] mazeGrid;

    public Maze(int height, int width, MazeCell[][] mazeGrid) {
        this.height = height;
        this.width = width;
        if (mazeGrid.length != height || mazeGrid[0].length != width)
            throw new IllegalArgumentException("Wrong height or width passed!");
        this.mazeGrid = mazeGrid;
    }

    public List<MazeCell> solve() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("* ".repeat(width + 2));
        sb.append('\n');
        for (int i = 0; i < height; i++) {
            sb.append("*");
            for (int j = 0; j < width; j++) {
                sb.append(' ');
                sb.append(mazeGrid[i][j]);
            }
            sb.append(" *\n");
        }
        sb.append("* ".repeat(width + 2));
        return sb.toString();
    }
}
