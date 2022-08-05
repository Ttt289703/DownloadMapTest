package com.example.downloadtest;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public  class RegionService {

    public static ArrayList<Region> getRegions(Context context){
        ArrayList<Region> regions = new ArrayList<Region>();

        regions.add(new Region("Germany", Region.State.unDownloaded));
        regions.add(new Region("Ukraine", Region.State.isDownloading));
        regions.add(new Region("Poland", Region.State.downloaded));
        regions.add(new Region("Spain", Region.State.downloaded));
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

        //ArrayList<Region> regions = getFlatContinentAndCountriesList(XMLRegionsParser.parseRegions(context));
        //updateRegionsState(regions);
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

    public static ArrayList<Region> getFlatContinentAndCountriesList(ArrayList<Region> regions){
        ArrayList<Region> flatRegionList = new ArrayList<>();

        for(int index = 0; index < regions.size(); index++){
            Region continent = regions.get(index);
            if(continent.type == Region.Type.continent){
                flatRegionList.add(continent);
                flatRegionList.addAll(continent.childRegions);
            }
        }
        return flatRegionList;
    }

    public static void updateRegionsState(ArrayList<Region> regions){
        for(Region region: regions){
            if (isDownloaded(region)){
                region.state = Region.State.downloaded;
            }else{
                region.state = Region.State.unDownloaded;
            }
            if(region.childRegions != null){
                updateRegionsState(region.childRegions);
            }
        }
    }

    private static boolean isDownloaded(Region region){
        // TODO: 8/2/2022 algorithm
        return false;
    }
}
