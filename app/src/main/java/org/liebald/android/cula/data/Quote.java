package org.liebald.android.cula.data;

/**
 * POJO object to store a quote.
 */
public class Quote {

    /**
     * The text of the quote.
     */
    private final String text;

    /**
     * Constructor for a new quote.
     *
     * @param text Text as String.
     */
    public Quote(String text) {
        this.text = text;
    }

    /**
     * Returns the text of the quote.
     *
     * @return The text as String.
     */
    public String getText() {
        return text;
    }

}
