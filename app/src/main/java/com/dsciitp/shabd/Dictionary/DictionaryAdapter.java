package com.dsciitp.shabd.Dictionary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dsciitp.shabd.R;
import java.util.ArrayList;


public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyHolder> {
    ArrayList<String> mylist;

    public DictionaryAdapter(ArrayList<String> mylist, Context context) {
        this.mylist = mylist;
    }


    @Override
    public DictionaryAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_topic, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.topicTitle.setText(mylist.get(position));
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView topicTitle;
        ImageView topicBackground;

        public MyHolder(View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.topic_title_name);
            topicBackground = itemView.findViewById(R.id.topic_title_background);
        }
    }
}