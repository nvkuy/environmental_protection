package com.nguyenvukhanhuygmail.environmental_protection.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenvukhanhuygmail.environmental_protection.MainActivity;
import com.nguyenvukhanhuygmail.environmental_protection.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText user_name_text_field, phone_number_field, email_field, password_field, repass_field;
    TextView place_picker_field;
    Spinner sex_spinner, acc_type_spinner;
    Button register_button;

    enum RegisterState {
        ok,
        null_field,
        invalid_phone_number,
        invalid_email,
        invalid_pass,
        invalid_repass
    }

    final String sex[] = {
            "Nam",
            "Nữ",
            "Đồng tính"
    };

    final String acc_type[] = {
            "Người dân",
            "Đơn vị chức năng"
    };

    final Pattern email_pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private final int PLACE_PICKER_REQUEST = 1;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        start();
        onButtonClick();
    }

    private void saveInfo(FirebaseUser user) {
        Map<String, String> mUser = new HashMap<>();
        mUser.put("user_name", email_field.getText().toString());
        mUser.put("phone_number", phone_number_field.getText().toString());
        mUser.put("sex", sex_spinner.getSelectedItem().toString());
        mUser.put("acc_type", acc_type_spinner.getSelectedItem().toString());
        mUser.put("location", place_picker_field.getText().toString());

        mDatabase.child("Users").child(user.getUid()).setValue(mUser);
    }

    private void onButtonClick() {

        place_picker_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(RegisterActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterState state = isOk(
                        user_name_text_field.getText().toString(),
                        phone_number_field.getText().toString(),
                        place_picker_field.getText().toString(),
                        email_field.getText().toString(),
                        password_field.getText().toString(),
                        repass_field.getText().toString());
                if (state == RegisterState.ok) {
                    mAuth.createUserWithEmailAndPassword(email_field.getText().toString(), password_field.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
//                                        Log.d("uID", mAuth.getCurrentUser().getUid());
                                        saveInfo(mAuth.getCurrentUser());
                                        gotoMainAct();
                                    } else {
                                        toastMsg("Lỗi tạo tài khoản");
                                    }
                                }
                            });
                } else {
                    switch (state) {
                        case null_field:
                            toastMsg("Vui lòng đầy đủ thông tin!");
                            break;
                        case invalid_pass:
                            toastMsg("Độ dài mật khẩu phải lớn hoặc bằng hơn 6 ký tự!");
                            break;
                        case invalid_email:
                            toastMsg("Email không đúng định dạng");
                            break;
                        case invalid_repass:
                            toastMsg("Xác nhận mật khẩu không khớp");
                            break;
                        case invalid_phone_number:
                            toastMsg("Độ dài số điện thoại không hợp lý");
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PLACE_PICKER_REQUEST) {
            place_picker_field.setText(PlacePicker.getPlace(data, this).getName());
        }

    }

    private void gotoMainAct() {
        startActivity(new Intent(getApplication(), MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private RegisterState isOk(String user_name, String phone_number, String location, String email, String pass, String repass) {

        if (user_name.equals("") || phone_number.equals("") || location.equals("") || email.equals("") || pass.equals(""))
            return RegisterState.null_field;
        if (phone_number.length() != 10 && phone_number.length() != 11) return RegisterState.invalid_phone_number;
        if (!email_pattern.matcher(email).find()) return RegisterState.invalid_email;
        if (pass.length() < 6) return RegisterState.invalid_pass;
        if (!pass.equals(repass)) return RegisterState.invalid_repass;

        return RegisterState.ok;
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setTitle(R.string.register_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_name_text_field = (EditText) findViewById(R.id.user_name_text_field);
        phone_number_field = (EditText) findViewById(R.id.phone_number_field);
        place_picker_field = (TextView) findViewById(R.id.place_picker_field);
        email_field = (EditText) findViewById(R.id.email_text_field);
        password_field = (EditText) findViewById(R.id.password_text_field);
        repass_field = (EditText) findViewById(R.id.repassword_text_field);

        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                sex
        );
        sexAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sex_spinner.setAdapter(sexAdapter);
        acc_type_spinner = (Spinner) findViewById(R.id.account_type_spinner);
        ArrayAdapter<String> acc_typeAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                acc_type
        );
        acc_typeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        acc_type_spinner.setAdapter(acc_typeAdapter);

        register_button = (Button) findViewById(R.id.register_button);

    }

}
