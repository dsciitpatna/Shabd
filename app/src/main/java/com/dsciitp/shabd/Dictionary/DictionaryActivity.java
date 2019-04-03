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

public class DictionaryActivity extends AppCompatActivity implements DictionaryAdapter.OnCategorySelectedListener, DictionaryFragment.OnCategorySelectedListener {

    EditText word;
    ImageView del;
    TextToSpeech t1;
    View v;
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
        search.setImageResource(R.drawable.ic_search_black_24dp);
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
        v = findViewById(R.id.dictionary_main);
        fab1 = findViewById(R.id.playd);
        final FloatingActionButton search = findViewById(R.id.search);
//        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        final float screenx = size.x;
//        final float screeny = size.y;
        final float originalx = ((View)search).getX();
        final float originaly = ((View)search).getY();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setImageResource(R.color.colorPrimary);
                search.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                search.animate().x(350f).y(250f).scaleX(6f).scaleY(6f).setDuration(1200).translationZBy(25f).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        ((View) fab1).setVisibility(View.INVISIBLE);
                        ((View) search).setVisibility(View.INVISIBLE);
                        onDictionarySelected();
                        search.animate().x(originalx).y(originaly).scaleX(1f).scaleY(1f).setDuration(1200).translationZBy(-25f);
                    }
                });
                //new DictionaryActivity().Callback().execute(dictionaryEntries(word.getText().toString()));
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

    @Override
    public void onDictionarySelected() {
//        NetworkUtils.dictionaryEntries(word.getText().toString());
        transactFragment(MeaningFragment.newInstance(word.getText().toString()));
    }
}



