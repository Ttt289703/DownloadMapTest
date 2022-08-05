package com.example.customrecycleritemtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RegionViewHolder extends RecyclerView.ViewHolder {
    private RegionItemView regionItemView;

    public RegionViewHolder(@NonNull View itemView) {
        super(itemView);
        regionItemView = (RegionItemView) itemView;

    }

    public RegionItemView getRegionItemView(){
        return regionItemView;
    }
}
