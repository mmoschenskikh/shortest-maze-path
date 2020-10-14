package ru.spbstu.shortestmazepath.util;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * A helper class for 'strings' file access.
 */
public class StringsSupplier {
    private static ResourceBundle strings;

    public static ResourceBundle getStrings() throws IOException {
        if (strings == null)
            strings = new PropertyResourceBundle(StringsSupplier.class.getResourceAsStream("strings"));
        return strings;
    }
}
