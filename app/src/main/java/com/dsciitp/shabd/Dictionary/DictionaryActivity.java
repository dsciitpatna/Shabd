package com.dsciitp.shabd.Dictionary;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.dsciitp.shabd.R;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity implements DictionaryAdapter.OnCategorySelectedListener {

    EditText word;
    ImageView del;
    TextToSpeech t1;
    FloatingActionButton fab1, search;
    private static final String TTS_SPEAK_ID = "SPEAK";
    String press = "Press me to know the meaning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            getSupportActionBar().hide();
        del = findViewById(R.id.deld);
        word = findViewById(R.id.speak);
        search = findViewById(R.id.search);
        setBaseFragment(savedInstanceState);
        fab1 = findViewById(R.id.playd);
        onclicksearch();
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
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(word.getText(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
                search.animate().scaleX(2f).scaleY(2f).setDuration(1000).translationZBy(25f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        search.animate().scaleX(1f).scaleY(1f).setDuration(1000).translationZBy(-25f).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                t1.speak(press, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
                                search.animate().scaleX(2f).scaleY(2f).setDuration(1000).translationZBy(+25f).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        search.animate().scaleX(1f).scaleY(1f).translationZBy(25f).setDuration(1000);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void setBaseFragment(Bundle savedInstanceState) {

        if (findViewById(R.id.frame_dictionary) != null) {

            if (savedInstanceState != null) {
                return;
            }
            DictionaryFragment firstFragment = new DictionaryFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_dictionary, firstFragment).commit();
        }
    }

    private void transactFragment(Fragment frag) {
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.frame_dictionary, frag, frag.getTag())
                .addToBackStack(frag.getTag())
                .commit();
    }


    @Override
    public void onTopicSelected(String title) {
        t1.speak(title, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
        word.append(title);
    }

    public void onDictionarySelected() {
//        NetworkUtils.dictionaryEntries(word.getText().toString());
        MeaningFragment fragment = MeaningFragment.newInstance(word.getText().toString());
        transactFragment(fragment);
        final View view = this.getWindow().getDecorView();

        view.setBackgroundColor(getResources().getColor(R.color.searchBackground));
        search.setBackgroundColor(getResources().getColor(R.color.floatingButton));
        fab1.setBackgroundColor(getResources().getColor(R.color.floatingButton));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DictionaryActivity.super.onBackPressed();
                search.setBackgroundColor(getResources().getColor(R.color.searchBackground));
                fab1.setBackgroundColor(getResources().getColor(R.color.searchBackground));
                view.setBackgroundColor(getResources().getColor(R.color.meaningBackground));
                onclicksearch();
            }
        });

    }

    private void onclicksearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                search.setImageResource(R.color.colorPrimary);
                search.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                v.animate().x(350f).y(250f).scaleX(60f).scaleY(60f).setDuration(800).translationZBy(25f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().translationX(0f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(2).translationZBy(-25f).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                search.setImageResource(R.drawable.ic_search_black_24dp);
                            }
                        });
                        onDictionarySelected();
                    }
                });
            }
        });
    }
}



