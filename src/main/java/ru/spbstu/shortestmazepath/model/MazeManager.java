package ru.spbstu.shortestmazepath.model;

import ru.spbstu.shortestmazepath.controller.MazeController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MazeManager {

    private static final int MIN_SIZE = MazeController.MIN_MAZE_SIZE;
    private static final int MAX_SIZE = MazeController.MAX_MAZE_SIZE;

    public static void save(Maze maze, File outputFile) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(maze.getHeight() + "x" + maze.getWidth());
        for (int i = 0; i < maze.getHeight(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maze.getWidth(); j++) {
                sb.append(maze.getMazeGrid()[j][i].type.toString());
            }
            lines.add(sb.toString());
        }
        Files.write(outputFile.toPath(), lines);
    }

    public static Maze load(File inputFile) throws IOException {
        List<String> lines = Files.readAllLines(inputFile.toPath());
        String size = lines.get(0);
        lines.remove(0);
        if (!size.matches("\\d+x\\d+"))
            throw new IllegalArgumentException("Maze size is not specified");
        final int height = Integer.parseInt(size.split("x")[0]);
        final int width = Integer.parseInt(size.split("x")[1]);
        if (height < MIN_SIZE || height > MAX_SIZE || width < MIN_SIZE || width > MAX_SIZE)
            throw new IllegalArgumentException("Wrong maze size is specified. Minimal size is " + MIN_SIZE + ", maximum size is " + MAX_SIZE);
        if (!lines.stream().allMatch(s -> s.length() == width))
            throw new IllegalArgumentException("Maze does not match the specified size");
        Cell startCell = null;
        Cell endCell = null;
        Cell[][] maze = new Cell[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell.Type t;
                char current;
                try {
                    current = lines.get(i).charAt(j);
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Maze does not match the specified size");
                }
                switch (current) {
                    case 'S':
                        t = Cell.Type.START;
                        break;
                    case 'E':
                        t = Cell.Type.END;
                        break;
                    case '#':
                        t = Cell.Type.WALL;
                        break;
                    case '.':
                        t = Cell.Type.PATH;
                        break;
                    default:
                        throw new IllegalArgumentException("Incorrect symbol at (" + j + ", " + i + "): " + current);
                }
                Cell c = new Cell(j, i, t);
                if (t == Cell.Type.START) {
                    if (startCell != null) throw new IllegalArgumentException("More than one start cell specified");
                    startCell = c;
                } else if (t == Cell.Type.END) {
                    if (endCell != null) throw new IllegalArgumentException("More than one end cell specified");
                    endCell = c;
                }
                maze[j][i] = c;
            }
        }
        return new Maze(height, width, startCell, endCell, maze);
    }
}
