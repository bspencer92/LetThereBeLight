package com.lettherebelight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;


import com.google.firebase.firestore.FirebaseFirestore;
import com.lettherebelight.Adapter.LightingPackageAdapter;
import com.lettherebelight.Model.LightingPackageModel;

import java.util.ArrayList;

public class ManageLightingPackages extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView lightingPackageView;
    private LightingPackageAdapter partAdapter;
   private ArrayList<LightingPackageModel> partsList;
   private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        partsList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lighting_packages);
        lightingPackageView = findViewById(R.id.recViewParts);
        lightingPackageView.setLayoutManager(new LinearLayoutManager(this));
        partAdapter = new LightingPackageAdapter(this);
        lightingPackageView.setAdapter(partAdapter);
        firestore = FirebaseFirestore.getInstance();

        LightingPackageModel part = new LightingPackageModel();
        part.setPartName("this is a test item");
        part.setStatus(0);
        part.setId(1);
        partsList.add(part);
        partsList.add(part);
        partsList.add(part);
        partsList.add(part);
        partsList.add(part);

        partAdapter.setPart(partsList);



    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        //partsList = firestore.getNamedQuery("part_text")
    }
}