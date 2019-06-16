package com.dsciitp.shabd.Learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.Learn.ColorGame.ColorGameActivity;
import com.dsciitp.shabd.Learn.Drawing.DrawingActivity;
import com.dsciitp.shabd.Learn.Piano.PianoActivity;
import com.dsciitp.shabd.Learn.Quiz.QuizActivity;
import com.dsciitp.shabd.Learn.Video.VideoActivity;
import com.dsciitp.shabd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class LearnActivity extends AppCompatActivity implements LearnAdapter.OnCategorySelectedListener {

    LearnAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_learn );
        Objects.requireNonNull( getSupportActionBar() ).setElevation( 0f );
        getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable( R.drawable.ic_arrow_back_white_24dp ) );

        RecyclerView storyRecycler = findViewById( R.id.learn_recycler_story );
        storyRecycler.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );
        ArrayList<LearnStoryModel> options = new ArrayList<LearnStoryModel>();

        try {
            JSONObject data = new JSONObject( loadJSONFromAsset() );
            JSONArray task = data.getJSONArray( "data" );
            for (int i = 0; i < task.length(); i++) {
                JSONObject jo_inside = task.getJSONObject( i );
                String title = jo_inside.getString( "title" );
                String url_value = jo_inside.getString( "imageResource" );
                String hindititle = jo_inside.getString( "hindiTitle" );
                String description = jo_inside.getString( "description" );
                String intent = jo_inside.getString( "intent" );
                String intentclass = jo_inside.getString( "intentClass" );
                String classname = "com.dsciitp.shabd.Learn." + intentclass + "." + intent;
                options.add( new LearnStoryModel( title, description, hindititle, url_value, Class.forName( classname ) ) );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            options.add( new LearnStoryModel( "Holiday", "", "gdfg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWEXBiGxKzBLNDhHMmgrR6KBYlOOO2f0SXP5TZ3DS_UthzhOoq", null ) );
        }

        storiesAdapter = new LearnAdapter( options, LearnActivity.this, (LearnAdapter.OnCategorySelectedListener) this );
        storyRecycler.setAdapter( storiesAdapter );

        ImageView learnImage2 = findViewById( R.id.learn_image_2 );
        Glide.with( this )
                .load( R.raw.play_play )
                .centerCrop()
                .placeholder( R.color.transparent )
                .into( learnImage2 );
        ImageView learnImage3 = findViewById( R.id.learn_image_3 );
        Glide.with( this )
                .load( "https://s3.amazonaws.com/media.eremedia.com/wp-content/uploads/2018/04/06122011/story.jpeg" )
                .centerCrop()
                .placeholder( R.color.transparent )
                .into( learnImage3 );
        ImageView learnImage4 = findViewById( R.id.learn_image_4 );
        Glide.with( this )
                .load( "http://clipartmag.com/images/quiz-clipart-24.jpg" )
                .centerCrop()
                .placeholder( R.color.transparent )
                .into( learnImage4 );
        ImageView learnImage5 = findViewById( R.id.learn_image_5 );
        Glide.with( this )
                .load( "https://www.codester.com/static//uploads/items/2658/preview-xl.jpg" )
                .centerCrop()
                .placeholder( R.color.transparent )
                .into( learnImage5 );

        CardView pianoCard = findViewById( R.id.learn_card_2 );
        pianoCard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LearnActivity.this, PianoActivity.class ) );
            }
        } );
        CardView drawingCard = findViewById( R.id.learn_card_1 );
        drawingCard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( LearnActivity.this, DrawingActivity.class );
                i.putExtra( "drawing", 1 );
                startActivity( i );
            }
        } );
        CardView numberCard = findViewById( R.id.learn_card_4 );
        numberCard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LearnActivity.this, QuizActivity.class ) );
            }
        } );
        CardView videoCard = findViewById( R.id.learn_card_3 );
        videoCard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LearnActivity.this, VideoActivity.class ) );
            }
        } );
        CardView colorCard = findViewById( R.id.learn_card_5 );
        colorCard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LearnActivity.this, ColorGameActivity.class ) );
            }
        } );
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open( "task_data.json" );
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read( buffer );
            is.close();
            json = new String( buffer, "UTF-8" );

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onStop() {
        super.onStop();
        //storiesAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        //storiesAdapter.startListening();
    }

    @Override
    public void onTopicSelected(LearnStoryModel title) {

        if (title.getIntent() != null)
            startActivity( new Intent( LearnActivity.this, title.getIntent() ) );
    }
}
