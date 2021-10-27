package com.lettherebelight;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText editTxtFullName, editTxtCompanyName;
    private RadioButton radioBtnApprentice, radioBtnJourneyman, radioBtnMaster;
    private Button btnUpdateAccount;
    private CircleImageView profilePic;

    private Uri imageUri;
    private User newUser = new User();
    ActivityResultLauncher<Intent> profilePicActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    imageUri = data.getData();
                    profilePic.setImageURI(imageUri);
                }
            }
    );

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference StorageRef;
    FirebaseFirestore dataBase;
    String position = "unknown";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);

        editTxtFullName = findViewById(R.id.editTxtFullName);
        editTxtCompanyName = findViewById(R.id.editTxtCompanyName);
        radioBtnApprentice = findViewById(R.id.radioBtnApprentice);
        radioBtnJourneyman = findViewById(R.id.radioBtnJourneyman);
        radioBtnMaster = findViewById(R.id.radioBtnMaster);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(this);
        profilePic = findViewById(R.id.profile_image);
        profilePic.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        dataBase = FirebaseFirestore.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_image: {
                chooseProfilePic();
            }

            case R.id.btnUpdateAccount: {
                updateAccount();
            }
        }
    }

    private void updateAccount() {
        String fullName = editTxtFullName.getText().toString().trim();
        String companyName = editTxtCompanyName.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTxtFullName.setError("Full name is required");
            editTxtFullName.requestFocus();
            return;
        } else if (companyName.isEmpty()) {
            editTxtCompanyName.setError("Company name is required");
            editTxtCompanyName.requestFocus();
            return;
        } else if (radioBtnMaster.isChecked()) {
            position = "Master";
        } else if (radioBtnJourneyman.isChecked()) {
            position = "Journeyman";
        } else if (radioBtnApprentice.isChecked()) {
            position = "Apprentice";
        } else if (position.equals("unknown")) {
            radioBtnApprentice.setError("Please select a position");
            radioBtnApprentice.requestFocus();
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_LONG).show();
            chooseProfilePic();
        }
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = dataBase.collection("users").document(userId);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fullName);
        userMap.put("companyName", companyName);
        userMap.put("position", position);
        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Profile updated. " + userId);
                StorageRef.child(userId).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Intent intent = new Intent(SetUpAccount.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SetUpAccount.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetUpAccount.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        StorageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if(task.isSuccessful()){
//                   StorageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            HashMap hashMap = new HashMap();
//                            hashMap.put("fullName", fullName );
//                            hashMap.put("companyName", companyName);
//                            hashMap.put("position", position);
//                            hashMap.put("profileImage", uri.toString());
//
//
//                            mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
//                                @Override
//                                public void onSuccess(Object o) {
//                                    Intent intent = new Intent(SetUpAccount.this, MainActivity.class);
//                                    startActivity(intent);
//                                    Toast.makeText(SetUpAccount.this, "Profile updated.", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(SetUpAccount.this, e.toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });
    }

    public void chooseProfilePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        profilePicActivity.launch(intent);
    }


}