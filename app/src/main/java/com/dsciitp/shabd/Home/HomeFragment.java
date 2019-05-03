package com.dsciitp.shabd.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsFromFirebase;
import com.dsciitp.shabd.database.WordsInRealm;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class HomeFragment extends Fragment{

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private HomeRecyclerAdapter recyclerAdapter;
    private List<WordsInRealm> topicList;
    Realm realm;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        initFirestore();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("topic_card");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        topicList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.topic_title_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RealmQuery<WordsInRealm> query = realm.where(WordsInRealm.class);
        query.equalTo("parentClass", "Home");
        RealmResults<WordsInRealm> result = query.findAll();

        realm.beginTransaction();
        topicList.addAll(result);
        realm.commitTransaction();

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
                List<WordsFromFirebase> topicModels;
                if (queryDocumentSnapshots != null) {
                    topicModels = queryDocumentSnapshots.toObjects(WordsFromFirebase.class);
                    for (WordsFromFirebase wordsFromFirebase : topicModels) {
                        WordsInRealm newWord = new WordsInRealm();
                        newWord.setId(wordsFromFirebase.getId());
                        newWord.setDescription(wordsFromFirebase.getDescription());
                        newWord.setTitle(wordsFromFirebase.getTitle());
                        newWord.setHindiTitle(wordsFromFirebase.getHindiTitle());
                        newWord.setImageResource(wordsFromFirebase.getImageResource());
                        newWord.setParentClass(wordsFromFirebase.getParentClass());
                        newWord.setIsItTopic(wordsFromFirebase.getIsItTopic());
                        topicList.add(newWord);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        });

    }
}
