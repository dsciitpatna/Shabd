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
import android.widget.Toast;

import com.dsciitp.shabd.R;

import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity implements DictionaryAdapter.OnCategorySelectedListener, MeaningFragment.OnMeaningPass {

    EditText word;
    ImageView del, play;
    TextToSpeech tts;
    FloatingActionButton search;
    private static final String TTS_SPEAK_ID = "SPEAK";
    Fragment activeFragment;
    String press = "Press me to know the meaning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dictionary );
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            getSupportActionBar().hide();
        del = findViewById( R.id.deld );
        word = findViewById( R.id.speak );
        search = findViewById( R.id.search );
        setBaseFragment( savedInstanceState );
        play = findViewById( R.id.playd );
        onclicksearch();
        del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textString = word.getText().toString();
                if (textString.length() > 0) {
                    word.setText( textString.substring( 0, textString.length() - 1 ) );
                    word.setSelection( word.getText().length() );//position cursor at the end of the line
                }
            }
        } );
        del.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textString = word.getText().toString();
                if (textString.length() > 0) {
                    word.setText( "" );
                    word.setSelection( word.getText().length() );
                }
                return false;
            }
        } );
        //Locale current = getResources().getConfiguration().locale;
        tts = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage( Locale.US );
                }
            }
        } );

        tts.setPitch( 1f );
        tts.setSpeechRate( 0.9f );
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!word.getText().toString().equals( "" )) {
                    tts.speak( word.getText(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
                    search.animate().scaleX( 2f ).scaleY( 2f ).setDuration( 1000 ).translationZBy( 25f ).withEndAction( new Runnable() {
                        @Override
                        public void run() {
                            search.animate().scaleX( 1f ).scaleY( 1f ).setDuration( 1000 ).translationZBy( -25f ).withEndAction( new Runnable() {
                                @Override
                                public void run() {
                                    if (word.getText().toString().length() < 15)
                                        tts.speak( press, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
                                    search.animate().scaleX( 2f ).scaleY( 2f ).setDuration( 1000 ).translationZBy( +25f ).withEndAction( new Runnable() {
                                        @Override
                                        public void run() {
                                            search.animate().scaleX( 1f ).scaleY( 1f ).translationZBy( 25f ).setDuration( 1000 );
                                        }
                                    } );
                                }
                            } );
                        }
                    } );
                }
            }
        } );
    }

    private void setBaseFragment(Bundle savedInstanceState) {

        if (findViewById( R.id.frame_dictionary ) != null) {

            if (savedInstanceState != null) {
                return;
            }
            DictionaryFragment firstFragment = new DictionaryFragment();
            firstFragment.setArguments( getIntent().getExtras() );
            getSupportFragmentManager().beginTransaction()
                    .add( R.id.frame_dictionary, firstFragment ).commit();
        }
    }

    private void transactFragment(Fragment frag) {
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace( R.id.frame_dictionary, frag, frag.getTag() )
                .addToBackStack( frag.getTag() )
                .commit();
    }


    @Override
    public void onTopicSelected(String title) {
        tts.speak( title, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
        word.append( title );

    }

    public void onDictionarySelected() {
//        NetworkUtils.dictionaryEntries(word.getText().toString());
        MeaningFragment fragment = MeaningFragment.newInstance( word.getText().toString() );
        transactFragment( fragment );
        final View view = this.getWindow().getDecorView();
        activeFragment = fragment;
//        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        search.setBackgroundColor( getResources().getColor( R.color.searchBackground ) );
        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DictionaryActivity.super.onBackPressed();
                onclicksearch();
            }
        } );

    }

    private void onclicksearch() {
        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (word.getText().toString().equals( "" )) {
                    Toast.makeText( DictionaryActivity.this, "Type a word to search", Toast.LENGTH_SHORT ).show();
                    tts.speak( "Type a word to search", TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
                } else {
                    search.setImageResource( R.color.searchBackground );
                    search.setBackgroundColor( getResources().getColor( R.color.searchBackground ) );
                    v.animate().x( 350f ).y( 250f ).scaleX( 40f ).scaleY( 40f ).setDuration( 500 ).translationZBy( 25f ).withEndAction( new Runnable() {
                        @Override
                        public void run() {
                            v.animate().translationX( 0f ).translationY( 0f ).scaleX( 1f ).scaleY( 1f ).setDuration( 300 ).translationZBy( -25f ).withEndAction( new Runnable() {
                                @Override
                                public void run() {
                                    search.setImageResource( R.drawable.ic_search_black_24dp );
                                }
                            } );
                            onDictionarySelected();
                        }
                    } );
                }
            }
        } );

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (activeFragment instanceof MeaningFragment) {
            onclicksearch();
        }
        super.onBackPressed();
    }

    @Override
    public void onMeaningPass(String data) {
        tts.setSpeechRate( 0.8f );
        tts.speak( data, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
    }
}



