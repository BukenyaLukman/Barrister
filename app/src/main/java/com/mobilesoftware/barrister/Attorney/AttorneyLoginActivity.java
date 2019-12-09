package com.mobilesoftware.barrister.Attorney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobilesoftware.barrister.R;

public class AttorneyLoginActivity extends AppCompatActivity {

    private EditText AttEmail,AttPassword;
    private TextView AttForgetPassword,AttNeedNewAccount;
    private Button AttLoginBtn;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attorney_login);

        mAuth = FirebaseAuth.getInstance();

        initializeFields();

        AttLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAttorney();
            }
        });

        AttNeedNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToAttorneyRegisterActivity();
            }
        });

    }

    private void LoginAttorney() {
        String email = AttEmail.getText().toString();
        String password = AttPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Provide Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Signing in...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loadingBar.dismiss();
                        Toast.makeText(AttorneyLoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        SendUserToHomeActivity();


                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(AttorneyLoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }


                }
            });

        }
    }

    private void SendUserToHomeActivity() {
        Intent homeIntent = new Intent(AttorneyLoginActivity.this, AttorneyHomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    private void initializeFields(){
        AttEmail = findViewById(R.id.attorney_email_input);
        AttPassword = findViewById(R.id.attorney_password_input_login);
        AttLoginBtn = findViewById(R.id.attorney_login_btn);
        AttForgetPassword = findViewById(R.id.attorney_forget_password);
        AttNeedNewAccount = findViewById(R.id.attorney_need_new_account);
        loadingBar = new ProgressDialog(this);


    }

    private void sendUserToAttorneyRegisterActivity(){
        Intent registerIntent = new Intent(AttorneyLoginActivity.this, AttorneyRegisterActivity.class);
        startActivity(registerIntent);
    }
}
