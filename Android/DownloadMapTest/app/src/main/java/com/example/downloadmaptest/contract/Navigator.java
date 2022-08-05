package com.example.downloadmaptest.contract;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public interface Navigator {
    static Navigator getNavigator(Fragment fragment){
        return (Navigator) fragment.requireActivity();
    }

    void showRegionListFragment(ArrayList<String> pathChain);
}
