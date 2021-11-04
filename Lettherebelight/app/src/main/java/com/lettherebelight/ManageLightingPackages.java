package com.lettherebelight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lettherebelight.Adapter.LightingPackageAdapter;
import com.lettherebelight.Model.LightingPackageModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ManageLightingPackages extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView lightingPackageView;
    private LightingPackageAdapter partAdapter;
   private ArrayList<LightingPackageModel> partsList;
   private FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        partsList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lighting_packages);
        lightingPackageView = findViewById(R.id.recViewParts);
        lightingPackageView.setLayoutManager(new LinearLayoutManager(this));
        partAdapter = new LightingPackageAdapter(firestore,this);
        lightingPackageView.setAdapter(partAdapter);
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fab = findViewById(R.id.floatBtnLighting);



//        LightingPackageModel part = new LightingPackageModel();
//        part.setPartName("this is a test item");
//        part.setStatus(0);
//        part.setId(1);
//        partsList.add(part);
//        partsList.add(part);
//        partsList.add(part);
//        partsList.add(part);
//        partsList.add(part);

        DocumentReference documentReference = firestore.collection("lighting_packages")
                .document(fUser.getUid()+"/lighting_packages/"+ fUser.getUid()+"'s parts");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                    for (QueryDocumentSnapshot documentSnapshots : documentSnapshot.get()) {
                        LightingPackageModel part =documentSnapshots.toObject(LightingPackageModel.class);
                        partsList.add(part);
                        Log.d("myTag", documentSnapshots.getId()+"=>"+documentSnapshots.getData());
                    }


                partAdapter.setPart(partsList);
            }
        });
//        firestore.collection("lighting_packages").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                       // LightingPackageModel part = new LightingPackageModel();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
//                               LightingPackageModel part =documentSnapshots.toObject(LightingPackageModel.class);
//                                partsList.add(part);
//                                Log.d("myTag", documentSnapshots.getId()+"=>"+documentSnapshots.getData());
//                            }
//                        }else {
//                            Log.d("myErrorTag", task.getException().toString());
//                        }
//
//                        partAdapter.setPart(partsList);
//                    }
//                });
//        String userId = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firestore.collection("users").document(fUser.getUid()+"/lighting_packages/"+ fUser.getUid()+"'s parts");
//
//                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//
//                //ArrayList<Map<String, Object>> mapArrayList = (ArrayList<Map<String, Object>>) document.getData().get(fUser.getUid()+"/lighting_packages/partMap");
//                 Map<String, Object> partsMap = document.getData();
//                 if(partsMap!=null){
//                    LightingPackageModel part = new LightingPackageModel();
//                     for (Map.Entry<String, Object> entry : partsMap.entrySet()) {
//                         if (entry.getKey().equals("partName")) {
//                             part.setPartName(entry.getValue().toString());
//                         }
//                         if(entry.getKey().equals("id")){
//                             part.setId((Integer) entry.getValue());
//                         }
//                         if(entry.getKey().equals("status")){
//                             part.setStatus((Integer) entry.getValue());
//                         }
//                         partsList.add(part);
//                     }

//                    Log.d("keySet", partsMap.keySet().toString()) ;
//                     Log.d("keySet", partsMap.toString());
//                     part.setPartName(partsMap.get("partMap").toString());
//                      //part.setId(partsMap.get("0"));
//                     partsList.add(part);
//                     Log.d("mytag",partsList.get(0).getPartName());
//                     partAdapter.setPart(partsList);
//                 }
//
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewPart.newInstance().show(getSupportFragmentManager(), AddNewPart.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        firestore.collection("lighting_packages").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshots : Objects.requireNonNull(task.getResult())) {
                                partsList.add(documentSnapshots.toObject(LightingPackageModel.class));
                                Log.d("myTag", documentSnapshots.getId()+"=>"+documentSnapshots.getData());
                            }
                        }else {
                            Log.d("myErrorTag", task.getException().toString());
                        }

                        partAdapter.setPart(partsList);
                    }

                });
//       String userId = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firestore.collection("users").document(fUser.getUid()+"/lighting_packages/"+ fUser.getUid()+"'s parts");
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//                Map<String, Object> partsMap = document.getData();
//                if(partsMap!=null){
//                    LightingPackageModel part = new LightingPackageModel();
//                    part.setPartName(partsMap.get("partMap").toString());
//                    // part.setId(partsMap.get("0"));
//                    partsList.add(part);
//                    Log.d("mytag",partsList.get(0).getPartName());
//                    partAdapter.setPart(partsList);
//                }
//
//            }
//        });
        //       documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//           @Override
//           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//               Map<String, Object> partsMap =  value.getData();
//
//               Collection<Object> objects = partsMap.values();
//
//           }
//       });
    }
}