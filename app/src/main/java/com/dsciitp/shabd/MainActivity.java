package com.dsciitp.shabd;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dsciitp.shabd.BasicTopic.BasicFragment;
import com.dsciitp.shabd.BasicTopic.BasicRecyclerAdapter;
import com.dsciitp.shabd.Category.CategoryFragment;
import com.dsciitp.shabd.Dictionary.DictionaryActivity;
import com.dsciitp.shabd.Home.HomeFragment;
import com.dsciitp.shabd.Home.HomeRecyclerAdapter;
import com.dsciitp.shabd.Learn.LearnActivity;
import com.dsciitp.shabd.QuickActions.QuickActionFragment;
import com.dsciitp.shabd.Setting.SettingFragment;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;


public class MainActivity extends AppCompatActivity implements HomeRecyclerAdapter.OnCategorySelectedListener,
        CategoryFragment.OnOnlineWordSelectedListener, BasicRecyclerAdapter.OnSubCategorySelectedListener {

    TextToSpeech tts;
    EditText speak;
    ImageView play;
    ImageView del;
    Toolbar topBar;
    Resources res;
    Point size;
    Realm realm;
    RelativeLayout speakBar;

    private List<Fragment> activeFragment = new ArrayList<>();

    private static final String TTS_SPEAK_ID = "SPEAK";
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showTopBar();
                    speakBar.setVisibility( View.VISIBLE );
                    updateFragment( new HomeFragment(), 0 );
                    return true;
                case R.id.navigation_quick:
                    showTopBar();
                    speakBar.setVisibility( View.VISIBLE );
                    transactFragment( new QuickActionFragment() );
                    return true;
                case R.id.navigation_dictionary:
                    startActivity( new Intent( MainActivity.this, DictionaryActivity.class ) );
                    return true;
                case R.id.navigation_settings:
                    hideTopBar();
                    transactFragment( new SettingFragment() );
                    return true;
                case R.id.navigation_learn:
                    startActivity( new Intent( MainActivity.this, LearnActivity.class ) );
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setLocale();
        speakBar = findViewById( R.id.speakbar );
        navigation = findViewById( R.id.navigation );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );
        navigation.setSelectedItemId( R.id.navigation_home );

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion( 2 )
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance( config );

        speakBar.setVisibility( View.INVISIBLE );
        setBaseFragment( savedInstanceState );
        initSpeakBar();

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize( size );
    }


    private void setBaseFragment(Bundle savedInstanceState) {

        if (findViewById( R.id.fragment_container ) != null) {

            if (savedInstanceState != null) {
                return;
            }
            HomeFragment firstFragment = new HomeFragment();
            firstFragment.setArguments( getIntent().getExtras() );
            getSupportFragmentManager().beginTransaction()
                    .add( R.id.fragment_container, firstFragment ).commit();
            activeFragment.add( firstFragment );
        }

    }

    private void setLocale() {
        res = getResources();
        String deviceLocale = Locale.getDefault().getLanguage();

        if (!(deviceLocale.equals( "en" ) || deviceLocale.equals( "hi" ))) {
            Locale locale = new Locale( "en" );
            Locale.setDefault( locale );
            Configuration config = new Configuration();
            config.locale = locale;
            res.updateConfiguration( config, null );
        }
    }

    private void initSpeakBar() {
        speak = findViewById( R.id.speak );
        play = findViewById( R.id.play );
        del = findViewById( R.id.del );


        tts = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage( new Locale( Locale.getDefault().getLanguage() ) );

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e( "TTS", "This Language is not supported" );
                    } else if (result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e( "TTS", "This Language is missing data" );
                    }
                    tts.setPitch( 1.0f );
                    tts.setSpeechRate( 0.8f );

                } else {
                    Log.e( "TTS", "Initialization Failed!" );
                }
            }
        } );
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = speak.getText().toString();
                tts.speak( toSpeak, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
            }
        } );
        del.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textString = speak.getText().toString();
                if (textString.length() > 0) {
                    speak.setText( "" );
                    speak.setSelection( speak.getText().length() );
                }
                return false;
            }
        } );
        del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textString = speak.getText().toString();
                if (textString.length() > 0) {
                    speak.setText( textString.substring( 0, textString.length() - 1 ) );
                    speak.setSelection( speak.getText().length() );//position cursor at the end of the line
                }
            }
        } );
    }

    @Override
    public void onTopicSelected(int id) {
        Toast.makeText( this, String.valueOf( id ), Toast.LENGTH_SHORT ).show();

        if (id != (-1)) {
            RealmQuery<WordsInRealm> query = realm.where( WordsInRealm.class );
            query.equalTo( "id", id );
            WordsInRealm result = query.findFirst();

            realm.beginTransaction();
            tts.speak( result.getTitle(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );

            if (Objects.requireNonNull( result ).getIsItTopic() == 1) {
                Toast.makeText( this, result.getTitle(), Toast.LENGTH_SHORT ).show();

                BasicFragment basicFragment = BasicFragment.newInstance( result.getTitle() );
                transactFragment( basicFragment );
            }
            realm.commitTransaction();
        } else {
            tts.speak( "holi", TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
            CategoryFragment categoryFragment = CategoryFragment.newInstance( "holi" );
            transactFragment( categoryFragment );
        }
    }

    @Override
    public void onSubTopicSelected(int id, View view) {
        Toast.makeText( this, String.valueOf( id ), Toast.LENGTH_SHORT ).show();

        RealmQuery<WordsInRealm> query = realm.where( WordsInRealm.class );
        query.equalTo( "id", id );
        WordsInRealm result = query.findFirst();

        realm.beginTransaction();
        tts.speak( result.getTitle(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );

        if (result.getIsItTopic() == 1) {
            BasicFragment basicFragment = BasicFragment.newInstance( result.getTitle() );
            transactFragment( basicFragment );
        } else {
            tts.speak( result.getTitle(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
            showWordAnimation( view );
            speak.append( result.getTitle() + " " );
        }
        realm.commitTransaction();
    }

    @Override
    public void onOnlineWordSelected(String text, View view) {
        tts.speak( text, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID );
        showWordAnimation( view );
        speak.append( text + " " );
    }

    private void showWordAnimation(final View view) {
        view.setEnabled( false );
        view.animate().x( size.x / 3f ).y( size.y / 4f ).translationZBy( 10f ).scaleXBy( 1.25f ).scaleYBy( 1.25f ).setDuration( 750 ).withEndAction( new Runnable() {
            @Override
            public void run() {
                view.animate().translationX( 0f ).translationY( 0f ).translationZBy( -10f ).scaleXBy( -1.25f ).scaleYBy( -1.25f ).setDuration( 1000 ).setStartDelay( 500 ).withEndAction( new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled( true );
                    }
                } );
            }
        } );
    }

    private void transactFragment(Fragment frag) {
        activeFragment.add( frag );
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace( R.id.fragment_container, frag, frag.getTag() )
                .addToBackStack( frag.getTag() )
                .commit();
        if (frag instanceof BasicFragment || frag instanceof QuickActionFragment || frag instanceof CategoryFragment) {
            speakBar.setVisibility( View.VISIBLE );
        } else {
            speakBar.setVisibility( View.INVISIBLE );
        }

    }

    private void updateFragment(Fragment fragment, int bStack) {
        activeFragment.add( fragment );
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace( R.id.fragment_container, fragment );
        manager.popBackStackImmediate( 1, 1 );

        if (bStack == 1) {
            transaction.addToBackStack( fragment.getTag() );
        } else if (bStack == 0) {
            manager.popBackStackImmediate();
        }
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        speak.setText( "" );
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
        if(activeFragment.get(activeFragment.size()-1)instanceof CategoryFragment)
        {
            activeFragment.remove( activeFragment.size() - 1 );
            super.onBackPressed();
            navigation.setSelectedItemId( R.id.navigation_home );
        }
        else if (activeFragment.get( activeFragment.size() - 1 ) instanceof QuickActionFragment) {

            if (activeFragment.get( activeFragment.size() - 2 ) instanceof SettingFragment) {
                hideTopBar();
            }
            activeFragment.remove( activeFragment.size() - 1 );
            super.onBackPressed();
            navigation.setSelectedItemId( R.id.navigation_home );
        } else if (activeFragment.get( activeFragment.size() - 1 ) instanceof SettingFragment) {
            showTopBar();

            if (activeFragment.size() > 1 && activeFragment.get( activeFragment.size() - 2 ) instanceof BasicFragment) {
                speakBar.setVisibility( View.VISIBLE );
            }
            activeFragment.remove( activeFragment.size() - 1 );
            super.onBackPressed();
            navigation.setSelectedItemId( R.id.navigation_home );

        } else if (activeFragment.get( activeFragment.size() - 1 ) instanceof BasicFragment) {
            if (activeFragment.size() > 1 && activeFragment.get( activeFragment.size() - 2 ) instanceof HomeFragment) {
                //speakBar.setVisibility( View.INVISIBLE );
            }
            activeFragment.remove( activeFragment.size() - 1 );
            super.onBackPressed();
        }
        else
            super.onBackPressed();

    }

    @Override
    protected void onResume() {
        navigation.setSelectedItemId( R.id.navigation_home );
        super.onResume();
    }

    private void hideTopBar() {
        if (topBar == null) topBar = findViewById( R.id.bar );
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setVisibility( View.GONE );
        }
    }

    private void showTopBar() {
        if (topBar == null)
            topBar = findViewById( R.id.bar );
        if (topBar.getVisibility() == View.GONE) {
            topBar.setVisibility( View.VISIBLE );
        }

    }
}
