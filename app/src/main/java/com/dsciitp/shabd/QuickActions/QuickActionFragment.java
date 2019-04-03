package com.dsciitp.shabd.QuickActions;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.BasicTopic.BasicRecyclerAdapter;
import com.dsciitp.shabd.Home.TopicModel;
import com.dsciitp.shabd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickActionFragment extends Fragment {


    public QuickActionFragment() {
        // Required empty public constructor
    }

    Resources res;
    private List<TopicModel> topicList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = Objects.requireNonNull(getContext()).getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_action, container, false);

        populateData();

        RecyclerView recyclerView = view.findViewById(R.id.quick_action_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        BasicRecyclerAdapter recyclerAdapter = new BasicRecyclerAdapter(getContext(), topicList, (BasicRecyclerAdapter.OnSubCategorySelectedListener) getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topicList.clear();
    }

    private void populateData() {
        topicList = new ArrayList<>();

        List<String> words = Arrays.asList(res.getStringArray(
                res.getIdentifier("Quick_Action_array", "array", getContext().getPackageName())));
        List<String> resID = Arrays.asList(res.getStringArray(
                res.getIdentifier("Quick_Action_array_res", "array", getContext().getPackageName())));

        for (int i = 0; i < words.size(); i++) {

            if (res.getIdentifier(resID.get(i), "drawable", getContext().getPackageName()) != 0) {
                topicList.add(new TopicModel(words.get(i),
                        res.getIdentifier(resID.get(i), "drawable", getContext().getPackageName()), words.get(i)));
            } else {
                topicList.add(new TopicModel(words.get(i), resID.get(i), words.get(i)));
            }

        }
    }

}
