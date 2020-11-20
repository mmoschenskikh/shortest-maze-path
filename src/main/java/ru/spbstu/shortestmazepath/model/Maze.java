package ru.spbstu.shortestmazepath.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Cell> getNeighbours(Cell cell) {
        Set<Cell> set = new HashSet<>();
        if (cell.x - 1 >= 0 && mazeGrid[cell.x - 1][cell.y] != MazeCell.WALL) { // Left neighbour
            set.add(new Cell(cell.x - 1, cell.y));
        }
        if (cell.x + 1 < width && mazeGrid[cell.x + 1][cell.y] != MazeCell.WALL) { // Right neighbour
            set.add(new Cell(cell.x + 1, cell.y));
        }
        if (cell.y - 1 >= 0 && mazeGrid[cell.x][cell.y - 1] != MazeCell.WALL) { // Down neighbour
            set.add(new Cell(cell.x, cell.y - 1));
        }
        if (cell.y + 1 < height && mazeGrid[cell.x][cell.y + 1] != MazeCell.WALL) { // Up neighbour
            set.add(new Cell(cell.x, cell.y + 1));
        }
        return set;
    }

    public double getHeuristics(Cell cell) {
        return cell.distanceTo(endCell);
    }

    public List<Cell> solve() throws IllegalArgumentException {
        throw new IllegalArgumentException("No path between start and end point exists!");
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
