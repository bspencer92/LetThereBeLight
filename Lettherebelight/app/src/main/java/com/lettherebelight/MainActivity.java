package com.lettherebelight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , View.OnClickListener    {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String userId;
    TextView welcome;
    ImageView blueprint, lightbulb;
    Spinner toolBarSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
       firestore = FirebaseFirestore.getInstance();
       welcome = findViewById(R.id.txtViewWelcome);
       blueprint = findViewById(R.id.imageViewBluePrint);
        blueprint.setOnClickListener(this);
       lightbulb = findViewById(R.id.imageViewLightBulb);
        lightbulb.setOnClickListener(this);
       userId = firebaseAuth.getCurrentUser().getUid();
        setWelcome();
        toolBarSpinner = findViewById(R.id.toolBarSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolBarMenu, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolBarSpinner.setAdapter(adapter);
        toolBarSpinner.setSelection(0, false);
        toolBarSpinner.setOnItemSelectedListener(this);
    }

    public void setWelcome(){
        DocumentReference documentReference = firestore.collection("users").document(userId );
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                welcome.setText("Welcome " + value.getString("fullName"));

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.imageViewBluePrint:{
                intent = new Intent(MainActivity.this, ManageBluePrints.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.imageViewLightBulb:{
                intent = new Intent(MainActivity.this, ManageLightingPackages.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            }
        }

    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = null;
        if(adapterView.getItemAtPosition(i).toString().equals("View Blue Prints")){
            intent = new Intent(MainActivity.this, ManageBluePrints.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("View Lighting Packages")){
            intent = new Intent(MainActivity.this, ManageLightingPackages.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Update Profile")){
            intent = new Intent(MainActivity.this, SetUpAccount.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Logout")){
            intent = new Intent(MainActivity.this, LoginActivity.class);
            firebaseAuth.signOut();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}