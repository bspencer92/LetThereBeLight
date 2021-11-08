package com.lettherebelight;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lettherebelight.Entity.User;

import java.util.HashMap;
import java.util.Map;

public class UpdateAccount extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private StorageReference StorageRef;
    private FirebaseFirestore dataBase;
    private String position = "unknown";
    private String userId;
    private User user = new User();
    private EditText editTxtFullName, editTxtCompanyName;
    private RadioButton radioBtnApprentice, radioBtnJourneyman, radioBtnMaster;
    private Button btnUpdateAccount;
    private Spinner toolBarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        editTxtFullName = findViewById(R.id.editTxtFullName);
        editTxtCompanyName = findViewById(R.id.editTxtCompanyName);
        radioBtnApprentice = findViewById(R.id.radioBtnApprentice);
        radioBtnJourneyman = findViewById(R.id.radioBtnJourneyman);
        radioBtnMaster = findViewById(R.id.radioBtnMaster);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        dataBase = FirebaseFirestore.getInstance();
        toolBarSpinner = findViewById(R.id.toolBarSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolBarMenu, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolBarSpinner.setAdapter(adapter);
        toolBarSpinner.setSelection(0, false);
        toolBarSpinner.setOnItemSelectedListener(this);
        getUserInfo();
    }

    public void getUserInfo() {
        DocumentReference documentReference = dataBase.collection("users").document(mUser.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                Log.d("user data:", user.getFullName());
                editTxtFullName.setHint(user.getFullName());
                editTxtCompanyName.setHint(user.getCompanyName());
                if (user.getPosition().equals("Master")) {
                    radioBtnMaster.setChecked(true);
                } else if (user.getPosition().equals("Journeyman")) {
                    radioBtnJourneyman.setChecked(true);
                } else if (user.getPosition().equals("Apprentice")) {
                    radioBtnApprentice.setChecked(true);
                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
        }
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = dataBase.collection("users").document(userId);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fullName);
        userMap.put("companyName", companyName);
        userMap.put("position", position);

        documentReference.update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Profile updated. " + userId);
                Intent intent = new Intent(UpdateAccount.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateAccount.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = null;
        if (adapterView.getItemAtPosition(i).toString().equals("View Blue Prints")) {
            intent = new Intent(UpdateAccount.this, PdfViewActivity.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("View Lighting Packages")) {
            intent = new Intent(UpdateAccount.this, ManageLightingPackages.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Update Profile")) {
            intent = new Intent(UpdateAccount.this, UpdateAccount.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Logout")) {
            intent = new Intent(UpdateAccount.this, LoginActivity.class);
            //firebaseAuth.signOut();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}