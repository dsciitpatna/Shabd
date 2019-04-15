package com.dsciitp.shabd.BasicTopic;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BasicRecyclerAdapter extends RecyclerView.Adapter<BasicRecyclerAdapter.TopicHolder> {

    private static final String TAG = BasicRecyclerAdapter.class.getSimpleName();

    final private OnSubCategorySelectedListener callback;
    private Context context;
    private List<WordsInRealm> topicList;

    private RealmConfiguration config = new RealmConfiguration.Builder()
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm realm = Realm.getInstance(config);

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

        final String title = topicList.get(position).getTitle();
        final int id = topicList.get(position).getId();
        String imageRes = topicList.get(position).getImageResource();
        holder.wordTitle.setText(title);

        if (topicList.get(position).getIsItTopic() == 1) {
            holder.cardLinearLayout.setBackgroundResource(R.drawable.gradient_2);
            holder.cardCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            WordsInRealm result = realm.where(WordsInRealm.class).equalTo("parentClass", title).findFirst();
                            if (result == null){
                                showDialogueBox(id, title, holder.getAdapterPosition());
                            } else {
                                Toast.makeText(context, "You cannot delete non empty categories", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return false;
                }
            });
        } else {
            holder.cardLinearLayout.setBackgroundResource(R.drawable.gradient_1);
            holder.cardCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialogueBox(id, title, holder.getAdapterPosition());
                    return false;
                }
            });
        }

        holder.cardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSubTopicSelected(id, v);
            }
        });


        Glide.with(context)
                .load(imageRes)
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

    private void showDialogueBox(final int id, String title, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
        builder.setMessage(title);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setIcon(R.drawable.shabd);
        builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            WordsInRealm result = realm.where(WordsInRealm.class).equalTo("id", id).findFirst();
                            if (result != null) result.deleteFromRealm();
                        }
                    });
                    topicList.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(true);

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}