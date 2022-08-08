package com.example.downloadmapprototype.screens.regions;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadmapprototype.R;


public class ContinentViewHolder extends RecyclerView.ViewHolder {
    public TextView regionNameView;

    public ContinentViewHolder(@NonNull View itemView) {
        super(itemView);
        regionNameView = itemView.findViewById(R.id.tv_region_name);
    }

    public void bind(String regionName) {
        regionNameView.setText(regionName);
    }
}
