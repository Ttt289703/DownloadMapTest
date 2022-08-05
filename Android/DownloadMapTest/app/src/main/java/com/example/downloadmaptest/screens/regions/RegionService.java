package com.example.downloadmaptest.screens.regions;

import android.content.Context;
import android.os.Environment;

import com.example.downloadmaptest.model.data.Region;
import com.example.downloadmaptest.model.parser.XMLRegionsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

interface RegionServiceListener {
    void onRegionsChange(ArrayList<Region> regions);
}

public class RegionService {
    ArrayList<RegionServiceListener> listeners = new ArrayList<>();
    ArrayList<Region> regions;

    private static RegionService instance = null;

    private RegionService(Context context) {
        regions = XMLRegionsParser.parseRegions(context);
        updateDownloadedRegions(regions);
    }

    public static RegionService getInstance(Context context) {
        if (instance == null) {
            instance = new RegionService(context);
        }
        return instance;
    }

    public void registerListener(RegionServiceListener listener) {
        if (listeners.isEmpty() || !listeners.contains(listener)) {
            listeners.add(listener);
            listener.onRegionsChange(regions);
        }
    }

    public void unregisterListener(RegionServiceListener listener) {
        listeners.remove(listener);
    }

    public static ArrayList<String> getPathChain(Region region){
        ArrayList<String> pathChain = new ArrayList<>();
        Region curentRegion = region;
        while(curentRegion.parent != null ||(curentRegion.parent !=null &&curentRegion.parent.type != Region.Type.continent)){
            pathChain.add(curentRegion.name);
            curentRegion = curentRegion.parent;
        }
        return pathChain;
    }

    public static void sortRegion(ArrayList<Region> regions){
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

    public static String getRegionDownloadPath(Region region){
        String result = region.name;
        Region currentRegion = region;
        while(currentRegion.parent.type != Region.Type.continent){
            currentRegion = currentRegion.parent;
            result = currentRegion.name + "_" + result;
        }
        return capitalize(result);
    }

    public static Region getContinent(Region region){
        Region currentRegion = region;
        while (currentRegion.type != Region.Type.continent){
            currentRegion = currentRegion.parent;
        }
        return currentRegion;
    }

    public static String getFullRegionDownloadPath(Region region){
        String result = region.name;
        result = getRegionDownloadPath(region) + "_" + getContinent(region).name;
        return capitalize(result);
    }

    private static String capitalize(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void updateDownloadedRegions(ArrayList<Region> regions){
        // TODO: 8/5/2022
        for(Region region: regions){
            if (isDownloaded(region)){
                //region.state = Region.State.downloaded;
            }else{
                region.state = Region.State.unDownloaded;
            }
            if(region.childRegions != null){
                updateDownloadedRegions(region.childRegions);
            }
        }
    }
    private static boolean isDownloaded(Region region){
        // TODO: 8/2/2022 algorithm
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "/a/b.html");
        return false;
    }
}
