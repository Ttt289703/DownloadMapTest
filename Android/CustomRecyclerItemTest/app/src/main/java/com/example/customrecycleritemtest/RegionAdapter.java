package com.example.customrecycleritemtest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customrecycleritemtest.model.Region;

import java.util.ArrayList;

public class RegionAdapter extends RecyclerView.Adapter<RegionViewHolder> {

    ArrayList<Region> list;

    public RegionAdapter(ArrayList<Region> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RegionItemView itemView = new RegionItemView(parent.getContext());

        return new RegionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        holder.getRegionItemView().setRegionName(list.get(position).name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
