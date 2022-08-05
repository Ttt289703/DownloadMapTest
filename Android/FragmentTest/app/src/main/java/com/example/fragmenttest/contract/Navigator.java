package com.example.fragmenttest.contract;

import androidx.fragment.app.Fragment;

import com.example.fragmenttest.model.data.Region;

import java.util.ArrayList;


public interface Navigator{
    static Navigator getNavigator(Fragment fragment){
        return (Navigator) fragment.requireActivity();
    }

    void showRegionListFragment(ArrayList<Region> regions);

}
