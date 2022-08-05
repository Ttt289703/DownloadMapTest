package com.example.nestedscrollviewtest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nestedscrollviewtest.data.Region;
import com.example.nestedscrollviewtest.data.RegionDataHolder;


interface RegionActionListener{

    void onRegionDetails(Region region, int index);

    void onRegionDownload(Region region);
}

public class RegionsAdapter extends RecyclerView.Adapter<RegionViewHolder> implements View.OnClickListener {

    private static int viewHolderCount;
    private int itemsCount;
    private RegionActionListener regionActionListener;
    private RegionDataHolder dataHolder;
    private Context context;

    public RegionsAdapter(int numberOfItems, RegionDataHolder dataHolder, RegionActionListener regionActionListener){
        this.dataHolder = dataHolder;
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

        View view = layoutInflater.inflate(layoutId, parent, false);

        RegionViewHolder regionViewHolder = new RegionViewHolder(view);

        view.getRootView().setOnClickListener(this);
        view.findViewById(R.id.button_download).setOnClickListener(this);

        viewHolderCount++;
        return regionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = dataHolder.regions.get(position);

        ConstraintSet constraintSet = new ConstraintSet();
        switch(region.state){
            case unDownloaded:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);

                constraintSet.clone(holder.parentConstraintLayout);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.TOP,holder.parentConstraintLayout.getId(),ConstraintSet.TOP,0);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.BOTTOM,holder.parentConstraintLayout.getId(),ConstraintSet.BOTTOM,0);
                constraintSet.applyTo(holder.parentConstraintLayout);
                break;
            case isDownloading:
                holder.progressBar.setEnabled(true);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);

                constraintSet.clone(holder.parentConstraintLayout);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.TOP,holder.parentConstraintLayout.getId(),ConstraintSet.TOP,8);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.BOTTOM,holder.progressBar.getId(),ConstraintSet.TOP,0);
                constraintSet.applyTo(holder.parentConstraintLayout);

                holder.downloadButton.setImageResource(R.drawable.ic_cancel);
                break;
            case downloaded:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GREEN);


                constraintSet.clone(holder.parentConstraintLayout);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.TOP,holder.parentConstraintLayout.getId(),ConstraintSet.TOP,0);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.BOTTOM,holder.parentConstraintLayout.getId(),ConstraintSet.BOTTOM,0);
                constraintSet.applyTo(holder.parentConstraintLayout);
                break;
            case expandable:
                holder.progressBar.setEnabled(false);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.downloadButton.setColorFilter(Color.GRAY);

                constraintSet.clone(holder.parentConstraintLayout);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.TOP,holder.parentConstraintLayout.getId(),ConstraintSet.TOP,0);
                constraintSet.connect(holder.regionNameView.getId(),ConstraintSet.BOTTOM,holder.parentConstraintLayout.getId(),ConstraintSet.BOTTOM,0);
                constraintSet.applyTo(holder.parentConstraintLayout);
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
        Region region = (Region)view.getTag();
        int index = 0;
        switch (view.getId()){
            case(R.id.button_download):
                for (Region _region : dataHolder.regions) {
                    if (_region == region) {
                        //_region.state = Region.State.isDownloading;
                    }
                }
                for (int _index = 0; _index < dataHolder.regions.size(); _index++) {
                    if (dataHolder.regions.get(_index) == region) {
                        dataHolder.regions.get(_index).state = Region.State.isDownloading;
                        index = _index;
                    }
                }
                regionActionListener.onRegionDetails(region, index);
                //view.holder.downloadButton.setImageResource(R.drawable.ic_cancel);
                //ImageButton button = view.findViewById(R.id.button_download);
                //button.setImageResource(R.drawable.ic_cancel);
                //region.state = Region.State.isDownloading;
                break;
            default:
                //regionActionListener.onRegionDetails(region);
        }
    }
}
