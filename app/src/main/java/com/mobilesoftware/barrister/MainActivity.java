package com.mobilesoftware.barrister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.mobilesoftware.barrister.Attorney.AttorneyLoginActivity;
import com.mobilesoftware.barrister.Attorney.AttorneyRegisterActivity;

public class MainActivity extends AppCompatActivity {

    private EditText UserEmail,UserPassword;
    private Button UserLogin,UserCreateAccount,AttorneyBtn,ClientBtn;
    private Dialog LoginDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);





        UserLogin = findViewById(R.id.login_button);
        UserCreateAccount = findViewById(R.id.create_account);


        UserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDialog = new Dialog(MainActivity.this);
                LoginDialog.setContentView(R.layout.options_login);
                LoginDialog.setCancelable(true);
                LoginDialog.show();

                ClientBtn = LoginDialog.findViewById(R.id.client_btn);
                AttorneyBtn = LoginDialog.findViewById(R.id.lawyer_btn);

                AttorneyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginDialog.dismiss();
                        GoToAttorneyActivityLogin();
                    }
                });
                ClientBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginDialog.dismiss();
                        GoToClientActivityLogin();
                    }
                });
            }
        });


        UserCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginDialog = new Dialog(MainActivity.this);
                LoginDialog.setContentView(R.layout.options_login);
                LoginDialog.setCancelable(true);
                LoginDialog.show();

                ClientBtn = LoginDialog.findViewById(R.id.client_btn);
                AttorneyBtn = LoginDialog.findViewById(R.id.lawyer_btn);

                AttorneyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginDialog.dismiss();
                        GoToAttorneyRegisterActivityLogin();
                    }
                });

                ClientBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoToClientRegisterActivityLogin();
                    }
                });

            }
        });
    }




    private void GoToAttorneyRegisterActivityLogin(){
        Intent clientIntent = new Intent(MainActivity.this, AttorneyRegisterActivity.class);
        startActivity(clientIntent);
    }

    private void GoToClientRegisterActivityLogin(){
        Intent clientIntent = new Intent(MainActivity.this,ClientRegisterActivity.class);
        startActivity(clientIntent);
    }

    private void GoToClientActivityLogin() {
        Intent clientIntent = new Intent(MainActivity.this,ClientLoginActivity.class);
        startActivity(clientIntent);
    }

    private void GoToAttorneyActivityLogin() {
        Intent attorneyIntent = new Intent(MainActivity.this, AttorneyLoginActivity.class);
        startActivity(attorneyIntent);
    }

}
