/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sliebald.cula.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilityclass for simple network REST API requests
 */
public class NetworkUtils {

    private final static String QUOTE_BASE_URL =
            "http://api.forismatic.com/api/1.0/";

    private final static String PARAM_QUERY = "method";
    private final static String PARAM_QUERY_VALUE = "getQuote";

    private final static String PARAM_FORMAT = "format";
    private final static String PARAM_FORMAT_VALUE = "json";

    private final static String PARAM_LANG = "lang";
    private final static String PARAM_LANG_VALUE = "en";


    /**
     * Builds the URL used to query forismatic.com for the quote of the day.
     *
     * @return The URL to use to request the Quote of the day.
     */
    public static URL getQuoteOfTheDayUrl() {
        Uri builtUri = Uri.parse(QUOTE_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, PARAM_QUERY_VALUE)
                .appendQueryParameter(PARAM_FORMAT, PARAM_FORMAT_VALUE)
                .appendQueryParameter(PARAM_LANG, PARAM_LANG_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}