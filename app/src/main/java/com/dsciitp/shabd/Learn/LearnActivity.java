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
import com.dsciitp.shabd.Learn.Piano.PianoActivity;
import com.dsciitp.shabd.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LearnActivity extends AppCompatActivity {

    FirestoreRecyclerAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        RecyclerView storyRecycler = findViewById(R.id.learn_recycler_story);
        storyRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Query query = mFirestore.collection("stories").orderBy("position");

        FirestoreRecyclerOptions<LearnStoryModel> options = new FirestoreRecyclerOptions.Builder<LearnStoryModel>()
                .setQuery(query, LearnStoryModel.class)
                .build();

        storiesAdapter = new FirestoreRecyclerAdapter<LearnStoryModel, StoryHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull StoryHolder holder, int position, @NonNull final LearnStoryModel model) {

                holder.wordTitle.setText(model.getTitle());

                Glide.with(LearnActivity.this)
                        .load(model.getImageResource())
                        .centerCrop()
                        .placeholder(R.color.transparent)
                        .into(holder.wordImage);

                holder.cardCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @NonNull
            @Override
            public StoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_learn_stories, viewGroup, false);

                return new StoryHolder(view);
            }
        };
        storyRecycler.setAdapter(storiesAdapter);

        ImageView learnImage2 = findViewById(R.id.learn_image_2);
        Glide.with(this)
                .load(R.raw.play_play)
                .centerCrop()
                .placeholder(R.color.transparent)
                .into(learnImage2);

        CardView pianoCard = findViewById(R.id.learn_card_2);
        pianoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LearnActivity.this, PianoActivity.class));
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        storiesAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        storiesAdapter.startListening();
    }

    private class StoryHolder extends RecyclerView.ViewHolder {
        TextView wordTitle;
        ImageView wordImage;
        CardView cardCardView;

        StoryHolder(@NonNull View itemView) {
            super(itemView);
            wordTitle = itemView.findViewById(R.id.card_learn_stories_title);
            wordImage = itemView.findViewById(R.id.card_learn_stories_image);
            cardCardView = itemView.findViewById(R.id.card_learn_stories_card);
        }
    }
}
