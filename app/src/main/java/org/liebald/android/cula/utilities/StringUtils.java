package org.liebald.android.cula.utilities;

/**
 * Helperclass for string manipulation.
 */
public class StringUtils {

    /**
     * Returns the given {@link String} with the first letter set to upper case, rest lowercase.
     *
     * @param input The original {@link String}.
     * @return The updated {@link String}.
     */
    public static String toFirstCharacterUpperCase(String input) {
        if (input == null || input.isEmpty())
            return input;
        if (input.length() == 1)
            return input.toUpperCase();
        else
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
