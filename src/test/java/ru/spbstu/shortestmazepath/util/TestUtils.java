package ru.spbstu.shortestmazepath.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    private static boolean approxEquals(double expected, double actual, double delta) {
        return Math.abs(expected - actual) <= delta;
    }

    public static void assertApproxEquals(double expected, double actual, double delta) {
        assertTrue(approxEquals(expected, actual, delta));
    }
}
