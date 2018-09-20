package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.nguyenvukhanhuygmail.environmental_protection.adapter.Problem_image_adapter;
import com.nguyenvukhanhuygmail.environmental_protection.ultil.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProblemActivity extends AppCompatActivity {

    EditText header_field, describe_field;
    TextView tv_location, add_image;
    RecyclerView rv_image_problem;
    Button submit_button;

    Problem_image_adapter problem_image_adapter;

    List<byte[]> image_problem = new ArrayList<>();

    private int index = 0;

    private final int PLACE_PICKER_REQUEST = 1, IMAGE_PICKER_REQUEST = 2, IMAGE_TAKER_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        start();
        onButtonClick();
        onImageProblemClick();
    }

    private void onImageProblemClick() {
        rv_image_problem.addOnItemTouchListener(new RecyclerItemClickListener(getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                index = position;
                PickOrTakeDialog(
                        "Thay đổi ảnh"
                );
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                tv_location.setText(PlacePicker.getPlace(data, this).getName());
            } else if (requestCode == IMAGE_PICKER_REQUEST) {
                addImageToRV(data);
            } else if (requestCode == IMAGE_TAKER_REQUEST) {
                addImageToRV(data);
            } else {

            }
        }

    }

    private void addImageToRV(Intent data) {
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        bmp.recycle();
        if (index != image_problem.size()) {
            image_problem.set(index, image);
        } else {
            image_problem.add(image);
        }
        problem_image_adapter = new Problem_image_adapter(image_problem);
        rv_image_problem.setAdapter(problem_image_adapter);
    }

    private void takePhoto(int code) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, code);

    }

    private void choosePhoto(int code) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, code);

    }

    private void showMapPicker(int code) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddProblemActivity.this), code);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    private void PickOrTakeDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle(title);
        builder.setMessage("Chọn từ thiết bị hay chụp ảnh?");
        builder.setCancelable(true);
        builder.setPositiveButton("Chọn ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choosePhoto(IMAGE_PICKER_REQUEST);
            }
        });
        builder.setNegativeButton("Chụp ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                takePhoto(IMAGE_TAKER_REQUEST);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void onButtonClick() {

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMapPicker(PLACE_PICKER_REQUEST);
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    index = image_problem.size();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                PickOrTakeDialog(
                        "Thêm ảnh cho vấn đề"
                );
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void start() {

        getSupportActionBar().setTitle(R.string.add_problem);

        rv_image_problem = (RecyclerView) findViewById(R.id.rv_image_problem);
        rv_image_problem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        header_field = (EditText) findViewById(R.id.problem_title);
        describe_field = (EditText) findViewById(R.id.describe_problem);
        tv_location = (TextView) findViewById(R.id.place_picker_problem);
        add_image = (TextView) findViewById(R.id.image_picker_problem);
        submit_button = (Button) findViewById(R.id.add_problem_button);

    }
}
