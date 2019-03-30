package com.dsciitp.shabd.Dictionary;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsciitp.shabd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class DictionaryActivity extends AppCompatActivity {

    EditText word;
    TextView meaning;
    String results;
    String base = "https://googledictionaryapi.eu-gb.mybluemix.net";
    final static String PARAM_QUERY = "define";
    private RecyclerView rview;
    private RecyclerView.LayoutManager layoutManager;
    private DictionaryAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            getSupportActionBar().hide();
        rview = findViewById(R.id.topic_dict_recycler_view);
        layoutManager = new GridLayoutManager(this, 3);
        rview.setLayoutManager(layoutManager);
        ArrayList mylist  = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.alphabets)));
        madapter = new DictionaryAdapter(mylist,this);

        rview.setAdapter(madapter);
        word = findViewById(R.id.speak);
        meaning = findViewById(R.id.mean);
        ImageView search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Callback().execute(dictionaryEntries(word.getText().toString()));
            }
        });

    }

    private URL dictionaryEntries(String search) {
        String language = "en";
        //word id is case sensitive and lowercase is required
        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(PARAM_QUERY, search)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("uri", url.toString());
        return url;
    }



    public class Callback extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String Results = null;
            try {
                Results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Results;
        }

        @Override
        protected void onPostExecute(String result) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
            int i;
            StringBuilder def = new StringBuilder("");
            meaning.setVisibility(View.VISIBLE);
            //meaning.setText(result);
            super.onPostExecute(result);
            try {
                JSONObject js = new JSONObject(result);
                def.append(js.getString("word"));
                //def.append("/n" + js.getJSONArray("meaning"));
                meaning.setText(def);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}


