package com.dsciitp.shabd;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.dsciitp.shabd.Home.TopicModel;
import com.dsciitp.shabd.Learn.LearnActivity;
import com.dsciitp.shabd.QuickActions.QuickActionFragment;
import com.dsciitp.shabd.Setting.SettingFragment;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements HomeRecyclerAdapter.OnCategorySelectedListener,
        CategoryFragment.OnFragmentInteractionListener, BasicRecyclerAdapter.OnSubCategorySelectedListener {

    TextToSpeech tts;
    EditText speakbar;
    ImageView play;
    ImageView del;
    RelativeLayout topbar;
    Resources res;

    private Fragment activeFragment;

    private static final String TTS_SPEAK_ID = "SPEAK";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showTopBar();
                    updateFragment(new HomeFragment(), 0);
                    return true;
                case R.id.navigation_quick:
                    showTopBar();
                    updateFragment(new QuickActionFragment(), 1);
                    return true;
                case R.id.navigation_dictionary:
                    startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
                    return true;
                case R.id.navigation_settings:
                    hideTopBar();
                    updateFragment(new SettingFragment(), 1);
                    return true;
                case R.id.navigation_learn:
                    startActivity(new Intent(MainActivity.this, LearnActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        setBaseFragment(savedInstanceState);
        initSpeakBar();
        res = getResources();

    }

    private void setBaseFragment(Bundle savedInstanceState) {

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            HomeFragment firstFragment = new HomeFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
            activeFragment = firstFragment;
        }

    }

    private void initSpeakBar() {
        speakbar = findViewById(R.id.speak);
        play = findViewById(R.id.play);
        del = findViewById(R.id.del);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(0.8f);

                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = speakbar.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
            }
        });
        del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textString = speakbar.getText().toString();
                if (textString.length() > 0) {
                    speakbar.setText("");
                    speakbar.setSelection(speakbar.getText().length());
                }
                return false;
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textString = speakbar.getText().toString();
                if (textString.length() > 0) {
                    speakbar.setText(textString.substring(0, textString.length() - 1));
                    speakbar.setSelection(speakbar.getText().length());//position cursor at the end of the line
                }
            }
        });
    }

    @Override
    public void onTopicSelected(String title) {
        if (res.getIdentifier(title + "_array", "array", getPackageName()) != 0) {
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            BasicFragment basicFragment = BasicFragment.newInstance(title);
            transactFragment(basicFragment);
        } else {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSubTopicSelected(final TopicModel model, final View view) {
        Toast.makeText(this, model.getTitle(), Toast.LENGTH_SHORT).show();

        final float originalX = view.getX();
        final float originalY = view.getY();

        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        if (res.getIdentifier(model.getReturnText() + "_array", "array", getPackageName()) != 0) {
            BasicFragment basicFragment = BasicFragment.newInstance(model.getReturnText());
            transactFragment(basicFragment);
        } else {
            tts.speak(model.getTitle(), TextToSpeech.QUEUE_FLUSH, null, TTS_SPEAK_ID);
            view.setClickable(false);
            view.animate().x(size.x / 3f).y(size.y / 3f).translationZBy(10f).scaleXBy(1.25f).scaleYBy(1.25f).setDuration(750).withEndAction(new Runnable() {
                @Override
                public void run() {
                    view.animate().x(originalX).y(originalY).translationZBy(-10f).scaleXBy(-1.25f).scaleYBy(-1.25f).setDuration(1000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                        }
                    });
                }
            });
            speakbar.append(model.getTitle() + " ");
        }
    }

    private void transactFragment(Fragment frag) {
        activeFragment = frag;
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                .replace(R.id.fragment_container, frag, frag.getTag())
                .addToBackStack(frag.getTag())
                .commit();
    }

    private void updateFragment(Fragment fragment, int bStack) {
        activeFragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        manager.popBackStackImmediate(1, 1);

        if (bStack == 1) {
            transaction.addToBackStack(fragment.getTag());
        } else if (bStack == 0) {
            manager.popBackStackImmediate();
        }
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        speakbar.setText("");
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
        if (activeFragment instanceof SettingFragment) {
            showTopBar();
        }
        super.onBackPressed();
    }

    private void hideTopBar() {
        if (topbar == null) topbar = findViewById(R.id.bar);
        if (topbar.getVisibility() == View.VISIBLE) {
            topbar.setVisibility(View.GONE);
        }
    }

    private void showTopBar() {
        if (topbar == null) topbar = findViewById(R.id.bar);
        if (topbar.getVisibility() == View.GONE) {
            topbar.setVisibility(View.VISIBLE);
        }
    }
}
