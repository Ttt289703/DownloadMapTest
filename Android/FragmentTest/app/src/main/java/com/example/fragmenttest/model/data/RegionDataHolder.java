package com.example.fragmenttest.model.data;

import android.content.Context;

import java.util.ArrayList;

public class RegionDataHolder {

    public static ArrayList<Region> regions;

    public RegionDataHolder(Context context) {
        if (regions == null) {
            regions = RegionService.getRegions(context);
        }
    }
}
