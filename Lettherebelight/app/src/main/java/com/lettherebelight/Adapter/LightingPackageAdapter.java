package com.lettherebelight.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lettherebelight.AddNewPart;
import com.lettherebelight.ManageLightingPackages;
import com.lettherebelight.Model.LightingPackageModel;
import com.lettherebelight.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightingPackageAdapter extends RecyclerView.Adapter<LightingPackageAdapter.ViewHolder> {

    private List<LightingPackageModel> lightingPackageList = new ArrayList<LightingPackageModel>();
    private ManageLightingPackages activity;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public LightingPackageAdapter(FirebaseFirestore firestore, ManageLightingPackages activity) {
        this.activity = activity;
        this.firestore = firestore;

    }

    public LightingPackageAdapter() {

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lightpackage_layout, parent, false);
        return new ViewHolder(itemView);

    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        firestore = FirebaseFirestore.getInstance();

        LightingPackageModel item = lightingPackageList.get(position);

        holder.part.setText(item.getPartName());
        holder.part.setChecked(toBoolean(item.getStatus()));
        holder.part.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    //item.setStatus(1);
                }
            }
        });
    }

    public Context getContext() {
        return activity;
    }

    public int getItemCount() {
        return lightingPackageList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public void setPart(ArrayList<LightingPackageModel> partsList) {
        this.lightingPackageList = partsList;
        for (int i = 0; i < partsList.size(); i++) {
            Log.d("inside set parts: ", i + partsList.get(i).getPartName());
        }
        //notifyDataSetChanged();
    }

    public List<LightingPackageModel> getLightingPackageList() {
        return lightingPackageList;
    }

    public void setLightingPackageList(List<LightingPackageModel> lightingPackageList) {
        this.lightingPackageList = lightingPackageList;
    }

    public ManageLightingPackages getActivity() {
        return activity;
    }

    public void setActivity(ManageLightingPackages activity) {
        this.activity = activity;
    }

    public void editPart(int position) {
        LightingPackageModel item = lightingPackageList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("part", item.getPartName());
        AddNewPart fragment = new AddNewPart();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewPart.TAG);
        //LightingPackageModel item = lightingPackageList.get(position);
        lightingPackageList.remove(position);
        notifyItemRemoved(position);
        Map<String, Object> partMap = new HashMap<>();
        partMap.put("partMap", lightingPackageList);
        firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid() + "/lighting_packages/" + firebaseAuth.getCurrentUser().getUid() + "'s parts");
        documentReference.set(partMap);
    }

    public void deleteItem(int position) {
        LightingPackageModel item = lightingPackageList.get(position);
        lightingPackageList.remove(position);
        notifyItemRemoved(position);
        Map<String, Object> partMap = new HashMap<>();
        partMap.put("partMap", lightingPackageList);
        firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid() + "/lighting_packages/" + firebaseAuth.getCurrentUser().getUid() + "'s parts");
        documentReference.set(partMap);

        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox part;

        ViewHolder(View view) {
            super(view);
            part = view.findViewById(R.id.lightPackageCheckBox);
        }
    }
}
