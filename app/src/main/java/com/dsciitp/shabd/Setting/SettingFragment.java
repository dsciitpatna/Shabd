package com.dsciitp.shabd.Setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.SigninActivity;
import com.dsciitp.shabd.UserConstants;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private final String INTENT_ACTION = "intent_action";

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        LinearLayout about_view = view.findViewById(R.id.about_button);

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

//        LinearLayout update = view.findViewById(R.id.update_ll);
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateData();
//            }
//        });

        ImageView profileImage = view.findViewById(R.id.profile_image);
        Glide.with(getContext())
                .load(UserConstants.photoUri)
                .centerInside()
                .placeholder(R.drawable.profile_photo)
                .into(profileImage);
        TextView displayName = view.findViewById(R.id.display_name);
        displayName.setText(UserConstants.displayName);
        return view;
    }

    private void logout(){
        Intent intent = new Intent(getContext(), SigninActivity.class);
        intent.putExtra(INTENT_ACTION, "logout");
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
    private void updateData(){
        Intent intent = new Intent(getContext(), SigninActivity.class);
        intent.putExtra(INTENT_ACTION, "update");
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}
