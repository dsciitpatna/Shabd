package com.dsciitp.shabd.BasicTopic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.List;

public class BasicRecyclerAdapter extends RecyclerView.Adapter<BasicRecyclerAdapter.TopicHolder> {

    private static final String TAG = BasicRecyclerAdapter.class.getSimpleName();

    final private OnSubCategorySelectedListener callback;
    private Context context;
    private List<WordsInRealm> topicList;

    public interface OnSubCategorySelectedListener {
        void onSubTopicSelected(int id, View view);
    }

    public BasicRecyclerAdapter(Context context, List<WordsInRealm> topicList, OnSubCategorySelectedListener listener) {
        this.context = context;
        callback = listener;
        this.topicList = topicList;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_word, viewGroup, false);

        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopicHolder holder, int position) {

        holder.wordTitle.setText(topicList.get(position).getTitle());

        if (topicList.get(position).getIsItTopic() == 1) {
            holder.cardLinearLayout.setBackgroundResource(R.drawable.gradient_2);
        } else {
            holder.cardLinearLayout.setBackgroundResource(R.drawable.gradient_1);
        }

        holder.cardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSubTopicSelected(topicList.get(holder.getAdapterPosition()).getId(), v);
            }
        });


        Glide.with(context)
                .load(topicList.get(position).getImageResource())
                .centerCrop()
                .placeholder(R.color.transparent)
                .into(holder.wordImage);
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    class TopicHolder extends RecyclerView.ViewHolder {
        TextView wordTitle;
        ImageView wordImage;
        CardView cardCardView;
        LinearLayout cardLinearLayout;

        TopicHolder(@NonNull View itemView) {
            super(itemView);
            wordTitle = itemView.findViewById(R.id.word_card_text);
            wordImage = itemView.findViewById(R.id.word_card_image);
            cardCardView = itemView.findViewById(R.id.card_word_cv);
            cardLinearLayout = itemView.findViewById(R.id.card_word_ll);
        }
    }
}