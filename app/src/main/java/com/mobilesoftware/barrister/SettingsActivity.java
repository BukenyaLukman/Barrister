package com.mobilesoftware.barrister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media.AudioAttributesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mobilesoftware.barrister.Attorney.AttorneyHomeActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,AreaOfPracticeEditText;
    private TextView profileChangeTextBtn, CloseTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private FirebaseUser mAuth;
    private String checker = "";
    private DatabaseReference usersRef;
    private String FullName,PhoneNumber,AreaOfPractice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("PicturesLegal");

        usersRef = FirebaseDatabase.getInstance().getReference().child("AttorneyPersonalData");
        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        userInfoDisplay();

        initializeFields();

        CloseTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserInfoSaved();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });
    }


    private void initializeFields() {

        profileImageView = findViewById(R.id.settings_profile_image);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        AreaOfPracticeEditText= findViewById(R.id.settings_area_of_practice);
        profileChangeTextBtn = findViewById(R.id.profile_image_change_btn);
        CloseTextBtn = findViewById(R.id.close_settings_btn);
        saveTextButton = findViewById(R.id.update_account_settings_btn);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Error: Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }


    }

    private void userInfoSaved()
    {
        FullName = fullNameEditText.getText().toString();
        AreaOfPractice = AreaOfPracticeEditText.getText().toString();
        PhoneNumber = userPhoneEditText.getText().toString();

        if (TextUtils.isEmpty(FullName)){
            Toast.makeText(this, "Please provide your name...", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(AreaOfPractice)){
            Toast.makeText(this, "Please provide your Area of Practice...", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(PhoneNumber)){
            Toast.makeText(this, "Please provide your phone number...", Toast.LENGTH_SHORT).show();

        }else if(checker.equals("clicked")){
            uploadImage();
        }else{
            updateOnlyUserInfoSaved();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfilePictureRef.child(imageUri.getLastPathSegment() + mAuth.getUid() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String,Object> attorneyMap = new HashMap<>();
                        attorneyMap.put("FullName",FullName);
                        attorneyMap.put("AreaOfPractice",AreaOfPractice);
                        attorneyMap.put("Phone",PhoneNumber);
                        attorneyMap.put("image",myUri);

                        usersRef.child(mAuth.getUid()).updateChildren(attorneyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    SendAttorneyToHomeActivity();
                                    Toast.makeText(SettingsActivity.this, "Settings Info updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    progressDialog.dismiss();
                                    String message = task.getException().toString();
                                    Toast.makeText(SettingsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Image not Selected", Toast.LENGTH_SHORT).show();
        }


        }

    private void SendAttorneyToHomeActivity() {
        Intent homeIntent = new Intent(SettingsActivity.this,AttorneyHomeActivity.class);
        startActivity(homeIntent);
    }


    private void updateOnlyUserInfoSaved() {

            FullName = fullNameEditText.getText().toString();
            AreaOfPractice = AreaOfPracticeEditText.getText().toString();
            PhoneNumber = userPhoneEditText.getText().toString();

            if (TextUtils.isEmpty(FullName)){
                Toast.makeText(this, "Please provide your name...", Toast.LENGTH_SHORT).show();

            }else if (TextUtils.isEmpty(AreaOfPractice)){
                Toast.makeText(this, "Please provide your Area of Practice...", Toast.LENGTH_SHORT).show();

            }else if (TextUtils.isEmpty(PhoneNumber)){
                Toast.makeText(this, "Please provide your phone number...", Toast.LENGTH_SHORT).show();

            }else {

                HashMap<String,Object> userMap = new HashMap<>();
                userMap.put("FullName",FullName);
                userMap.put("AreaOfPractice",AreaOfPractice);
                userMap.put("Phone",PhoneNumber);
                usersRef.child(mAuth.getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           startActivity(new Intent(SettingsActivity.this, AttorneyHomeActivity.class));
                           Toast.makeText(SettingsActivity.this, "Profile Info updated successfully", Toast.LENGTH_SHORT).show();
                           finish();
                       }else{
                           String message = task.getException().toString();
                           Toast.makeText(SettingsActivity.this, "Error "+ message, Toast.LENGTH_SHORT).show();
                       }
                    }
                });


            }

        }



        private void userInfoDisplay() {

            usersRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("image").exists()) {
                            String Image = dataSnapshot.child("image").getValue().toString();
                            String fullName = dataSnapshot.child("FullName").getValue().toString();
                            String phone = dataSnapshot.child("Phone").getValue().toString();
                            String areaOfPractice = dataSnapshot.child("AreaOfPractice").getValue().toString();

                            Picasso.get().load(Image).into(profileImageView);

                            fullNameEditText.setText(fullName);
                            userPhoneEditText.setText(phone);
                            AreaOfPracticeEditText.setText(areaOfPractice);

                        }else{

                            Toast.makeText(SettingsActivity.this, "Please update your profile info", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
