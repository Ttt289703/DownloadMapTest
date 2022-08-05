package com.example.downloadmapprototype.contract;

import androidx.fragment.app.Fragment;

import com.example.downloadmapprototype.model.data.Region;

import java.util.ArrayList;


public interface Navigator{
    static Navigator getNavigator(Fragment fragment){
        return (Navigator) fragment.requireActivity();
    }

    void showRegionListFragment(ArrayList<Region> regions);

}
