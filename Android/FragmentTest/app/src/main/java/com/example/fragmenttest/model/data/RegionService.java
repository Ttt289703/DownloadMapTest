package com.example.fragmenttest.model.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.fragmenttest.model.parser.XMLRegionsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegionService {

    public static ArrayList<Region> regions;

    public static ArrayList<Region> getRegions(Context context) {

        ArrayList<Region> regions = getFlatContinentAndCountriesList(XMLRegionsParser.parseRegions(context));
        updateDownloadedRegions(regions);
        return regions;
    }

    public static void sortRegion(ArrayList<Region> regions) {
        Collections.sort(regions, new Comparator<Region>() {
            @Override
            public int compare(Region region, Region t1) {
                return region.name.toLowerCase().compareTo(t1.name.toLowerCase());
            }
        });
    }

    public static ArrayList<Region> getFlatContinentAndCountriesList(ArrayList<Region> regions) {
        ArrayList<Region> flatRegionList = new ArrayList<>();
        for (int index = 0; index < regions.size(); index++) {
            Region continent = regions.get(index);
            if (continent.type == Region.Type.continent) {
                flatRegionList.add(continent);
                flatRegionList.addAll(continent.childRegions);
            }
        }
        return flatRegionList;
    }

    public static void updateDownloadedRegions(ArrayList<Region> regions) {
        for (Region region : regions) {
            if (isDownloaded(region)) {
                region.state = Region.State.downloaded;
            } else {
                region.state = Region.State.unDownloaded;
            }
            if (region.childRegions != null) {
                updateDownloadedRegions(region.childRegions);
            }
        }
    }

    private static boolean isDownloaded(Region region) {
        if (region.type != Region.Type.continent) {
            File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String subpath = getDownloadPath(region);
            File myFile = new File(getDownloadPath(region));
            if (myFile.exists()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

/*    public static void deleteRegion(Region region){
        if(isDownloaded(region)){
            File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String subpath = getRegionPath(region) + "_" + getContinent(region).name + "_2.obf.zip";
            File myFile = new File(extStore.getAbsolutePath() + "/", subpath);
            if(myFile.exists()){
                if(myFile.delete()){
                    region.state = Region.State.unDownloaded;
                }
            }
        }
    }*/

    public static String getDownloadPath(Region region) {
        File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String subpath = getRegionPath(region) + "_" + getContinent(region).name + "_2.obf.zip";
        return extStore.getAbsolutePath() + "/" + subpath;
    }

    public static String getRegionPath(Region region) {
        String result = region.name;
        Region currentRegion = region;
        while (currentRegion.parent.type != Region.Type.continent) {
            currentRegion = currentRegion.parent;
            result = currentRegion.name + "_" + result;
        }
        return capitalize(result);
    }

    public static String getFullRegionPath(Region region) {
        String result = region.name;
        result = getRegionPath(region) + "_" + getContinent(region).name;
        return capitalize(result);
    }


    public static Region getContinent(Region region) {
        Region currentRegion = region;
        while (currentRegion.type != Region.Type.continent) {
            currentRegion = currentRegion.parent;
        }
        return currentRegion;
    }

    private static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
