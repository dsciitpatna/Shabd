package com.dsciitp.shabd.BasicTopic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dsciitp.shabd.R;
import com.dsciitp.shabd.database.WordsInRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BasicFragment extends Fragment {

    private static final String WORD_TITLE = "TITLE";

    private String wordTitle;

    private List<WordsInRealm> topicList;
    Realm realm;

    FloatingActionButton editFab;

    public BasicFragment() {
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
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        topicList = new ArrayList<>();
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

        editFab = view.findViewById(R.id.basic_fragment_edit_fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topicList.clear();
    }

    private void populateData() {
        RealmQuery<WordsInRealm> query = realm.where(WordsInRealm.class);
        query.equalTo("parentClass", wordTitle).sort("isItTopic", Sort.DESCENDING);
        RealmResults<WordsInRealm> result = query.findAll();

        realm.beginTransaction();
        topicList.addAll(result);
        realm.commitTransaction();
    }

    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.basic_popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(new LinearLayout(getContext()), Gravity.CENTER, 0, 0);

        final Button addCatBut = popupView.findViewById(R.id.add_category_button);
        final Button addWordBut = popupView.findViewById(R.id.add_word_button);

        final LinearLayout editLayout = popupView.findViewById(R.id.basic_popup_edit_layout);
        final EditText titleInput = popupView.findViewById(R.id.basic_popup_input_title);
        final EditText descriptionInput = popupView.findViewById(R.id.basic_popup_input_description);
        final EditText imageInput = popupView.findViewById(R.id.basic_popup_input_image);

        addCatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.VISIBLE);
                addWordBut.setVisibility(View.GONE);
                addCatBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addData(titleInput.getText().toString(), descriptionInput.getText().toString(), imageInput.getText().toString(), 1);
                        popupWindow.dismiss();
                    }
                });
            }
        });
        addWordBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.VISIBLE);
                addCatBut.setVisibility(View.GONE);
                addWordBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addData(titleInput.getText().toString(), descriptionInput.getText().toString(), imageInput.getText().toString(), 0);
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    private void addData(String title, String description, String image, int isItTopic) {
        realm.beginTransaction();

        Number currentIdNum = realm.where(WordsInRealm.class).max("id");
        int nextId = currentIdNum.intValue() + 1;

        WordsInRealm newWord = realm.createObject(WordsInRealm.class, nextId);
        newWord.setDescription(description);
        newWord.setTitle(title);
        newWord.setHindiTitle(title);
        newWord.setImageResource(image);
        newWord.setParentClass(wordTitle);
        newWord.setIsItTopic(isItTopic);

        realm.insert(newWord);
        realm.commitTransaction();

        topicList.clear();
        populateData();

    }
}
