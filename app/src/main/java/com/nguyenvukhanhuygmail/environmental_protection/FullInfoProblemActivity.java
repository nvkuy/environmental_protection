package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FullInfoProblemActivity extends AppCompatActivity {

    String uID, date, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info_problem);
        start();
        setUpProblem();
    }

    private void setUpProblem() {
    }

    private void start() {

        Intent i = getIntent();
        uID = i.getStringExtra("uID");
        date = i.getStringExtra("date");
        title = i.getStringExtra("title");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

    }
}
