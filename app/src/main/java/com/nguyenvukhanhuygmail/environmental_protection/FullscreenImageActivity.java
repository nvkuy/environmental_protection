package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullscreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết ảnh của báo cáo");

        Intent i = getIntent();
        String url = i.getStringExtra("image_url");

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        Picasso
                .get()
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(photoView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
