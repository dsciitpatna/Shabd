package com.dsciitp.shabd.Learn.ColorGame;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {


    private Context context;
    private ArrayList<Integer> mylist;
    public ColorAdapter.OnCategorySelectedListener callback;

    ColorAdapter(ArrayList<Integer> mylist, Context context, ColorAdapter.OnCategorySelectedListener listener) {
        this.mylist = mylist;
        callback = listener;
        this.context = context;

    }

    public interface OnCategorySelectedListener {
        void onTopicSelected(int pos);
    }

    @NonNull
    @Override
    public ColorAdapter.ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.color_card, parent, false );
        return new ColorHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorAdapter.ColorHolder holder, final int position) {

        int e = mylist.get( holder.getAdapterPosition() );

        holder.cardCardView.setCardBackgroundColor( e );
        holder.cardCardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicSelected( holder.getAdapterPosition() );

            }
        } );


    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    class ColorHolder extends RecyclerView.ViewHolder {
        CardView cardCardView;

        ColorHolder(@NonNull View itemView) {
            super( itemView );
            cardCardView = itemView.findViewById( R.id.card_color );
        }
    }

}
