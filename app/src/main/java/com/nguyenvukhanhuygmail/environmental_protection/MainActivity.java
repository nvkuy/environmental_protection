package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenvukhanhuygmail.environmental_protection.model.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    String uID;
    User user;

    ListView lv_problems;
    FloatingActionButton add_problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        setupLvProblem();
        onAddProblemButtonClick();
    }

    private void setupLvProblem() {

        dbRef.child("Problems").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        startActivity(new Intent(getApplication(), AddProblemActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_problems = (ListView) findViewById(R.id.lv_problems);
        add_problem = (FloatingActionButton) findViewById(R.id.add_problem);

        dbRef.child("Users").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.getAcc_type().equals("Người dân")) {
                    add_problem.setVisibility(View.VISIBLE);
                } else {
                    add_problem.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
