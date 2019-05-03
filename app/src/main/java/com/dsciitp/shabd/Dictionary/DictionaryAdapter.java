package com.dsciitp.shabd.Dictionary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsciitp.shabd.R;

import java.util.ArrayList;


public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyHolder> {

    private Context context;
    final private OnCategorySelectedListener callback;
    private ArrayList<String> mylist;

    DictionaryAdapter(ArrayList<String> mylist, Context context, DictionaryAdapter.OnCategorySelectedListener listener) {
        this.mylist = mylist;
        callback = listener;
        this.context = context;
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
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final float screenx = size.x;
        final float screeny = size.y;
        holder.alphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final float originalx = holder.alphabet.getX();
                final float originaly = holder.alphabet.getY();
                v.setClickable(false);
                v.animate().x(screenx / 3).y(screeny / 3).translationZBy(25f).scaleX(2.2f).scaleY(2.2f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTopicSelected(mylist.get(holder.getAdapterPosition()));
                        v.animate().x(originalx).y(originaly).translationZBy(-25f).scaleX(1f).scaleY(1f).setDuration(1000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.setClickable(true);
                            }
                        });
                    }
                });
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
        CardView alphabet;

        MyHolder(View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.topic_title_name);
            topicBackground = itemView.findViewById(R.id.topic_title_background);
            alphabet = itemView.findViewById(R.id.card_view);
        }
    }
}