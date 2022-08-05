package com.example.nestedscrollviewtest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public  class RegionData {

    public static ArrayList<Region> getRegions(){
        ArrayList<Region> regions = new ArrayList<Region>();

        regions.add(new Region("Germany", Region.State.unDownloaded));
        regions.add(new Region("Ukraine", Region.State.isDownloading));
        regions.add(new Region("Poland", Region.State.downloaded));
        regions.add(new Region("Spain", Region.State.expandable));
        regions.add(new Region("Belarus", Region.State.unDownloaded));
        regions.add(new Region("France", Region.State.unDownloaded));
        regions.add(new Region("Finland", Region.State.unDownloaded));
        regions.add(new Region("asd", Region.State.unDownloaded));
        regions.add(new Region("asd1", Region.State.unDownloaded));
        regions.add(new Region("asd2", Region.State.unDownloaded));
        regions.add(new Region("asd3", Region.State.unDownloaded));
        regions.add(new Region("asd4", Region.State.unDownloaded));
        regions.add(new Region("asd5", Region.State.unDownloaded));
        regions.add(new Region("asd6", Region.State.unDownloaded));
        regions.add(new Region("asd7", Region.State.unDownloaded));
        //public String region[] = {"Germany", "Ukraine", "Poland", "Spain", "Belarus", "France", "Finland", "asd", "asd1", "asd2", "asd3", "asd4", "asd5"};


        return regions;
    }

    public static void sortRegion(ArrayList<Region> regions){
        //regions.sort((o1, o2) -> o1.name.compareTo(o2.name));
        //Collections.sort(regions, (o1, o2) -> o1.name.compareTo(o2.name));
        Collections.sort(regions, new Comparator<Region>() {
            @Override
            public int compare(Region region, Region t1) {
                return region.name.toLowerCase().compareTo(t1.name.toLowerCase());
            }
        });
    }
}
