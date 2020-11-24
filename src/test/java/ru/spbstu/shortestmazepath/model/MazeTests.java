package ru.spbstu.shortestmazepath.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static ru.spbstu.shortestmazepath.TestUtils.assertApproxEquals;

public class MazeTests {

    @Test
    public void neighboursTest() {
        Random random = new Random();
        for (int attempt = 0; attempt < 100; attempt++) {
            int width = random.nextInt(1000) + 3; // Adding 3 to avoid getting a degenerate case
            int height = random.nextInt(1000) + 3;
            Cell[][] cells = new Cell[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cells[i][j] = new Cell(i, j, Cell.Type.PATH);
                }
            }
            Maze noWalls = new Maze(height, width, null, null, cells);
            assertEquals(2, noWalls.getNeighbours(cells[0][0]).size());
            assertEquals(2, noWalls.getNeighbours(cells[width - 1][height - 1]).size());
            assertEquals(4, noWalls.getNeighbours(cells[width / 2][height / 2]).size());
        }

        for (int attempt = 0; attempt < 100; attempt++) {
            int width = random.nextInt(1000) + 3;
            int height = random.nextInt(1000) + 3;
            Cell[][] cells = new Cell[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cells[i][j] = new Cell(i, j, Cell.Type.WALL);
                }
            }
            Maze onlyWalls = new Maze(height, width, null, null, cells);
            assertEquals(0, onlyWalls.getNeighbours(cells[0][0]).size());
            assertEquals(0, onlyWalls.getNeighbours(cells[width - 1][height - 1]).size());
            assertEquals(0, onlyWalls.getNeighbours(cells[width / 2][height / 2]).size());
            assertEquals(0, onlyWalls.getNeighbours(cells[random.nextInt(width)][random.nextInt(height)]).size());
        }
    }

    @Test
    public void heuristicsTest() {
        Maze maze = Maze.random();
        Random random = new Random();
        Cell c1 = maze.getMazeGrid()[0][0];

        int randX = random.nextInt(maze.getWidth());
        int randY = random.nextInt(maze.getHeight());
        Cell c2 = maze.getMazeGrid()[randX][randY];

        assertApproxEquals(c1.distanceTo(maze.getEndCell()), maze.getHeuristics(c1), 1e-7);
        assertApproxEquals(c2.distanceTo(maze.getEndCell()), maze.getHeuristics(c2), 1e-7);
    }

    @Test
    public void equalsTest() {
        Maze m1 = Maze.random();
        Maze m2 = Maze.random();

        assertEquals(m1, m1);
        if (!m1.getStartCell().equals(m2.getStartCell()))
            assertNotEquals(m1, m2);
    }

    @Test
    public void simpleSolveTest() {
        Cell[][] cells = new Cell[2][2];
        cells[0][0] = new Cell(0, 0, Cell.Type.START);
        cells[1][0] = new Cell(1, 0, Cell.Type.END);
        cells[0][1] = new Cell(0, 1, Cell.Type.PATH);
        cells[1][1] = new Cell(1, 1, Cell.Type.PATH);
        Maze maze1 = new Maze(2, 2, cells[0][0], cells[1][0], cells);
        assertEquals(2, maze1.solve().size());
        assertIterableEquals(Arrays.asList(cells[0][0], cells[1][0]), maze1.solve());

        cells[1][0] = new Cell(1, 0, Cell.Type.PATH);
        cells[1][1] = new Cell(1, 1, Cell.Type.END);
        Maze maze2 = new Maze(2, 2, cells[0][0], cells[1][1], cells);
        assertEquals(3, maze2.solve().size());
        // (0, 1) because it firstly checks the right neighbour
        assertIterableEquals(Arrays.asList(cells[0][0], cells[0][1], cells[1][1]), maze2.solve());
    }

    @Test
    public void solveTests() throws IOException {
        Maze m1 = MazeManager.load(new File("examples/sample1.maze"));
        assertEquals(39, m1.solve().size());

        Maze m2 = MazeManager.load(new File("examples/sample2.maze"));
        assertEquals(53, m2.solve().size());

        Maze m3 = MazeManager.load(new File("examples/sample3.maze"));
        assertEquals(38, m3.solve().size());
    }
}
