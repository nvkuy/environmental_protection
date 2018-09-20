package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    ListView lv_problems;
    FloatingActionButton add_problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        onAddProblemButtonClick();
    }

    private void onAddProblemButtonClick() {
        add_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddProblemAct();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        mAuth.signOut();
        finish();
        return true;
    }

    private void gotoAddProblemAct() {
        Intent i = new Intent(getApplication(), AddProblemActivity.class);
        i.putExtra("uID", user.getUid());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_problems = (ListView) findViewById(R.id.lv_problems);
        add_problem = (FloatingActionButton) findViewById(R.id.add_problem);

    }

}
