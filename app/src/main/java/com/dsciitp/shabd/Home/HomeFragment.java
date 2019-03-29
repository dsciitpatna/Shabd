package com.dsciitp.shabd.Home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dsciitp.shabd.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirestoreRecyclerAdapter adapter;

    OnCategorySelectedListener callback;


    public HomeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.topic_title_recycler_view);

        Query query = FirebaseFirestore.getInstance()
                .collection("topic_card")
                .orderBy("position");

        FirestoreRecyclerOptions<GetTitle> options = new FirestoreRecyclerOptions.Builder<GetTitle>()
                .setQuery(query, GetTitle.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<GetTitle, TopicHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull TopicHolder holder, int position, @NonNull GetTitle model) {
                holder.mTopicTitleName.setText(model.getTitle());
//                holder.mTopicTitleName.setOnClickListener();
            }

            @NonNull
            @Override
            public TopicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_topic, viewGroup,false);

                return new TopicHolder(view);
            }
        };

//        adapter.startListening();

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mTopicTitleName;

        TopicHolder(@NonNull View itemView) {
            super(itemView);
            mTopicTitleName = itemView.findViewById(R.id.topic_title_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.e("mylogmessage", "hello");
            int position = getAdapterPosition();
            Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

            callback.onTopicSelected(555);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategorySelectedListener) {
            callback = (OnCategorySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

//    http://developer.android.com/training/basics/fragments/communicating.html
//    interface with activity

    public interface OnCategorySelectedListener {
        void onTopicSelected(int categoryId);
    }

    public void setOnCategorySelectedListener(Activity activity) {
        callback = (OnCategorySelectedListener) activity;
    }
}
