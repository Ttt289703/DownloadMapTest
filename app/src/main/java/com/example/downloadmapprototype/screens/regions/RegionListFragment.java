package com.example.downloadmapprototype.screens.regions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.downloadmapprototype.App;
import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.contract.HasCustomTitle;
import com.example.downloadmapprototype.contract.Navigator;
import com.example.downloadmapprototype.databinding.FragmentRegionListBinding;
import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;

import java.util.ArrayList;

public class RegionListFragment extends Fragment implements HasCustomTitle {

    private static final String ARG_REGION_ORDER = "ARG_REGIONS_ORDER";
    private ArrayList<String> regionOrder;
    private FragmentRegionListBinding binding;
    private RegionsAdapter regionsAdapter;
    private RegionListViewModel viewModel;
    private String title;

    private ArrayList<String> getRegionOrder() {
        return requireArguments().getStringArrayList(ARG_REGION_ORDER);
    }

    private void updateTitle(ArrayList<Region> regions) {
        if (title != null && title.isEmpty()) {
            if (regions.get(0).parent != null) {
                this.title = regions.get(0).parent.displayName;
            } else {
                title = getResources().getString(R.string.app_name);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegionListBinding.inflate(inflater, container, false);
        regionOrder = getRegionOrder();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.regionRecyclerView.setLayoutManager(layoutManager);

        regionsAdapter = new RegionsAdapter(new RegionActionListener() {
            @Override
            public void onRegionDetails(Region region) {
                if (region.childRegions != null) {
                    showNextRegionFragment(region);
                }
            }

            @Override
            public void onRegionDownload(Region region) {
                if (region.state == Region.State.isDownloading) {
                    viewModel.cancelDownload(region);
                } else if (region.state == Region.State.unDownloaded) {
                    viewModel.downloadRegion(region);
                }
                regionsAdapter.notifyDataSetChanged();
            }
        });

        App app = (App) getActivity().getApplication();
        viewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(app, this)).get(RegionListViewModel.class);

        viewModel.regions.observe(getViewLifecycleOwner(), new Observer<RegionsData>() {
            @Override
            public void onChanged(RegionsData regionsData) {
                ArrayList<Region> _regions = getRegionsByOrder(regionsData.regions);
                regionsAdapter.setRegions(_regions);
                regionsAdapter.updateItem(regionsData.updatedRegion);
                updateTitle(_regions);
            }
        });
        binding.regionRecyclerView.setItemAnimator(null);
        binding.regionRecyclerView.setAdapter(regionsAdapter);
        return binding.getRoot();
    }

    public static RegionListFragment newInstance(ArrayList<String> regionOrder) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_REGION_ORDER, regionOrder);

        RegionListFragment fragment = new RegionListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void showNextRegionFragment(Region region) {
        Navigator navigator = Navigator.getNavigator(this);
        navigator.showRegionListFragment(RegionService.getRegionOrder(region));
    }

    public ArrayList<Region> getRegionsByOrder(ArrayList<Region> regions) {
        if (regionOrder.isEmpty()) {
            return regions;
        } else {
            int index = regionOrder.size() - 1;
            int minIndex = 0;
            Region region = null;
            while (index >= minIndex) {
                region = getRegionByName(regions, regionOrder.get(index));
                regions = region.childRegions;
                index--;
            }

            if (region == null) {
                return null;
            } else {
                return region.childRegions;
            }
        }
    }

    private Region getRegionByName(ArrayList<Region> regions, String name) {
        for (Region region : regions) {
            if (region.getName().equals(name)) {
                return region;
            }
        }
        return null;
    }
}
