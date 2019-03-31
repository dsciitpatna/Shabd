package com.dsciitp.shabd.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class HomeFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore mFirestore;
    private CollectionReference topicReference;
    private Query mQuery;

    private HomeRecyclerAdapter recyclerAdapter;
    private List<TopicModel> topicList;

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
        initFirestore();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("topic_card")
                .orderBy("position");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        topicList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.topic_title_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        topicList.add(new TopicModel("Basic", "", "Basic Words", "Basic"));
        topicList.add(new TopicModel("Advanced", "", "Advanced Words", "Advanced"));

        recyclerAdapter = new HomeRecyclerAdapter(getContext(), topicList, (HomeRecyclerAdapter.OnCategorySelectedListener)getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        populateRecycler();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topicList.clear();
    }

    private void populateRecycler(){

        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("MainActivity", "Error getting Snapshots");
                    return;
                }
                List<TopicModel> topicModels;
                if (queryDocumentSnapshots != null) {
                    topicModels = queryDocumentSnapshots.toObjects(TopicModel.class);
                    topicList.addAll(topicModels);
                    Log.e("mylogmessage", topicList.toString());
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        });

    }
}
