package ru.spbstu.shortestmazepath.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MazeManagerTests {

    @Test
    public void saveLoadTest() throws IOException {
        for (int i = 0; i < 100; i++) {
            Maze m = Maze.random();
            File tempFile = new File("temp.maze");
            MazeManager.save(m, tempFile);
            Maze mLoaded = MazeManager.load(tempFile);
            assertEquals(m, mLoaded);
            tempFile.delete();
        }
    }
}
