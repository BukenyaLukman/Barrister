package com.mobilesoftware.barrister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class SavedAttorneyActivity extends AppCompatActivity {
        private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_attorney);

        mToolbar = findViewById(R.id.client_saved_attorney);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attorneys Saved");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
