package com.example.downloadmaptest.screens.regions;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.downloadmaptest.model.data.Region;

import java.util.ArrayList;

public class RegionListViewModel extends ViewModel {
    private RegionService regionService;
    private Context context;
    private Fragment fragment;
    private MutableLiveData<ArrayList<Region>> _regions = new MutableLiveData<>();
    LiveData<ArrayList<Region>> regions = _regions;

    public RegionListViewModel(RegionService regionService, Context context, Fragment fragment) {
        this.regionService = regionService;
        this.context = context;
        this.fragment = fragment;
        regionService.registerListener(regionServiceListener);
        Log.e("ViewModel", "Created");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        regionService.unregisterListener(regionServiceListener);
    }

    private RegionServiceListener regionServiceListener = new RegionServiceListener() {
        @Override
        public void onRegionsChange(ArrayList<Region> regions) {
            _regions.setValue(regions);
        }
    };

    public ArrayList<Region> getRegionsFromPath(ArrayList<String> pathChain){
        if(pathChain.isEmpty()){
            return RegionService.getFlatContinentAndCountriesList(regionService.regions);
        }else{
            ArrayList<Region> regions = RegionService.getFlatContinentAndCountriesList(regionService.regions);
            int index = pathChain.size() - 1;
            int minIndex = 0;
            Region region = null;
            while (index >= minIndex){
                region = getRegionFromName(regions, pathChain.get(index));
                regions = region.childRegions;
                index--;
            }

            if(region == null){
                return null;
            }else{
                return region.childRegions;
            }
        }
    }

    private Region getRegionFromName(ArrayList<Region> regions, String name){
        for(Region region : regions){
            if(region.name == name){
                return region;
            }
        }
        return null;
    }

}
