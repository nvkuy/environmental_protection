package com.nguyenvukhanhuygmail.environmental_protection;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenvukhanhuygmail.environmental_protection.adapter.Problem_image_adapter;
import com.nguyenvukhanhuygmail.environmental_protection.model.Problem;
import com.nguyenvukhanhuygmail.environmental_protection.util.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    enum AddProblemState {
        null_header,
        null_describe,
        null_location,
        null_image,
        ok
    }

    String uID;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;

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
                Uri image_uri = data.getData();
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    addImageToRV(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_TAKER_REQUEST) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                addImageToRV(bmp);
            } else {

            }
        }

    }

    private void addImageToRV(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        bmp.recycle();
        if (index != image_problem.size()) {
            image_problem.set(index, image);
        } else {
            image_problem.add(image);
        }
        updateRV();
    }

    private void addProblemAndBackToMainAct(String uID) {

        final Problem problem = new Problem(
                header_field.getText().toString(),
                describe_field.getText().toString(),
                Calendar.getInstance().getTime().toString(),
                false,
                tv_location.getText().toString(),
                uID
        );
        mDatabase.child("Problems").push().setValue(problem)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadImage(problem.getImage_code());
                    toastMsg("Thêm vấn đề thành công!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMsg("Thêm vấn đề thất bại!");
                }
            });
    }

    private void takePhoto(int code) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, code);

    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
        if (title.equals("Thay đổi ảnh") && !image_problem.isEmpty()) {
            builder.setNeutralButton("Xóa ảnh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    image_problem.remove(index);
                    updateRV();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void updateRV() {
        problem_image_adapter = new Problem_image_adapter(image_problem);
        rv_image_problem.setAdapter(problem_image_adapter);
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
                AddProblemState state = isOk();
                if (state == AddProblemState.ok) {
                    addProblemAndBackToMainAct(uID);
                } else {
                    switch (state) {
                        case null_header:
                            toastMsg("Tiêu đề không được bỏ trống!");
                            break;
                        case null_describe:
                            toastMsg("Mô tả không được bỏ trống");
                            break;
                        case null_image:
                            toastMsg("Vấn đề không đáng tin, cần có ít nhất 1 ảnh");
                            break;
                        case null_location:
                            toastMsg("Vấn đề không đáng tin, cần vị trí");
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    private void uploadImage(String image_code) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("ProblemImage").child(image_code);
        for(byte[] image : image_problem) {
            UploadTask uploadTask = imageRef.putBytes(image);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    toastMsg("Lưu ảnh thất bại!");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    toastMsg("Lưu ảnh thành công!");
                    finish();
                }
            });
        }
    }

    private AddProblemState isOk() {

        if (header_field.getText().toString().equals(""))
            return AddProblemState.null_header;

        if (describe_field.getText().toString().equals(""))
            return AddProblemState.null_describe;

        if (tv_location.getText().toString().equals(""))
            return AddProblemState.null_location;

        if (image_problem.isEmpty())
            return AddProblemState.null_image;

        return AddProblemState.ok;
    }

    private void start() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().setTitle(R.string.add_problem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rv_image_problem = (RecyclerView) findViewById(R.id.rv_image_problem);
        rv_image_problem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        header_field = (EditText) findViewById(R.id.problem_title);
        describe_field = (EditText) findViewById(R.id.describe_problem);
        tv_location = (TextView) findViewById(R.id.place_picker_problem);
        add_image = (TextView) findViewById(R.id.image_picker_problem);
        submit_button = (Button) findViewById(R.id.add_problem_button);

    }
}
