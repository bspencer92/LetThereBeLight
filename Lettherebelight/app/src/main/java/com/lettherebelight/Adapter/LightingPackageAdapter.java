package com.lettherebelight.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lettherebelight.AddNewPart;
import com.lettherebelight.Model.LightingPackageModel;
import com.lettherebelight.ManageLightingPackages;
import com.lettherebelight.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LightingPackageAdapter extends RecyclerView.Adapter<LightingPackageAdapter.ViewHolder> {

    private List<LightingPackageModel> lightingPackageList = new ArrayList<>();
    private ManageLightingPackages activity;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public LightingPackageAdapter(FirebaseFirestore firestore, ManageLightingPackages activity){
        this.activity = activity;
        this.firestore = firestore;

    }

    public LightingPackageAdapter() {

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lightpackage_layout, parent, false);
        return new ViewHolder(itemView);

    }

    public  void onBindViewHolder(ViewHolder holder, int position){
        firestore = FirebaseFirestore.getInstance();
        LightingPackageModel item = lightingPackageList.get(position);
        holder.part.setText(item.getPartName());
        holder.part.setChecked(toBoolean(item.getStatus()));
        holder.part.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                }
            }
        });
    }

    public int getItemCount(){
        return lightingPackageList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setPart(ArrayList<LightingPackageModel> partsList){
        this.lightingPackageList = partsList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox part;
        ViewHolder(View view){
            super(view);
            part = view.findViewById(R.id.lightPackageCheckBox);
        }
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

    public void editPart(int position){
        LightingPackageModel item = lightingPackageList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("part", item.getPartName());
        AddNewPart fragment = new AddNewPart();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewPart.TAG);
    }
}
