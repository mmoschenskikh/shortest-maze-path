package ru.spbstu.shortestmazepath.model;

import ru.spbstu.shortestmazepath.controller.MazeController;

import java.util.*;

public class Maze {

    private static final int MIN_SIZE = MazeController.MIN_MAZE_SIZE;
    private static final int MAX_SIZE = MazeController.MAX_MAZE_SIZE;

    private final int height;
    private final int width;
    private final Cell startCell;
    private final Cell endCell;
    private final Cell[][] mazeGrid;

    public Maze(int height, int width, Cell startCell, Cell endCell, Cell[][] mazeGrid) {
        this.height = height;
        this.width = width;
        this.startCell = startCell;
        this.endCell = endCell;
        if (mazeGrid.length != width || mazeGrid[0].length != height)
            throw new IllegalArgumentException("Wrong height or width passed!");
        this.mazeGrid = mazeGrid;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell getStartCell() {
        return startCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Maze maze = (Maze) o;

        if (height != maze.height) return false;
        if (width != maze.width) return false;
        if (!Objects.equals(startCell, maze.startCell)) return false;
        if (!Objects.equals(endCell, maze.endCell)) return false;
        return Arrays.deepEquals(mazeGrid, maze.mazeGrid);
    }

    @Override
    public int hashCode() {
        int result = height;
        result = 31 * result + width;
        result = 31 * result + (startCell != null ? startCell.hashCode() : 0);
        result = 31 * result + (endCell != null ? endCell.hashCode() : 0);
        result = 31 * result + Arrays.deepHashCode(mazeGrid);
        return result;
    }

    public Cell getEndCell() {
        return endCell;
    }

    public Cell[][] getMazeGrid() {
        return mazeGrid;
    }

    /**
     * Finds all the neighbours of the specified cell.
     *
     * @param cell a cell to find neighbours of.
     * @return a list of neighbour cells.
     */
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

    /**
     * Computes the estimated cost of the cheapest path from the cell to the goal.
     *
     * @param cell a cell to estimate the cost.
     * @return estimated distance to the goal.
     */
    public double getHeuristics(Cell cell) {
        return cell.distanceTo(endCell);
    }

    /**
     * Finds the shortest path from the start point to the end point using the A* algorithm.
     *
     * @return a list containing all the cells included in the path.
     * @throws IllegalArgumentException if the shortest path cannot be found.
     */
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

    /**
     * Creates a random maze.
     *
     * @return randomly generated maze.
     */
    public static Maze random() {
        Random random = new Random();
        int height = Math.max(random.nextInt(MAX_SIZE), MIN_SIZE);
        int width = Math.max(random.nextInt(MAX_SIZE), MIN_SIZE);

        Cell start = new Cell(random.nextInt(width), random.nextInt(height), Cell.Type.START);

        int endX = random.nextInt(width);
        while (endX == start.x)
            endX = random.nextInt(width);

        Cell end = new Cell(endX, random.nextInt(height), Cell.Type.END);

        Cell[][] grid = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell.Type type;
                if (start.x == i && start.y == j)
                    type = Cell.Type.START;
                else if (end.x == i && end.y == j)
                    type = Cell.Type.END;
                else
                    type = (random.nextBoolean()) ? Cell.Type.WALL : Cell.Type.PATH;
                grid[i][j] = new Cell(i, j, type);
            }
        }
        return new Maze(height, width, start, end, grid);
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
