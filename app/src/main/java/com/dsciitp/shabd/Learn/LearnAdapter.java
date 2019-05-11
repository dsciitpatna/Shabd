package com.dsciitp.shabd.Learn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;

import java.util.ArrayList;


public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.StoryHolder>  {



    private Context context;
    private ArrayList<LearnStoryModel> mylist;
    public LearnAdapter.OnCategorySelectedListener callback;
   LearnAdapter(ArrayList<LearnStoryModel> mylist, Context context,LearnAdapter.OnCategorySelectedListener listener) {
        this.mylist = mylist;
        callback=listener;
        this.context = context;
    }

    public interface OnCategorySelectedListener {
        void onTopicSelected(LearnStoryModel title);
    }

    @NonNull
    @Override
    public LearnAdapter.StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_learn_stories, parent, false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryHolder holder, final int position) {
       LearnStoryModel e=mylist.get(holder.getAdapterPosition());
        holder.wordTitle.setText(e.getTitle());
        Glide.with(context)
                        .load(e.getImageResource())
                        .centerCrop()
                        .placeholder(R.color.transparent)
                        .into(holder.wordImage);
       holder.cardCardView.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callback.onTopicSelected(mylist.get(holder.getAdapterPosition()));
           }
       } );

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

   class StoryHolder extends RecyclerView.ViewHolder {
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