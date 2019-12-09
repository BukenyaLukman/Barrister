package com.mobilesoftware.barrister.Attorney;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobilesoftware.barrister.MainActivity;
import com.mobilesoftware.barrister.R;
import com.mobilesoftware.barrister.SettingsActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttorneyHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView ProfileImageHome,SettingsView;
    private Button MyProfileBtn;
    private TextView EducationLink;
    private FirebaseUser mAuth;
    private DatabaseReference AttorneyRef;
    private TextView UserNameView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attorney_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);



        AttorneyRef = FirebaseDatabase.getInstance().getReference().child("AttorneyPersonalData");
        mAuth = FirebaseAuth.getInstance().getCurrentUser();


        ProfileImageHome = findViewById(R.id.profile_home_picture);
        MyProfileBtn = findViewById(R.id.my_profile_btn);
        EducationLink = findViewById(R.id.education_link);

        EducationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent educIntent = new Intent(AttorneyHomeActivity.this, EducationDetailsActivity.class);
                startActivity(educIntent);
            }
        });

        MyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(AttorneyHomeActivity.this, MyProfileActivity.class);
                startActivity(profileIntent);
            }
        });



        AttorneyRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myFullName = dataSnapshot.child("FullName").getValue().toString();
                    UserNameView.setText(myFullName);
                    if(dataSnapshot.hasChild("image")){
                        final String myProfileImage = dataSnapshot.child("image").getValue().toString();
                        if(myProfileImage.isEmpty()){
                            ProfileImageHome.setImageResource(R.drawable.profile);
                            SettingsView.setImageResource(R.drawable.profile);
                        }else{
                            Picasso.get().load(myProfileImage).into(SettingsView);
                            Picasso.get().load(myProfileImage).into(ProfileImageHome);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttorneyHomeActivity.this, "Error: "+ databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        UserNameView = headerView.findViewById(R.id.attorney_username);
        SettingsView = headerView.findViewById(R.id.attorney_profile_image);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attorney_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_profile){
            Intent intent = new Intent(AttorneyHomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_projects) {
            Intent intent = new Intent(AttorneyHomeActivity.this, MyProjectsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_applications) {
            Intent intent = new Intent(AttorneyHomeActivity.this, MyApplicationsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(AttorneyHomeActivity.this, MyNotificationsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_settings) {
            Intent intent = new Intent(AttorneyHomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            Intent intent = new Intent(AttorneyHomeActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
