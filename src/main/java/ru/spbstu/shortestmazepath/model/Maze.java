package ru.spbstu.shortestmazepath.model;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    private final int height;
    private final int width;
    private final Cell startCell;
    private final Cell endCell;
    private final MazeCell[][] mazeGrid;

    public Maze(int height, int width, Cell startCell, Cell endCell, MazeCell[][] mazeGrid) {
        this.height = height;
        this.width = width;
        this.startCell = startCell;
        this.endCell = endCell;
        if (mazeGrid.length != width || mazeGrid[0].length != height)
            throw new IllegalArgumentException("Wrong height or width passed!");
        this.mazeGrid = mazeGrid;
    }

    public static class Cell {
        public final int x;
        public final int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            if (x != cell.x) return false;
            return y == cell.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        /**
         * Calculating Manhattan distance between two points.
         *
         * @param other the second point.
         * @return Manhattan distance between points.
         */
        public double distanceTo(Cell other) {
            int dx = x - other.x;
            int dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public List<Cell> solve() {
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
