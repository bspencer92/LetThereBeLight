package com.lettherebelight.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.lettherebelight.Model.LightingPackageModel;
import com.lettherebelight.ManageLightingPackages;
import com.lettherebelight.R;

import java.util.ArrayList;
import java.util.List;

public class LightingPackageAdapter extends RecyclerView.Adapter<LightingPackageAdapter.ViewHolder> {

    private List<LightingPackageModel> lightingPackageList;
    private ManageLightingPackages activity;

    public LightingPackageAdapter(ManageLightingPackages activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lightpackage_layout, parent, false);
        return new ViewHolder(itemView);

    }

    public  void onBindViewHolder(ViewHolder holder, int position){
        LightingPackageModel item = lightingPackageList.get(position);
        holder.part.setText(item.getPartName());
        holder.part.setChecked(toBoolean(item.getStatus()));
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
}
