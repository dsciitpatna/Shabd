package com.dsciitp.shabd.Setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.UserConstants;
import com.dsciitp.shabd.signin.SigninActivity;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.Objects;

import butterknife.BindView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private final String INTENT_ACTION = "intent_action";
private LinearLayout language_setting;

    private  int rating;

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

        LinearLayout update = view.findViewById(R.id.update_ll);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        final LinearLayout settings_layout = view.findViewById(R.id.settings_fragmenr_layout);
        LinearLayout rate = view.findViewById(R.id.rate_button);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.rate_popup, null);

                SmileRating smileRating = (SmileRating)popupView.findViewById(R.id.smile_rating);


                smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                                                             @Override
                                                             public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {

                                                                 rating=smiley;
                                                             }
                                                         }
                );



                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched

                Button submit = popupView.findViewById(R.id.submit_button);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int rating will be sent
                        // text also
                        popupWindow.dismiss();

                    }
                });


            }
        });

        ImageView profileImage = view.findViewById(R.id.profile_image);
        Glide.with(getContext())
                .load(UserConstants.photoUri)
                .centerInside()
                .placeholder(R.drawable.profile_photo)
                .into(profileImage);
        TextView displayName = view.findViewById(R.id.display_name);
        displayName.setText(UserConstants.displayName);

        TextView logoutView = view.findViewById(R.id.tv_logout_setting);
        logoutView.append(" (" + UserConstants.email + ")");
        language_setting=view.findViewById(R.id.language_setting);
        language_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LanguageSettingActivity.class));
            }
        });
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
