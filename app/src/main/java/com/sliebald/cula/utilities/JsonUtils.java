package com.sliebald.cula.utilities;

import android.util.Log;

import com.sliebald.cula.data.database.Entities.QuoteEntry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class for parsing JSON objects retrieved from http://forismatic.com.
 */
public class JsonUtils {


    /**
     * Key for the text of a quote in the json.
     */
    private static final String TEXT_KEY = "quoteText";

    /**
     * Key for the author of a quote in the json.
     */
    private static final String AUTHOR_KEY = "quoteAuthor";


    /**
     * Gets the json representation of a single Quote and returns it as java object.
     *
     * @param json The json string of the {@link QuoteEntry}.
     * @return The {@link QuoteEntry} object created from the json string.
     */
    public static QuoteEntry parseQuoteJson(String json) {
        try {
            JSONObject jsonQuote = new JSONObject(json);
            String text = jsonQuote.getString(TEXT_KEY);
            String author = jsonQuote.getString(AUTHOR_KEY);
            return new QuoteEntry(text, author);
        } catch (JSONException exception) {
            Log.e(JsonUtils.class.getSimpleName(), "Error parsing the json String: " + exception
                    + " received json: " + json);
            return new QuoteEntry("Error loading the Quote from the Internet", "The Internet");
        }
    }

}
