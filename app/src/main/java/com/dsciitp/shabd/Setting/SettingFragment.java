package com.dsciitp.shabd.Setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsciitp.shabd.R;
import com.dsciitp.shabd.SigninActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        TextView about_view = view.findViewById(R.id.about_shabd_button);

             // Set a click listener on that View
        about_view.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the about shabd View is clicked on.
            @Override
            public void onClick(View view1) {
                Intent aboutIntent = new Intent(getActivity(), aboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        LinearLayout logout = view.findViewById(R.id.setting_logout_ll);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    private void logout(){
        Intent intent = new Intent(getContext(), SigninActivity.class);
        intent.putExtra("logout_action", "logout");
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}
