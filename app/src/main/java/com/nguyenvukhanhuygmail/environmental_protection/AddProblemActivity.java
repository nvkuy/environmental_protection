package com.nguyenvukhanhuygmail.environmental_protection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import java.util.List;

public class AddProblemActivity extends AppCompatActivity {

    EditText header_field, describe_field;
    TextView tv_location, add_image;
    RecyclerView rv_image_problem;
    Button submit_button;

    Problem_image_adapter problem_image_adapter;

    List<byte[]> image_problem;

    private final int PLACE_PICKER_REQUEST = 1, IMAGE_PICKER_REQUEST = 2, IMAGE_TAKER_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        start();
        onButtonClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                tv_location.setText(PlacePicker.getPlace(data, this).getName());
            } else if (requestCode == IMAGE_PICKER_REQUEST) {

            } else if (requestCode == IMAGE_TAKER_REQUEST) {

            } else {

            }
        }

    }

    public static class PickOrTakeDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.PickOrTakeDialogMsg)
                    .setPositiveButton(R.string.TakeButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.PickButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private void onButtonClick() {

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddProblemActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
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
        problem_image_adapter = new Problem_image_adapter(image_problem);
        rv_image_problem.setAdapter(problem_image_adapter);

        header_field = (EditText) findViewById(R.id.problem_title);
        describe_field = (EditText) findViewById(R.id.describe_problem);
        tv_location = (TextView) findViewById(R.id.place_picker_problem);
        add_image = (TextView) findViewById(R.id.add_problem);
        submit_button = (Button) findViewById(R.id.add_problem_button);

    }
}
