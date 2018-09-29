package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenvukhanhuygmail.environmental_protection.adapter.ViewPaperAdapter;
import com.nguyenvukhanhuygmail.environmental_protection.model.Problem;
import com.nguyenvukhanhuygmail.environmental_protection.model.User;
import com.nguyenvukhanhuygmail.environmental_protection.util.ClickableViewPager;

import java.util.ArrayList;
import java.util.List;

public class FullInfoProblemActivity extends AppCompatActivity {

    String uID, date, title;

    TextView tv_problem_describe, tv_problem_location, tv_problem_post_time, tv_user_name, tv_user_sex, tv_user_phone_num, tv_user_location;
    ClickableViewPager vp_image_problem;

    ViewPaperAdapter viewPaperAdapter;
    List<String> image_url = new ArrayList<>();

    User user;
    Problem problem;

    private DatabaseReference mDatabase;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info_problem);
        start();
        getData();
        showFullScreenImage();
    }

    private void showFullScreenImage() {
        vp_image_problem.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplication(), FullscreenImageActivity.class).putExtra("image_url", image_url.get(position)));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void updateVP() {
        viewPaperAdapter.notifyDataSetChanged();
    }

    private void getData() {

        mDatabase.child("Users").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                tv_user_name.setText("Tên người gửi: " + user.getUser_name());
                tv_user_location.setText("Địa chỉ: " + user.getLocation());
                tv_user_sex.setText("Giới tính: " + user.getSex());
                tv_user_phone_num.setText("Số điện thoại: " + user.getPhone_number());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Problems").child(uID).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                problem = dataSnapshot.getValue(Problem.class);

                tv_problem_describe.setText("Mô tả vấn đề: " + problem.getDescribe());
                tv_problem_location.setText("Xảy ra tại: " + problem.getLocation());
                tv_problem_post_time.setText("Thời điểm báo cáo vấn đề: " + problem.getDate());

                for (int i = 1; i <= problem.getImage_num(); i++) {
                    storageRef.child("ProblemImage").child(problem.getImage_code()).child(String.valueOf(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_url.add(String.valueOf(uri));
                            updateVP();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            toastMsg("Đã xảy ra lỗi khi lấy hình ảnh từ server!");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void start() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Intent i = getIntent();
        uID = i.getStringExtra("uID");
        date = i.getStringExtra("date");
        title = i.getStringExtra("title");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        tv_problem_describe = (TextView) findViewById(R.id.problem_describe);
        tv_problem_location = (TextView) findViewById(R.id.location_problem);
        tv_problem_post_time = (TextView) findViewById(R.id.post_time);
        tv_user_name = (TextView) findViewById(R.id.user_name);
        tv_user_location = (TextView) findViewById(R.id.user_location);
        tv_user_phone_num = (TextView) findViewById(R.id.user_phone_number);
        tv_user_sex = (TextView) findViewById(R.id.user_sex);
        vp_image_problem = (ClickableViewPager) findViewById(R.id.vp_image_problem);

        viewPaperAdapter = new ViewPaperAdapter(image_url, this);
        vp_image_problem.setAdapter(viewPaperAdapter);

    }
}
