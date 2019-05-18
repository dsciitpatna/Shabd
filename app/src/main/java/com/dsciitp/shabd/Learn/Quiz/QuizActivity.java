package com.dsciitp.shabd.Learn.Quiz;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsciitp.shabd.R;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    int locationOfCorrectAnswer;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    int totalQuestions;
    int correctQuestions;

    TextView pointsTextView;
    TextView resultTextView;
    TextView timeTextView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView questionTextView;
    Button playAgainButton;
    MediaPlayer mediaPlayer,mediaPlayer1;
    RelativeLayout gameRelativeLayout;
    CountDownTimer timer;
    SharedPreferences preferences;
    int highScore;
    TextView highScoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz );
        mediaPlayer = MediaPlayer.create( QuizActivity.this, R.raw.clock );
        mediaPlayer1=MediaPlayer.create( this,R.raw.applause );
        getSupportActionBar().setElevation( 0f );
        getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable( R.drawable.ic_arrow_back_white_24dp ) );
        getSupportActionBar().setTitle( getString( R.string.quiz ) );
        initViews();

    }

    private void initViews() {

        preferences = PreferenceManager.getDefaultSharedPreferences( this );

        highScore = preferences.getInt( "high_score", 0 );
        highScoreView = findViewById( R.id.tv_high_score );
        highScoreView.setText( "HighScore = " + String.valueOf( preferences.getInt( "high_score", 0 ) ) );

        pointsTextView = findViewById( R.id.tv_points );
        resultTextView = findViewById( R.id.tv_result );
        timeTextView = findViewById( R.id.tv_timer );
        questionTextView = findViewById( R.id.tv_question );
        button1 = findViewById( R.id.button1 );
        button2 = findViewById( R.id.button2 );
        button3 = findViewById( R.id.button3 );
        button4 = findViewById( R.id.button4 );
        playAgainButton = findViewById( R.id.button_play_again );
        gameRelativeLayout = findViewById( R.id.gameRelativeLayout );
        gameRelativeLayout.setVisibility( View.INVISIBLE );

        playAgainButton.setText( "Play" );

        playAgainButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgainButton.setVisibility( View.INVISIBLE );
                gameRelativeLayout.setVisibility( View.VISIBLE );
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
        mediaPlayer = MediaPlayer.create( QuizActivity.this, R.raw.clock );
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
                gameRelativeLayout.setVisibility( View.INVISIBLE );
                showScore();

            }
        }.start();
    }

    private void showScore() {
        resultTextView.setVisibility( View.VISIBLE );
        resultTextView.setText( "Score = " + correctQuestions + "/" + totalQuestions );

        String message;

        if (correctQuestions >= highScore) {
            highScore = correctQuestions;
            preferences.edit().putInt( "high_score", correctQuestions ).apply();
            message = "CONGRATS\nNew High Score = ";
            mediaPlayer1.start();
        } else {
            message = "High Score = ";
        }

        highScoreView.setText( message + String.valueOf( highScore ) );
        highScoreView.setVisibility( View.VISIBLE );

    }

    public void checkAnswer(final View view) {

        int g = R.color.green;
        int r = R.color.red;
        if (view.getTag().toString().equals( String.valueOf( locationOfCorrectAnswer ) )) {
            view.setBackgroundResource( g );
            //resultTextView.setText("Correct");
            correctQuestions++;
        } else {
            view.setBackgroundResource( r );
            //resultTextView.setText("Incorrect");
        }

        totalQuestions++;
        pointsTextView.setText( correctQuestions + "/" + totalQuestions );
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                view.setBackgroundResource( R.color.grey );
                createQuestion();

            }
        }, 300 );


    }

    private void createQuestion() {

        answers.clear();

        Random random = new Random();

        int a = random.nextInt( 30 );
        int b = random.nextInt( 30 );

        locationOfCorrectAnswer = random.nextInt( 4 );
        int wrongAnswer;

        for (int i = 0; i < 4; i++) {

            if (i == locationOfCorrectAnswer) {
                answers.add( a + b );
            } else {
                wrongAnswer = random.nextInt( 30 );

                while (wrongAnswer == (a + b)) {
                    wrongAnswer = random.nextInt( 30 );
                }
                answers.add( wrongAnswer );
            }

        }

        String question = String.valueOf( a ) + "+" + String.valueOf( b );
        questionTextView.setText( question );

        button1.setText( String.valueOf( answers.get( 0 ) ) );
        button2.setText( String.valueOf( answers.get( 1 ) ) );
        button3.setText( String.valueOf( answers.get( 2 ) ) );
        button4.setText( String.valueOf( answers.get( 3 ) ) );

    }

    @Override
    public void onBackPressed() {

        if(highScoreView.getVisibility()==View.VISIBLE)
        {
            mediaPlayer.stop();
            super.onBackPressed();
            mediaPlayer1.stop();
        }
        else {
            timer.onFinish();
            timer.cancel();
            mediaPlayer.stop();
        }

    }
}
