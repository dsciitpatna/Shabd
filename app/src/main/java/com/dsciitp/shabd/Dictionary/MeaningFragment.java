package com.dsciitp.shabd.Dictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsciitp.shabd.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeaningFragment extends Fragment {

    AVLoadingIndicatorView progress;
    TextView meaning;
    final private OnMeaningPass callback = null;
    OnMeaningPass dataPasser;
    private static final String ARG_PARAM1 = "param1";
    String searchText;
    private static final String TTS_SPEAK_ID = "SPEAK";

    public MeaningFragment() {
        // Required empty public constructor
    }

    public interface OnMeaningPass {
        public void onMeaningPass(String data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnMeaningPass) context;
    }

    public void passData(String data) {
        dataPasser.onMeaningPass(data);
    }

    public static MeaningFragment newInstance(String param1) {
        MeaningFragment fragment = new MeaningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchText = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meaning, container, false);
        progress = view.findViewById(R.id.progress);
        meaning = view.findViewById(R.id.mean);
        progress.show();
        //progress.setVisibility(View.VISIBLE);

        task(searchText);
        return view;
    }

    private void task(final String search) {

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                return NetworkUtils.dictionaryEntries(search);
            }

            @Override
            protected void onPostExecute(String s) {
                after(s);
            }
        }.execute();
    }

    // AsyncTaskLoader<>
    private void after(String s) {
        //progress.setVisibility(View.INVISIBLE);
        progress.hide();
        meaning.setVisibility(View.VISIBLE);
        if (s == null)
            meaning.setText("Not Found");
        else
            meaning.setText(s);
        passData(meaning.getText().toString());

    }
}
