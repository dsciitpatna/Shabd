package com.dsciitp.shabd.Learn.ColorGame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dsciitp.shabd.Learn.Video.VideoAdapter;
import com.dsciitp.shabd.R;

import java.util.ArrayList;
import java.util.Random;

public class ColorGameActivity extends AppCompatActivity implements ColorAdapter.OnCategorySelectedListener {
    private ArrayList<Integer> colours;
    private ColorAdapter colorAdapter;
    private RecyclerView colorRecycler;
    private int d, span;
    SharedPreferences preferences;

    int totalQuestions;
    int correctQuestions;

    TextView pointsTextView;
    TextView resultTextView;
    TextView timeTextView;
    Button playAgainButton;
    MediaPlayer mediaPlayer,mediaPlayer1;
    CountDownTimer timer;

    int highScore;
    TextView highScoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_color_game );
        mediaPlayer = MediaPlayer.create( this, R.raw.clock );
        mediaPlayer1=MediaPlayer.create( this,R.raw.applause );
        getSupportActionBar().setElevation( 0f );
        getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable( R.drawable.ic_arrow_back_white_24dp ) );
        getSupportActionBar().setTitle( getString( R.string.quiz ) );
        preferences = PreferenceManager.getDefaultSharedPreferences( this );
        highScore = preferences.getInt( "high_score_color", 0 );
        span = preferences.getInt( "span", 2 );
        preferences.edit().putInt( "span", 2 ).apply();
        highScoreView = findViewById( R.id.tv_high_score1 );
        highScoreView.setText( "HighScore = " + String.valueOf( preferences.getInt( "high_score_color", 0 ) ) );
        d = preferences.getInt( "correctanswer", 0 );
        pointsTextView = findViewById( R.id.tv_points );
        resultTextView = findViewById( R.id.tv_result );
        timeTextView = findViewById( R.id.tv_timer );

        preferences = PreferenceManager.getDefaultSharedPreferences( this );
        colorRecycler = findViewById( R.id.learn_color_game );
        colours = new ArrayList<Integer>();
        playAgainButton = findViewById( R.id.button_play_again );

        colorRecycler.setVisibility( View.INVISIBLE );

        playAgainButton.setText( "Play" );
        playAgainButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgainButton.setVisibility( View.INVISIBLE );
                timeTextView.setVisibility( View.VISIBLE );
                pointsTextView.setVisibility( View.VISIBLE );
                colorRecycler.setVisibility( View.VISIBLE );
                highScoreView.setVisibility( View.INVISIBLE );
                createQuestion();
                startQuiz();

            }
        } );
    }

    private void startQuiz() {
        timeTextView.setText( "30s" );
        totalQuestions = 0;
        correctQuestions = 0;
        pointsTextView.setText( "0/0" );
        resultTextView.setVisibility( View.INVISIBLE );
        timer = new CountDownTimer( 30100, 1000 ) {
            @Override
            public void onTick(long millisUntilFinished) {


                mediaPlayer.start();
                timeTextView.setText( String.valueOf( millisUntilFinished / 1000 ) + "s" );
            }

            @Override
            public void onFinish() {
                mediaPlayer.pause();

                timeTextView.setText( "0s" );
                playAgainButton.setText( "Play Again" );
                playAgainButton.setVisibility( View.VISIBLE );
                timeTextView.setVisibility( View.INVISIBLE );
                pointsTextView.setVisibility( View.INVISIBLE );
                colorRecycler.setVisibility( View.INVISIBLE );
                showScore();

            }
        }.start();
    }

    private void showScore() {
        resultTextView.setVisibility( View.VISIBLE );
        resultTextView.setText( "Score = " + correctQuestions + "/" + totalQuestions );
        preferences.edit().putInt( "span", 2 ).apply();
        ;
        String message;

        if (correctQuestions >= highScore) {
            highScore = correctQuestions;
            preferences.edit().putInt( "high_score_color", correctQuestions ).apply();
            message = "CONGRATS\nNew High Score = ";
            mediaPlayer1.start();
        } else {
            message = "High Score = ";
        }

        highScoreView.setText( message + String.valueOf( highScore ) );
        highScoreView.setVisibility( View.VISIBLE );

    }

    public void createQuestion() {
        int a, i, b, c, span;
        colours.clear();
        span = preferences.getInt( "span", 2 );
        colorRecycler.setLayoutManager( new GridLayoutManager( this, span ) );
        Random random = new Random();

        int d = random.nextInt( span * 5 - 1 );
        preferences.edit().putInt( "correctanswer", d ).apply();
        a = random.nextInt( 256 );
        b = random.nextInt( 256 );
        c = random.nextInt( 256 );

        int color2 = Color.argb( 255, a, b, c );
        int color1 = Color.argb( 255, a + 12, b - 11, c + 13 );
        for (i = 0; i <= span * 5 - 1; i++) {
            if (i == d)
                colours.add( color1 );
            else
                colours.add( color2 );
        }
        colorAdapter = new ColorAdapter( colours, this, (ColorAdapter.OnCategorySelectedListener) this );

        colorRecycler.setAdapter( colorAdapter );
    }

    @Override
    public void onTopicSelected(int pos) {
        if (pos == preferences.getInt( "correctanswer", 0 )) {

            pointsTextView.setBackgroundColor( getResources().getColor( R.color.green ) );
            int c = preferences.getInt( "span", 2 );
            if (c <= 5)
                preferences.edit().putInt( "span", ++c ).apply();
            ;
            correctQuestions++;
        } else {
            final RecyclerView.ViewHolder g = colorRecycler.findViewHolderForAdapterPosition( preferences.getInt( "correctanswer", 0 ) );
            g.itemView.animate().scaleX( 1.3f ).scaleY( 1.3f ).setDuration( 500 ).withEndAction( new Runnable() {
                @Override
                public void run() {
                    g.itemView.animate().setDuration( 200 ).scaleX( 1 ).scaleY( 1 ).start();
                }
            } ).start();
            pointsTextView.setBackgroundColor( getResources().getColor( R.color.red ) );
        }
        totalQuestions++;
        pointsTextView.setText( correctQuestions + "/" + totalQuestions );
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pointsTextView.setBackgroundResource( R.color.grey );
                createQuestion();

            }
        }, 700 );

    }

    @Override
    public void onBackPressed() {
        if (highScoreView.getVisibility() == View.VISIBLE) {
            mediaPlayer.stop();
            super.onBackPressed();
            mediaPlayer1.stop();
        } else {
            timer.onFinish();
            timer.cancel();
            mediaPlayer.stop();
        }
    }
}
