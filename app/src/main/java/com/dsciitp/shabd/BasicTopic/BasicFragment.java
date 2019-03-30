package com.dsciitp.shabd.BasicTopic;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.Home.TopicModel;
import com.dsciitp.shabd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicFragment extends Fragment {

    private static final String WORD_TITLE = "TITLE";

    private String wordTitle;

    private List<TopicModel> topicList;
    private BasicRecyclerAdapter recyclerAdapter;

    public BasicFragment() {
        // Required empty public constructor
    }

    public static BasicFragment newInstance(String title) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putString(WORD_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordTitle = getArguments().getString(WORD_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        populateData();

        RecyclerView recyclerView = view.findViewById(R.id.basic_frag_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new BasicRecyclerAdapter(getContext(), topicList, (BasicRecyclerAdapter.OnSubCategorySelectedListener)getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topicList.clear();
    }

    private void populateData(){
        topicList = new ArrayList<>();
        Resources res = getContext().getResources();

        List<String> words = Arrays.asList(getResources().getStringArray(res.getIdentifier(wordTitle+"_array", "array", getContext().getPackageName())));
        List<String> resID = Arrays.asList(getResources().getStringArray(res.getIdentifier(wordTitle+"_array_res", "array", getContext().getPackageName())));

        for (int i = 0; i < words.size(); i++) {

            if (res.getIdentifier(resID.get(i), "drawable", getContext().getPackageName()) != 0) {
                topicList.add(new TopicModel(words.get(i), res.getIdentifier(resID.get(i), "drawable", getContext().getPackageName())));
            } else {
                topicList.add(new TopicModel(words.get(i), resID.get(i)));
            }
            Log.e("mylogmessage", String.valueOf(res.getIdentifier(resID.get(i), "drawable", getContext().getPackageName())));

        }

    }
}
