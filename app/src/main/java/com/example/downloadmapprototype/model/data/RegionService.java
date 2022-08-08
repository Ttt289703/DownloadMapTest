package com.example.downloadmapprototype.model.data;

import android.content.Context;
import android.os.Environment;

import com.example.downloadmapprototype.model.parser.XMLRegionsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegionService {
    public final static String DOWNLOAD_DIRECTORY = "/Test/maps/";
    public final static String DOWNLOAD_SUFFIX = "_2.obf.zip";

    ArrayList<RegionServiceListener> listeners = new ArrayList<>();
    public ArrayList<Region> regions;

    private static RegionService instance = null;

    private RegionService(Context context) {
        regions = getFlatContinentsAndRegionsList(XMLRegionsParser.parseRegions(context));
        updateDownloadedRegions(regions);
    }

    public static RegionService getInstance(Context context) {
        if (instance == null) {
            instance = new RegionService(context);
        }
        return instance;
    }

    public static void sortRegion(ArrayList<Region> regions) {
        Collections.sort(regions, new Comparator<Region>() {
            @Override
            public int compare(Region region1, Region region2) {
                return region1.getName().toLowerCase().compareTo(region2.getName().toLowerCase());
            }
        });
    }

    private static ArrayList<Region> getFlatContinentsAndRegionsList(ArrayList<Region> regions) {
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
            File myFile = new File(getDownloadPath(region));
            if (myFile.exists()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

/*    public static void deleteRegion(Region region, Context context){
        if(isDownloaded(region)){
            File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String subPath = getRegionPath(region) + "_" + getContinent(region).getName() + DOWNLOAD_SUFFIX;

            File myFile = new File(extStore.getAbsolutePath() + DOWNLOAD_DIRECTORY + subPath);
            if(myFile.exists()){
                if(myFile.delete()){
                    region.state = Region.State.unDownloaded;
                }
            }
        }
    }*/

    public static String getDownloadPath(Region region) {
        File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String subPath = getRegionPath(region) + "_" + getContinent(region).getName() + DOWNLOAD_SUFFIX;
        return extStore.getAbsolutePath() + DOWNLOAD_DIRECTORY + subPath;
    }

    public static String getRegionPath(Region region) {
        String result = region.getName();
        Region currentRegion = region;
        while (currentRegion.parent.type != Region.Type.continent) {
            currentRegion = currentRegion.parent;
            result = currentRegion.getName() + "_" + result;
        }
        return capitalize(result);
    }

    public static String getFullRegionPath(Region region) {
        String result = region.getName();
        result = getRegionPath(region) + "_" + getContinent(region).getName();
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


    public static ArrayList<String> getRegionOrder(Region region) {
        ArrayList<String> pathChain = new ArrayList<>();
        Region curentRegion = region;
        while (curentRegion.parent != null || (curentRegion.parent != null && curentRegion.parent.type != Region.Type.continent)) {
            pathChain.add(curentRegion.getName());
            curentRegion = curentRegion.parent;
        }
        return pathChain;
    }

    public void registerListener(RegionServiceListener listener) {
        if (listeners.isEmpty() || !listeners.contains(listener)) {
            listeners.add(listener);
            listener.onRegionsChange(regions, null);
        }
    }

    public void unregisterListener(RegionServiceListener listener) {
        listeners.remove(listener);
    }

    public void setRegionProgress(Region region, int progress) {
        region.setDownloadProgress(progress);
        notifyOnRegionsChange(region);
    }

    public void setRegionState(Region region, Region.State state) {
        region.setState(state);
        notifyOnRegionsChange(region);
    }

    private void notifyOnRegionsChange(Region updatedRegion) {
        for (RegionServiceListener listener : listeners) {
            listener.onRegionsChange(regions, updatedRegion);
        }
    }
}
