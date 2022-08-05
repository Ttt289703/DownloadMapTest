package com.example.downloadmaptest.screens;

import android.os.Bundle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.downloadmaptest.R;
import com.example.downloadmaptest.databinding.FragmentStartBinding;
import com.example.downloadmaptest.screens.regions.RegionListFragment;

import java.util.ArrayList;

public class StartFragment extends Fragment {
    private static final String ARG_REGIONS = "ARG_REGIONS_CHAIN";
    private static final String ARG_MAIN_FRAGMENT = "ARG_MAIN_FRAGMENT";
    FragmentStartBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);

        RegionListFragment fragment = RegionListFragment.newInstance(new ArrayList<String>());
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.regionListFragmentContainer, fragment)
                .commit();

        return binding.getRoot();
    }

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }
}
