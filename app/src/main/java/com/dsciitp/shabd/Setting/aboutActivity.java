package com.dsciitp.shabd.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dsciitp.shabd.R;

public class aboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_shabd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));

    }
}