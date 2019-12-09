package com.mobilesoftware.barrister;

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
import com.mobilesoftware.barrister.Attorney.AttorneyHomeActivity;
import com.mobilesoftware.barrister.Attorney.AttorneyLoginActivity;

public class ClientLoginActivity extends AppCompatActivity {

    private EditText CliEmail,CliPassword;
    private TextView CliForgetPassword,CliNeedNewAccount;
    private Button CliLoginBtn;

    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        mAuth = FirebaseAuth.getInstance();

        initializeFields();

        CliNeedNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToClientRegisterActivity();
            }
        });

        CliLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginClient();
            }
        });
    }

    private void LoginClient() {
        String email = CliEmail.getText().toString();
        String password = CliPassword.getText().toString();

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
                        Toast.makeText(ClientLoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        SendUserToHomeActivity();


                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(ClientLoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }


                }
            });

        }
    }

    private void SendUserToHomeActivity() {
        Intent homeIntent = new Intent(ClientLoginActivity.this, ClientHomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    private void initializeFields(){
        CliEmail = findViewById(R.id.client_email_input);
        CliPassword = findViewById(R.id.client_password_input_login);
        CliLoginBtn = findViewById(R.id.client_login_btn);
        CliForgetPassword = findViewById(R.id.client_forget_password);
        CliNeedNewAccount = findViewById(R.id.client_need_new_account);
        loadingBar = new ProgressDialog(this);

    }

    private void sendUserToClientRegisterActivity(){
        Intent registerIntent = new Intent(ClientLoginActivity.this,ClientRegisterActivity.class);
        startActivity(registerIntent);
    }
}
