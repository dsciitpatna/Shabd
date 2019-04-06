package com.dsciitp.shabd.BasicTopic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class BasicFragment extends Fragment {

    private static final String WORD_TITLE = "TITLE";

    private String wordTitle;

    private List<WordsInRealm> topicList;
    Realm realm;


    public BasicFragment() {}

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
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        populateData();

        RecyclerView recyclerView = view.findViewById(R.id.basic_frag_recycler_view);

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
        query.equalTo("parentClass", wordTitle);
        RealmResults<WordsInRealm> result = query.findAll();

        realm.beginTransaction();
        topicList.addAll(result);
        realm.commitTransaction();
    }
}
