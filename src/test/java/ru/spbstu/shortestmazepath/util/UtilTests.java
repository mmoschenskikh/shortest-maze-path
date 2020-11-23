package ru.spbstu.shortestmazepath.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTests {

    @Test
    public void stringsSupplierTest() throws IOException {
        ResourceBundle rb = StringsSupplier.getStrings();
        assertTrue(rb.containsKey("title"));
        assertEquals("Successfully reset", rb.getString("resetOk"));
    }
}
