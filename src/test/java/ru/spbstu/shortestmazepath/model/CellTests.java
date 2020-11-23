package ru.spbstu.shortestmazepath.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTests {

    @Test
    public void equalsTest() {
        Cell c1 = new Cell(0, 0, Cell.Type.START);
        Cell c2 = new Cell(13, 37, Cell.Type.WALL);
        Cell c3 = new Cell(0, 0, Cell.Type.END);
        Cell c4 = new Cell(13, 37, Cell.Type.WALL);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c2);
        assertEquals(c2, c4);
        assertEquals(c2, c2);
    }

    @Test
    public void distanceTest() {
        Cell c1 = new Cell(0, 0, Cell.Type.WALL);
        Cell c2 = new Cell(0, 1, Cell.Type.WALL);
        Cell c3 = new Cell(1, 1, Cell.Type.START);
        assertApproxEquals(0, c1.distanceTo(c1), 1e-7);
        assertApproxEquals(c2.distanceTo(c1), c1.distanceTo(c2), 1e-7);
        assertApproxEquals(1, c1.distanceTo(c2), 1e-7);
        assertApproxEquals(Math.sqrt(2), c1.distanceTo(c3), 1e-7);
    }

    private static boolean approxEquals(double expected, double actual, double delta) {
        return Math.abs(expected - actual) <= delta;
    }

    private static void assertApproxEquals(double expected, double actual, double delta) {
        assertTrue(approxEquals(expected, actual, delta));
    }
}
