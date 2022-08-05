package com.example.downloadmaptest.screens.regions;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadmaptest.R;

public class RegionViewHolder extends RecyclerView.ViewHolder {

    public TextView regionNameView;
    public ImageButton downloadButton;
    public ProgressBar progressBar;
    public ConstraintLayout parentConstraintLayout;
    public ImageView mapIcon;
    public View divider;

    public RegionViewHolder(@NonNull View itemView) {
        super(itemView);
        regionNameView = itemView.findViewById(R.id.tv_region_name);
        downloadButton = itemView.findViewById(R.id.button_download);
        progressBar = itemView.findViewById(R.id.progressBar);
        parentConstraintLayout = itemView.findViewById(R.id.region_item_parent_layout);
        mapIcon = itemView.findViewById(R.id.image_view_map);
        divider = itemView.findViewById(R.id.divider);
    }

    public void bind(String regionName){
        regionNameView.setText(regionName);
    }
}
