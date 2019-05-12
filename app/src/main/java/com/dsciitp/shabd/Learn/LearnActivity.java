package com.dsciitp.shabd.Learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.Dictionary.DictionaryAdapter;
import com.dsciitp.shabd.Learn.Drawing.DrawingActivity;
import com.dsciitp.shabd.Learn.Piano.PianoActivity;
import com.dsciitp.shabd.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity implements LearnAdapter.OnCategorySelectedListener{

  LearnAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));



        RecyclerView storyRecycler = findViewById(R.id.learn_recycler_story);
        storyRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<LearnStoryModel> options = new ArrayList<LearnStoryModel>();
        //options.add( "Draw an apple","dgd","gdfg","https://www.countryplace.com.au/wp-content/uploads/Creative-Painting.jpg","Drawing" );
        options.add( new LearnStoryModel( "Play song","dgd","gdfg","https://www.countryplace.com.au/wp-content/uploads/Creative-Painting.jpg",PianoActivity.class ) );
        options.add( new LearnStoryModel( "Listen to a story","dgd","gdfg","https://s3.amazonaws.com/media.eremedia.com/wp-content/uploads/2018/04/06122011/story.jpeg",DrawingActivity.class ) );

        String draw_resouces[] = getResources().getStringArray(R.array.drawings_source);
        String picture[] = getResources().getStringArray(R.array.draw_items);
        for (int i=0;i<10;i++)
        {
            options.add( new LearnStoryModel(picture[i],"dgd","gdfg",draw_resouces[i],DrawingActivity.class ));
        }


        storiesAdapter=new LearnAdapter( options,LearnActivity.this, (LearnAdapter.OnCategorySelectedListener )this);
         storyRecycler.setAdapter( storiesAdapter );

        ImageView learnImage2 = findViewById(R.id.learn_image_2);
        Glide.with(this)
                .load(R.raw.play_play)
                .centerCrop()
                .placeholder(R.color.transparent)
                .into(learnImage2);
        ImageView learnImage3 = findViewById(R.id.learn_image_3);
        Glide.with(this)
                .load("https://s3.amazonaws.com/media.eremedia.com/wp-content/uploads/2018/04/06122011/story.jpeg")
                .centerCrop()
                .placeholder(R.color.transparent)
                .into(learnImage3);

        CardView pianoCard = findViewById(R.id.learn_card_2);
        pianoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LearnActivity.this, PianoActivity.class));
            }
        });
        CardView drawingCard = findViewById(R.id.learn_card_1);
        drawingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LearnActivity.this, DrawingActivity.class));
            }
        });

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

        startActivity(new Intent(LearnActivity.this, title.getIntent()));
    }
}
