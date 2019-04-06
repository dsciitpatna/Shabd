package com.dsciitp.shabd.QuickActions;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.BasicTopic.BasicRecyclerAdapter;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickActionFragment extends Fragment {


    public QuickActionFragment() {
        // Required empty public constructor
    }

    Realm realm;

    Resources res;
    private List<WordsInRealm> topicList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = Objects.requireNonNull(getContext()).getResources();
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
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

        RealmQuery<WordsInRealm> query = realm.where(WordsInRealm.class);
        query.equalTo("parentClass", "QuickAction");
        RealmResults<WordsInRealm> result = query.findAll();

        realm.beginTransaction();
        for(WordsInRealm words : result){
            topicList.add(words);
            Log.e("mylog", String.valueOf(words.getId()));
        }
        realm.commitTransaction();
    }

}
