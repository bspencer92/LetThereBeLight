package com.lettherebelight;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lettherebelight.Adapter.LightingPackageAdapter;
import com.lettherebelight.Model.LightingPackageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManageLightingPackages extends AppCompatActivity implements DialogCloseListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private RecyclerView lightingPackageView;
    private LightingPackageAdapter partAdapter;
    private ArrayList<LightingPackageModel> partsList;
    private FirebaseFirestore firestore;
    private FloatingActionButton fab;
    private Spinner toolBarSpinner;


    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lighting_packages);
        lightingPackageView = findViewById(R.id.recViewParts);
        partsList = new ArrayList<>();
        lightingPackageView.setLayoutManager(new LinearLayoutManager(this));
        partAdapter = new LightingPackageAdapter(firestore, this);

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fab = findViewById(R.id.floatBtnLighting);
        toolBarSpinner = findViewById(R.id.toolBarSpinner);
        ArrayAdapter<CharSequence> toolBarAdapter = ArrayAdapter.createFromResource(this, R.array.toolBarMenu, android.R.layout.simple_spinner_dropdown_item);
        toolBarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolBarSpinner.setAdapter(toolBarAdapter);
        toolBarSpinner.setSelection(0, false);
        toolBarSpinner.setOnItemSelectedListener(this);

        getList();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(partAdapter));
        itemTouchHelper.attachToRecyclerView(lightingPackageView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewPart.newInstance().show(getSupportFragmentManager(), AddNewPart.TAG);
            }
        });

    }

    private void getList() {
        partsList.clear();
        lightingPackageView.setAdapter(partAdapter);
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("users").document(fUser.getUid() + "/lighting_packages/" + fUser.getUid() + "'s parts");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                Map<String, Object> partsMap = document.getData();
                if (partsMap != null) {


                    Object a = partsMap.get("partMap");
                    Log.d("Object", a.toString());
                    ArrayList<HashMap<String, Object>> test = (ArrayList<HashMap<String, Object>>) a;
                    Log.d("test.size", test.size() + " size");
                    HashMap<String, Object> l = new HashMap<>();
                    for (int i = 0; i < test.size(); i++) {
                        LightingPackageModel part = new LightingPackageModel();
                        l = test.get(i);
                        part.setPartName(l.get("partName").toString());

                        part.setStatus(Integer.parseInt(l.get("status").toString()));
                        part.setId(Integer.parseInt(l.get("id").toString()));
                        partsList.add(part);
                        Log.d("list at i first loop : ", i + partsList.get(i).getPartName());
                        Log.d("L test: " + i, l.toString());
                        Log.d("L test partName: " + i, l.get("partName").toString());

                    }
                    Log.d("parts List size", partsList.size() + "is the size of parts list");
                    Log.d("parts List outside ", partsList.toString());
                    for (int i = 0; i < partsList.size(); i++) {
                        Log.d("parts list at i: ", i + partsList.get(i).getPartName());

                    }
                    Log.d("keySet", partsMap.keySet().toString());
                    Log.d("keySet", partsMap.toString());
                    //    Collections.reverse(partsList);
                    partAdapter.setPart(partsList);
                    //Log.d("mytag",partsList.get(0).getPartName());
                    partAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        //LightingPackageAdapter newAdapter = new LightingPackageAdapter();
        partsList.clear();

        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("users").document(fUser.getUid() + "/lighting_packages/" + fUser.getUid() + "'s parts");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                Map<String, Object> partsMap = document.getData();
                if (partsMap != null) {


                    Object a = partsMap.get("partMap");
                    Log.d("Object", a.toString());
                    ArrayList<HashMap<String, Object>> test = (ArrayList<HashMap<String, Object>>) a;
                    Log.d("test.size", test.size() + " size");
                    HashMap<String, Object> l = new HashMap<>();
                    for (int i = 0; i < test.size(); i++) {
                        LightingPackageModel part = new LightingPackageModel();
                        l = test.get(i);
                        part.setPartName(l.get("partName").toString());

                        part.setStatus(Integer.parseInt(l.get("status").toString()));
                        part.setId(Integer.parseInt(l.get("id").toString()));
                        partsList.add(part);
                        Log.d("list at i first loop : ", i + partsList.get(i).getPartName());
                        Log.d("L test: " + i, l.toString());
                        Log.d("L test partName: " + i, l.get("partName").toString());

                    }
                    Log.d("parts List size", partsList.size() + "is the size of parts list");
                    Log.d("parts List outside ", partsList.toString());
                    for (int i = 0; i < partsList.size(); i++) {
                        Log.d("parts list at i: ", i + partsList.get(i).getPartName());

                    }
                    Log.d("keySet", partsMap.keySet().toString());
                    Log.d("keySet", partsMap.toString());
                    //    Collections.reverse(partsList);

                    lightingPackageView.setAdapter(partAdapter);
                    //Log.d("mytag",partsList.get(0).getPartName());

                    partAdapter.setPart(partsList);

                }

            }
        });
        //lightingPackageView.swapAdapter(newAdapter, true);
        lightingPackageView.setAdapter(partAdapter);
        partAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = null;
        if (adapterView.getItemAtPosition(i).toString().equals("View Blue Prints")) {
            intent = new Intent(ManageLightingPackages.this, PdfViewActivity.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("View Lighting Packages")) {
            intent = new Intent(ManageLightingPackages.this, ManageLightingPackages.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Update Profile")) {
            intent = new Intent(ManageLightingPackages.this, UpdateAccount.class);
        } else if (adapterView.getItemAtPosition(i).toString().equals("Logout")) {
            intent = new Intent(ManageLightingPackages.this, LoginActivity.class);
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