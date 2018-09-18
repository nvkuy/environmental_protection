package com.nguyenvukhanhuygmail.environmental_protection.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenvukhanhuygmail.environmental_protection.MainActivity;
import com.nguyenvukhanhuygmail.environmental_protection.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email_text_field, password_text_field;
    Button login_button;
    TextView gotoResgister_button;

    private FirebaseAuth mAuth;

    final Pattern email_pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    enum LoginState {
        ok,
        null_field,
        invalid_email,
        invalid_pass
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        start();
        onButtonClick();
    }

    private void onButtonClick() {

        gotoResgister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegister();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginState state = isOk(email_text_field.getText().toString(), password_text_field.getText().toString());
                if (state == LoginState.ok) {
                    mAuth.signInWithEmailAndPassword(email_text_field.getText().toString(), password_text_field.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
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
                            toastMsg("Độ dài mật khẩu phải lớn hơn 6 ký tự!");
                            break;
                        case invalid_email:
                            toastMsg("Email không đúng định dạng");
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void gotoRegister() {
        startActivity(new Intent(getApplication(), RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void gotoMainAct() {
        startActivity(new Intent(getApplication(), MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private LoginState isOk(String email, String pass) {

        if (email.equals("") || pass.equals(""))
            return LoginState.null_field;
        if (!email_pattern.matcher(email).find()) return LoginState.invalid_email;
        if (pass.length() < 6) return LoginState.invalid_pass;

        return LoginState.ok;
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) gotoMainAct();

        email_text_field = (EditText) findViewById(R.id.email_text_field);
        password_text_field = (EditText) findViewById(R.id.password_text_field);
        login_button = (Button) findViewById(R.id.login_button);
        gotoResgister_button = (TextView) findViewById(R.id.gotoRegister_button);
    }

}
