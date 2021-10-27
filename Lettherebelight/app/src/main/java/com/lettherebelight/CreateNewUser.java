package com.lettherebelight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateNewUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private EditText  editTxtNewEmail, editTxtNewPassword, editTxtConfirmNewPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private TextView txtViewReturnToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        firebaseAuth = FirebaseAuth.getInstance();


        db = FirebaseFirestore.getInstance();
       // editTxtFullName =(EditText)findViewById(R.id.editTxtFullName);
        editTxtNewEmail =  findViewById(R.id.editTextNewEmailAddress);
        editTxtNewPassword = findViewById(R.id.editTextNewPassword);
        editTxtConfirmNewPassword= findViewById(R.id.editTextConfirmNewPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtViewReturnToLogin = findViewById(R.id.txtViewReturnToLogin);
        btnRegister.setOnClickListener(this);
        txtViewReturnToLogin.setOnClickListener(this);
        progressBar =  findViewById(R.id.progressBarRegister);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:{
                registerUser();
                break;
            }
            case R.id.txtViewReturnToLogin:{
                returnToLogin();
                break;
            }
        }
    }

    private void returnToLogin() {
        Intent intent = new Intent(CreateNewUser.this, LoginActivity.class);
        startActivity(intent);
    }

    private void registerUser() {
        String email = editTxtNewEmail.getText().toString().trim();
       // String fullName = editTxtFullName.getText().toString().trim();
        String password = editTxtNewPassword.getText().toString().trim();
        String confirmPassword = editTxtConfirmNewPassword.getText().toString().trim();

//        if(fullName.isEmpty()){
//            editTxtFullName.setError("Full name is required");
//            editTxtFullName.requestFocus();
//            return;
//        }
        if(email.isEmpty()){
            editTxtNewEmail.setError("Email is required");
            editTxtNewEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTxtNewEmail.setError("Please provide a valid email");
            editTxtNewEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTxtNewPassword.setError("Password is required");
            editTxtNewPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTxtNewPassword.setError("Password must contain 6 characters");
            editTxtNewPassword.requestFocus();
        }
        if(!confirmPassword.equals(password)){
            editTxtConfirmNewPassword.setError("Passwords must match");
            editTxtConfirmNewPassword.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    //User user = new User(fullName, email, password);
                    Toast.makeText(CreateNewUser.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNewUser.this, SetUpAccount.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateNewUser.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        firebaseAuth.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            User user = new User(fullName, email, password);
//
//                            FirebaseDatabase.getInstance().getReference("User"  ).child(FirebaseAuth.getInstance()
//                                    .getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(CreateNewUser.this,"Registration Succsessful",Toast.LENGTH_SHORT ).show();
//                                        progressBar.setVisibility(View.GONE);
//                                    }else{
//                                        Toast.makeText(CreateNewUser.this, "Registration failed. Try again.", Toast.LENGTH_LONG).show();
//                                        progressBar.setVisibility(View.GONE);
//                                    }
//                                }
//                            });
//                        }else{
//                            Toast.makeText(CreateNewUser.this, "Registration failed. Try again.", Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });
    }
}