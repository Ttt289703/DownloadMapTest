package com.example.nestedscrollviewtest.data;

import java.util.ArrayList;

public class RegionDataHolder {

    public ArrayList<Region> regions;

    public RegionDataHolder(){
        regions = RegionData.getRegions();
    }
}
