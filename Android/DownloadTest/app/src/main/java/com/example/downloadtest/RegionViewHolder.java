package com.example.downloadtest;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RegionViewHolder extends RecyclerView.ViewHolder {

    public TextView regionNameView;
    public ImageButton downloadButton;
    public ProgressBar progressBar;
    public ConstraintLayout parentConstraintLayout;

    public RegionViewHolder(@NonNull View itemView) {
        super(itemView);

        regionNameView = itemView.findViewById(R.id.tv_region_name);
        downloadButton = itemView.findViewById(R.id.button_download);
        progressBar = itemView.findViewById(R.id.progressBar);
        parentConstraintLayout = itemView.findViewById(R.id.region_item_parent_layout);
    }

    public void bind(String regionName){
        regionNameView.setText(regionName);
    }
}
