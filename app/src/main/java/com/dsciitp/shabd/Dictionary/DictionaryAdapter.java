package com.dsciitp.shabd.Dictionary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dsciitp.shabd.R;
import java.util.ArrayList;


public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyHolder> {


    final private OnCategorySelectedListener callback;
    private ArrayList<String> mylist;

    DictionaryAdapter(ArrayList<String> mylist, Context context,DictionaryAdapter.OnCategorySelectedListener listener) {
        this.mylist = mylist;
        callback = listener;
    }
    public interface OnCategorySelectedListener {
        void onTopicSelected(String title);
    }

    @NonNull
    @Override
    public DictionaryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dictionary, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        holder.topicTitle.setText(mylist.get(position));
        holder.topicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicSelected(mylist.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView topicTitle;
        ImageView topicBackground;

        MyHolder(View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.topic_title_name);
            topicBackground = itemView.findViewById(R.id.topic_title_background);
        }
    }
}