package com.mobilesoftware.barrister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttorneyDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference DetailsRef;
    private FirebaseUser mUser;
    private CircleImageView ImageViewAtt;
    private TextView NameOfAtt,AreaOfPractice,PhoneNumber;
    private String AttorneyID = "";
    private Button GetAttorneyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attorney_details);

        mToolbar = findViewById(R.id.my_details_app_bar);

        AttorneyID = getIntent().getStringExtra("pid");


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DetailsRef = FirebaseDatabase.getInstance().getReference().child("AttorneyPersonalData");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Get Attorney");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NameOfAtt = findViewById(R.id.details_name_of_attorney);
        AreaOfPractice = findViewById(R.id.details_area_of_practice);
        PhoneNumber = findViewById(R.id.details_phone_number);
        ImageViewAtt = findViewById(R.id.details_profile_image);
        GetAttorneyBtn = findViewById(R.id.details_submit_btn);



        GetAttorneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttorneyDetailsActivity.this, ClientPostProjectActivity.class);
                intent.putExtra("pid",AttorneyID);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        retriveAttorneyDetails();
    }

    private void retriveAttorneyDetails() {
        DetailsRef.child(AttorneyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()){
                  Search search = dataSnapshot.getValue(Search.class);
                  NameOfAtt.setText(search.getFullName());
                  AreaOfPractice.setText(search.getAreaOfPractice());
                  PhoneNumber.setText(search.getPhone());

                  Picasso.get().load(search.getImage()).into(ImageViewAtt);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
