package com.example.downloadmapprototype;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.model.data.Region;


import java.util.ArrayList;


interface RegionActionListener {

    void onRegionDetails(Region region, int index);

    void onRegionDownload(Region region, int index);
}

public class RegionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private int itemsCount;
    private RegionActionListener regionActionListener;
    private ArrayList<Region> regions;
    private Context context;

    public RegionsAdapter(int numberOfItems, ArrayList<Region> regions, RegionActionListener regionActionListener) {
        this.regions = regions;
        itemsCount = numberOfItems;
        this.regionActionListener = regionActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        return regions.get(position).type.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        View view;

        if (viewType == Region.Type.continent.ordinal()) {
            int layoutId = R.layout.continent_list_item;
            view = layoutInflater.inflate(layoutId, parent, false);
            viewHolder = new ContinentViewHolder(view);
        } else {
            int layoutId = R.layout.region_list_item;
            view = layoutInflater.inflate(layoutId, parent, false);
            viewHolder = new RegionViewHolder(view);

            view.getRootView().setOnClickListener(this);
            view.findViewById(R.id.button_download).setOnClickListener(this);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Region region = regions.get(position);
        int viewType = getItemViewType(position);

        if (viewType == Region.Type.continent.ordinal()) {
            ContinentViewHolder viewHolder = (ContinentViewHolder) holder;
            viewHolder.bind(region.displayName);
        } else {
            RegionViewHolder viewHolder = (RegionViewHolder) holder;
            ConstraintSet constraintSet = new ConstraintSet();
            setRegionViewHolderStyle(region, viewHolder, constraintSet);
        }
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
                        index = _index;
                    }
                }

                regionActionListener.onRegionDownload(region, index);
                break;
            default:
                regionActionListener.onRegionDetails(region, index);
        }
    }

    private void setRegionViewHolderStyle(Region region, RegionViewHolder viewHolder, ConstraintSet constraintSet) {
        switch (region.state) {
            case unDownloaded:
                applyUnDownloadedStyle(viewHolder, constraintSet);
                break;
            case isDownloading:
                applyIsDownloadingStyle(region, viewHolder, constraintSet);
                break;
            case downloaded:
                applyDownloadedStyle(viewHolder, constraintSet);
                break;
        }
        if (region.hasMap) {
            viewHolder.downloadButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.downloadButton.setVisibility(View.INVISIBLE);
        }
        viewHolder.bind(region.displayName);
        viewHolder.itemView.setTag(region);
        viewHolder.downloadButton.setTag(region);
    }

    private void applyDownloadedStyle(RegionViewHolder viewHolder, ConstraintSet constraintSet) {
        viewHolder.progressBar.setEnabled(false);
        viewHolder.progressBar.setVisibility(View.INVISIBLE);
        viewHolder.downloadButton.setColorFilter(context.getColor(R.color.item_icon));
        viewHolder.mapIcon.setColorFilter(Color.GREEN);

        constraintSet.clone(viewHolder.parentConstraintLayout);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.TOP, viewHolder.parentConstraintLayout.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.BOTTOM, viewHolder.parentConstraintLayout.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(viewHolder.parentConstraintLayout);

        viewHolder.downloadButton.setImageResource(R.drawable.ic_action_remove_dark);
    }

    private void applyIsDownloadingStyle(Region region, RegionViewHolder viewHolder, ConstraintSet constraintSet) {
        viewHolder.progressBar.setEnabled(true);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
        viewHolder.progressBar.setProgress(region.downloadProgress);
        viewHolder.downloadButton.setColorFilter(context.getColor(R.color.item_icon));
        viewHolder.mapIcon.clearColorFilter();

        constraintSet.clone(viewHolder.parentConstraintLayout);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.TOP, viewHolder.parentConstraintLayout.getId(), ConstraintSet.TOP, 8);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.BOTTOM, viewHolder.progressBar.getId(), ConstraintSet.TOP, 0);
        constraintSet.applyTo(viewHolder.parentConstraintLayout);

        viewHolder.downloadButton.setImageResource(R.drawable.ic_action_remove_dark);
    }

    private void applyUnDownloadedStyle(RegionViewHolder viewHolder, ConstraintSet constraintSet) {
        viewHolder.progressBar.setEnabled(false);
        viewHolder.progressBar.setVisibility(View.INVISIBLE);
        viewHolder.downloadButton.setColorFilter(context.getColor(R.color.item_icon));
        viewHolder.mapIcon.clearColorFilter();

        constraintSet.clone(viewHolder.parentConstraintLayout);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.TOP, viewHolder.parentConstraintLayout.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(viewHolder.regionNameView.getId(), ConstraintSet.BOTTOM, viewHolder.divider.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(viewHolder.parentConstraintLayout);

        viewHolder.downloadButton.setImageResource(R.drawable.ic_action_import);
    }
}
