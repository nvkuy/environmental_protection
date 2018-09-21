package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenvukhanhuygmail.environmental_protection.adapter.ProblemAdapter;
import com.nguyenvukhanhuygmail.environmental_protection.model.Problem;
import com.nguyenvukhanhuygmail.environmental_protection.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private StorageReference sRef;
    String uID;
    User user;

    RecyclerView rv_problems;
    TextView tv_null_problem;
    FloatingActionButton add_problem;

    List<Problem> problemList = new ArrayList<>();
    ProblemAdapter problemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        showTvOrRv();
        setupLvProblem();
        onAddProblemButtonClick();
    }

    private void showTvOrRv() {
        if (problemList.isEmpty()) {
            tv_null_problem.setVisibility(View.VISIBLE);
            rv_problems.setVisibility(View.GONE);
        } else {
            tv_null_problem.setVisibility(View.GONE);
            rv_problems.setVisibility(View.VISIBLE);
        }
    }

    private void updateRV() {
        showTvOrRv();
        problemAdapter = new ProblemAdapter(problemList, user.getAcc_type(), dbRef, sRef);
        rv_problems.setAdapter(problemAdapter);
    }

    private void setupLvProblem() {

        dbRef.child("Problems").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Problem problem = dataSnapshot.getValue(Problem.class);
                problemList.add(problem);
                updateRV();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Problem problem = dataSnapshot.getValue(Problem.class);
                problemList.set(findItem(problem.getImage_code()), problem);
                updateRV();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Problem problem = dataSnapshot.getValue(Problem.class);
                problemList.remove(findItem(problem.getImage_code()));
                updateRV();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private int findItem(String image_code) {

        int i = 0;
        while (!image_code.equals(problemList.get(i).getImage_code())) {
            i++;
        }

        return i;
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_problems = (RecyclerView) findViewById(R.id.rv_problems);
        rv_problems.setLayoutManager(new LinearLayoutManager(this));
        tv_null_problem = (TextView) findViewById(R.id.tv_null_problem);
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
