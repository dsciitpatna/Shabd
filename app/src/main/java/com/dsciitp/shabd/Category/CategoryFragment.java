package com.dsciitp.shabd.Category;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class CategoryFragment extends Fragment {

    private static final String TITLE = "title";

    private String categoryTitle;

    private OnOnlineWordSelectedListener mListener;
    FirestoreRecyclerAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    private int gradientArray[] = {R.drawable.gradient_1, R.drawable.gradient_2, R.drawable.gradient_4, R.drawable.gradient_5};

    public static CategoryFragment newInstance(String categoryTitle) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, categoryTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.category_frag_recycler_view);

        Locale currentLocale = Locale.getDefault();

        Query query = FirebaseFirestore.getInstance()
                .collection(categoryTitle + "_" + currentLocale.getLanguage()).orderBy("position");

        FirestoreRecyclerOptions<CategoryModel> options = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CategoryModel, WordHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull WordHolder holder, int position, @NonNull final CategoryModel model) {

                holder.wordTitle.setText(model.getTitle());

                Random rnd = new Random();
                int gradientPosition = rnd.nextInt(4);
                holder.cardLinearLayout.setBackgroundResource(gradientArray[gradientPosition]);

                Glide.with(Objects.requireNonNull(getContext()))
                        .load(model.getImageUrl())
                        .centerCrop()
                        .placeholder(R.color.transparent)
                        .into(holder.wordImage);

                holder.cardCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onOnlineWordSelected(model.getTitle(), v);
                    }
                });
            }

            @NonNull
            @Override
            public WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_word, viewGroup,false);

                return new WordHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
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

    private class WordHolder extends RecyclerView.ViewHolder{
        TextView wordTitle;
        ImageView wordImage;
        CardView cardCardView;
        LinearLayout cardLinearLayout;

        WordHolder(@NonNull View itemView) {
            super(itemView);
            wordTitle = itemView.findViewById(R.id.word_card_text);
            wordImage = itemView.findViewById(R.id.word_card_image);
            cardCardView = itemView.findViewById(R.id.card_word_cv);
            cardLinearLayout = itemView.findViewById(R.id.card_word_ll);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOnlineWordSelectedListener) {
            mListener = (OnOnlineWordSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnOnlineWordSelectedListener {
        void onOnlineWordSelected(String text, View view);
    }
}
