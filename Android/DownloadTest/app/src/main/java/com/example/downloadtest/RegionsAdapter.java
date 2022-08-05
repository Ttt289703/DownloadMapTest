package com.example.downloadtest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;


import com.example.downloadtest.databinding.RegionListItemBinding;

import java.util.ArrayList;


interface RegionActionListener {

    void onRegionDetails(Region region, int index);

    void onRegionDownload(Region region, int index);
}

public class RegionsAdapter extends RecyclerView.Adapter<RegionViewHolder> implements View.OnClickListener {

    private static int viewHolderCount;
    private int itemsCount;
    private RegionActionListener regionActionListener;
    private ArrayList<Region> regions;
    private Context context;

    public RegionsAdapter(int numberOfItems, ArrayList<Region> regions, RegionActionListener regionActionListener) {
        this.regions = regions;
        itemsCount = numberOfItems;
        this.regionActionListener = regionActionListener;
        viewHolderCount = 0;
    }


    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutId = R.layout.region_list_item;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RegionListItemBinding binding = RegionListItemBinding.inflate(layoutInflater, parent, false);

        RegionViewHolder regionViewHolder = new RegionViewHolder(binding.getRoot());

        binding.getRoot().getRootView().setOnClickListener(this);
        binding.getRoot().findViewById(R.id.button_download).setOnClickListener(this);

        viewHolderCount++;
        return regionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = regions.get(position);

        switch (region.state) {
            case unDownloaded:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);

                break;
            case isDownloading:
                holder.progressBar.setEnabled(true);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);

                holder.downloadButton.setImageResource(R.drawable.ic_cancel);
                break;
            case downloaded:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GREEN);
                break;
            default:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);
                break;
        }

        holder.bind(region.name);
        holder.itemView.setTag(region);
        holder.downloadButton.setTag(region);
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    @Override
    public void onClick(View view) {
        Region region = (Region) view.getTag();
        int index = 0;
        switch (view.getId()) {
            case (R.id.button_download):
                for (int _index = 0; _index < regions.size(); _index++) {
                    if (regions.get(_index) == region) {
                        regions.get(_index).state = Region.State.isDownloading;
                        index = _index;
                    }
                }

                regionActionListener.onRegionDownload(region, index);
                //view.holder.downloadButton.setImageResource(R.drawable.ic_cancel);
                //ImageButton button = view.findViewById(R.id.button_download);
                //button.setImageResource(R.drawable.ic_cancel);
                break;
            default:
                regionActionListener.onRegionDetails(region, index);
        }
    }
}
