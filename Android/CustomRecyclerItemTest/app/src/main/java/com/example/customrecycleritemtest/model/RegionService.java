package com.example.customrecycleritemtest.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RegionService {

    private TreeSet<Region> regions = new TreeSet<Region>();
    private MutableLiveData<TreeSet<Region>> liveData = new MutableLiveData<>();
    private ArrayList<Region> regions2 = new ArrayList<>();



    public ArrayList<Region> getRegions(){
        return regions2;
    }
}
