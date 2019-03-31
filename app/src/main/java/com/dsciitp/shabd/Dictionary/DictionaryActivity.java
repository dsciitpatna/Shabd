package com.dsciitp.shabd.Dictionary;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dsciitp.shabd.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity implements DictionaryAdapter.OnCategorySelectedListener {

    EditText word;
    TextView meaning;
    ImageView del;
    TextToSpeech t1;
    View v;
    private static final String TTS_SPEAK_ID = "SPEAK";
    String base = "https://googledictionaryapi.eu-gb.mybluemix.net";
    final static String PARAM_QUERY = "define";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            getSupportActionBar().hide();
        RecyclerView rview = findViewById(R.id.topic_dict_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        rview.setLayoutManager(layoutManager);
        ArrayList mylist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.alphabets)));
        DictionaryAdapter madapter = new DictionaryAdapter(mylist, this, this);
        rview.setAdapter(madapter);
        v=findViewById(R.id.dictionary_main);
        word = findViewById(R.id.speak);
        del = findViewById(R.id.deld);
        meaning = findViewById(R.id.mean);
        ImageView search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Callback().execute(dictionaryEntries(word.getText().toString()));
            }
        });
        del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textString = word.getText().toString();
                if (textString.length() > 0) {
                    word.setText("");
                    word.setSelection(word.getText().length());
                }
                return false;
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textString = word.getText().toString();
                if (textString.length() > 0) {
                    word.setText(textString.substring(0, textString.length() - 1));
                    word.setSelection(word.getText().length());//position cursor at the end of the line
                }
            }
        });
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        FloatingActionButton fab1 = findViewById(R.id.playd);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(word.getText(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
            }
        });
    }
    private URL dictionaryEntries(String search) {
        String language = "en";
        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(PARAM_QUERY, search)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null)
            Log.d("uri", url.toString());
        return url;
    }
    @Override
    public void onTopicSelected(String title) {
        t1.speak(title, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
        word.append(title);
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


