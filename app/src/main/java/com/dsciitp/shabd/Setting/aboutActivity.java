package com.dsciitp.shabd.Setting;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dsciitp.shabd.R;

import java.util.ArrayList;

public class aboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_shabd);

        TextView textView = (TextView) findViewById(R.id.overview_text);

        textView.setText(new SpannableString(textView.getText()));

        TextJustification.justify(textView);

    }
}