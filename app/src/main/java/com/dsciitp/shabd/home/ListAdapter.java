package com.dsciitp.shabd.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsciitp.shabd.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListAdapter extends RecyclerView.Adapter {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("topic_card");
    private DocumentReference noteRef = db.document("topic_card/topic_1");
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_topic,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ListViewHolder) viewHolder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTopicTitleName;

        public ListViewHolder(View itemView){
            super(itemView);
            mTopicTitleName=(TextView) itemView.findViewById(R.id.topic_title_name);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            notebookRef.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                GetTitle getTitles=documentSnapshot.toObject(GetTitle.class);

                                String title=getTitles.getTitle();
                                mTopicTitleName.setText(title);
                            }
                        }
                    });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
