package com.example.downloadmapprototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.databinding.FragmentStartBinding;
import com.example.downloadmapprototype.model.data.Region;

import java.util.ArrayList;

public class StartFragment extends Fragment {
    private static final String ARG_REGIONS = "ARG_REGIONS";
    ArrayList<Region> regions;
    FragmentStartBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        regions = getRegions();

        RegionListFragment fragment = RegionListFragment.newInstance(regions);
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.regionListFragmentContainer, fragment)
                .commit();

        return binding.getRoot();
    }

    public ArrayList<Region> getRegions(){
        return requireArguments().getParcelableArrayList(ARG_REGIONS);
    }

    public static StartFragment newInstance(ArrayList<Region> regions) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_REGIONS, regions);

        StartFragment fragment = new StartFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
