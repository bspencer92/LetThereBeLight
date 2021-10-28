package com.lettherebelight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String userId;
    TextView welcome;
    ImageView blueprint, lightbulb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
       firestore = FirebaseFirestore.getInstance();
       welcome = findViewById(R.id.txtViewWelcome);
       blueprint = findViewById(R.id.imageViewBluePrint);
       lightbulb = findViewById(R.id.imageViewLightBulb);
       userId = firebaseAuth.getCurrentUser().getUid();
        setWelcome();
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
}