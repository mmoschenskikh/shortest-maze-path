package ru.spbstu.shortestmazepath.model;

import java.util.*;

public class Maze {

    private final int height;
    private final int width;
    private final Cell startCell;
    private final Cell endCell;
    private final Cell[][] mazeGrid;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getMazeGrid() {
        return mazeGrid;
    }

    public Maze(int height, int width, Cell startCell, Cell endCell, Cell[][] mazeGrid) {
        this.height = height;
        this.width = width;
        this.startCell = startCell;
        this.endCell = endCell;
        if (mazeGrid.length != width || mazeGrid[0].length != height)
            throw new IllegalArgumentException("Wrong height or width passed!");
        this.mazeGrid = mazeGrid;
    }

    public Set<Cell> getNeighbours(Cell cell) {
        Set<Cell> set = new HashSet<>();
        if (cell.x - 1 >= 0 && mazeGrid[cell.x - 1][cell.y].type != Cell.Type.WALL) { // Left neighbour
            set.add(mazeGrid[cell.x - 1][cell.y]);
        }
        if (cell.x + 1 < width && mazeGrid[cell.x + 1][cell.y].type != Cell.Type.WALL) { // Right neighbour
            set.add(mazeGrid[cell.x + 1][cell.y]);
        }
        if (cell.y - 1 >= 0 && mazeGrid[cell.x][cell.y - 1].type != Cell.Type.WALL) { // Down neighbour
            set.add(mazeGrid[cell.x][cell.y - 1]);
        }
        if (cell.y + 1 < height && mazeGrid[cell.x][cell.y + 1].type != Cell.Type.WALL) { // Up neighbour
            set.add(mazeGrid[cell.x][cell.y + 1]);
        }
        return set;
    }

    public double getHeuristics(Cell cell) {
        return cell.distanceTo(endCell);
    }

    public List<Cell> solve() throws IllegalArgumentException {
        if (startCell == null || endCell == null)
            throw new IllegalArgumentException("Both start and end points must be set!");
        final int inf = Integer.max(height, width) + 1;
        Map<Cell, Integer> cost = new HashMap<>(); // the cost of the cheapest path from start to a cell
        PriorityQueue<Cell> toVisit =
                new PriorityQueue<>(Comparator.comparingDouble(value -> getHeuristics(value) + cost.getOrDefault(value, inf)));
        toVisit.add(startCell);
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        cost.put(startCell, 0);
        while (!toVisit.isEmpty()) {
            Cell current = toVisit.poll();
            if (current.equals(endCell)) {
                LinkedList<Cell> path = new LinkedList<>();
                path.addFirst(current);
                while (cameFrom.containsKey(current)) {
                    current = cameFrom.get(current);
                    path.addFirst(current);
                }
                return path;
            }
            visited.add(current);
            Set<Cell> ns = getNeighbours(current);
            for (Cell neighbor : ns) {
                int score = cost.getOrDefault(current, inf) + 1;
                if (score < cost.getOrDefault(neighbor, inf) || !visited.contains(neighbor)) {
                    cameFrom.put(neighbor, current);
                    cost.put(neighbor, score);
                    if (!toVisit.contains(neighbor))
                        toVisit.add(neighbor);
                }

            }
        }
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
                sb.append(mazeGrid[j][i]);
            }
            sb.append(" *\n");
        }
        sb.append("* ".repeat(width + 2));
        return sb.toString();
    }
}
