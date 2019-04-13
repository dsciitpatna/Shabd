package com.dsciitp.shabd.signin;

import android.content.Context;

import com.dsciitp.shabd.database.WordsFromFirebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class SigninUtils {

    private static final String FILE_NAME = "shabd_data.json";

    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<WordsFromFirebase> extractJson(Context context) {

        List<WordsFromFirebase> newWords = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(loadJSONFromAsset(context));
            JSONArray words = object.getJSONArray("data");
            for (int i = 0; i < words.length(); i++) {
                JSONObject currentWord = words.getJSONObject(i);

                WordsFromFirebase word = new WordsFromFirebase();
                word.setTitle(currentWord.getString("title"));
                word.setDescription(currentWord.getString("description"));
                word.setHindiTitle(currentWord.getString("hindiTitle"));
                word.setId(currentWord.getInt("id"));
                word.setImageResource(currentWord.getString("imageResource"));
                word.setIsItTopic(currentWord.getInt("isItTopic"));
                word.setParentClass(currentWord.getString("parentClass"));

                newWords.add(word);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newWords;
    }


}
