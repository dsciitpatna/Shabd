package com.dsciitp.shabd.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.TopicHolder> {

    private static final String TAG = HomeRecyclerAdapter.class.getSimpleName();

    final private OnCategorySelectedListener callback;
    private Context context;
    private List<TopicModel> topicList;
    private String categoryName;

    public interface OnCategorySelectedListener {
        void onTopicSelected(String title);
    }

    HomeRecyclerAdapter(Context context, List<TopicModel> topicList, OnCategorySelectedListener listener) {
        this.context = context;
        callback = listener;
        this.topicList = topicList;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_topic, viewGroup,false);

        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopicHolder holder, int position) {
        Log.d(TAG, "#" + position);

        holder.topicTitle.setText(topicList.get(position).getTitle());
        categoryName = topicList.get(position).getTitle();

        holder.topicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicSelected(topicList.get(holder.getAdapterPosition()).getTitle());
            }
        });

        Glide.with(context)
                .load(topicList.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_card_placeholder)
                .into(holder.topicBackground);

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }



    class TopicHolder extends RecyclerView.ViewHolder{
        TextView topicTitle;
        ImageView topicBackground;

        TopicHolder(@NonNull View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.topic_title_name);
            topicBackground = itemView.findViewById(R.id.topic_title_background);
        }

    }
}