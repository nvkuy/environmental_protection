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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nguyenvukhanhuygmail.environmental_protection.util.RecyclerItemClickListener;

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
        showFullInfoProblem();
    }

    private void showFullInfoProblem() {
        rv_problems.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getApplication(), FullInfoProblemActivity.class);
                i.putExtra("uID", uID);
                i.putExtra("date", problemList.get(position).getDate());
                i.putExtra("title", problemList.get(position).getTitle());
                startActivity(i);
            }
        }));
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
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_from_bottom);
        rv_problems.setLayoutAnimation(controller);
        problemAdapter = new ProblemAdapter(problemList, user.getAcc_type(), dbRef, sRef, uID);
        rv_problems.setAdapter(problemAdapter);
        rv_problems.scheduleLayoutAnimation();

    }

    private void setupLvProblem() {

//        DatabaseReference problemRef;
//        if (user.getAcc_type().equals("Người dân")) {
//            problemRef = dbRef.child("Problems").child(uID);
//        } else {
//            problemRef = dbRef.child("Problems");
//        }
        problemList.clear();
        if (user.getAcc_type().equals("Người dân")) { //nếu đăng nhập dưới quyền người dân
            dbRef.getRoot().child("Problems").child(uID).addChildEventListener(new ChildEventListener() {
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
        } else { //nếu đăng nhập dưới quyền đơn vị chức năng
            dbRef.getRoot().child("Problems").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot uIDs : dataSnapshot.getChildren()) {
                        Problem problem = uIDs.getValue(Problem.class);
                        problemList.add(problem);
                        updateRV();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot uIDs : dataSnapshot.getChildren()) {
                        Problem problem = uIDs.getValue(Problem.class);
                        problemList.set(findItem(problem.getImage_code()), problem);
                        updateRV();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot uIDs : dataSnapshot.getChildren()) {
                        Problem problem = uIDs.getValue(Problem.class);
                        problemList.remove(findItem(problem.getImage_code()));
                        updateRV();
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                if (user != null) {
                    if (user.getAcc_type().equals("Người dân")) {
                        showTvOrRv();
                        setupLvProblem();
                        onAddProblemButtonClick();
                    } else {
                        add_problem.setVisibility(View.GONE);
                    }
                } else {
                    toastMsg("Lỗi đăng nhập người dùng");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
