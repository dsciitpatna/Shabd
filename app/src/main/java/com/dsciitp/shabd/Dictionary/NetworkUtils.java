package com.dsciitp.shabd.Dictionary;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String base = "https://googledictionaryapi.eu-gb.mybluemix.net";
    final static String PARAM_QUERY = "define";

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        /*urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(2000);*/
        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        try {
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

    public static String dictionaryEntries(String search) {
        String language = "en";
        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(PARAM_QUERY, search.toLowerCase())
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) return null;

        Log.d("uri", url.toString());

        try {
            String json = getResponseFromHttpUrl(url);

            if (json != null) {
                return extractJson(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extractJson(String string) {

        int i;
        StringBuilder def = new StringBuilder("");

        //meaning.setText(result);
        try {
            JSONObject js = new JSONObject(string);

            def.append(js.getString("word"));
            //def.append("/n" + js.getJSONArray("meaning"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return def.toString();
    }

    }

