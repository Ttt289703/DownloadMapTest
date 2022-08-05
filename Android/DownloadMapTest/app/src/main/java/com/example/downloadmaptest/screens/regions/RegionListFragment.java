package com.example.downloadmaptest.screens.regions;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.downloadmaptest.App;
import com.example.downloadmaptest.ViewModelFactory;
import com.example.downloadmaptest.contract.HasCustomTitle;
import com.example.downloadmaptest.contract.Navigator;
import com.example.downloadmaptest.databinding.FragmentRegionListBinding;
import com.example.downloadmaptest.model.data.Region;
import com.example.downloadmaptest.model.downloader.DownloadService;
import com.example.downloadmaptest.model.downloader.DownloadServiceListener;

import java.util.ArrayList;


public class RegionListFragment extends Fragment implements HasCustomTitle {
    private static final String ARG_CHAIN = "ARG_REGIONS_CHAIN";

    private FragmentRegionListBinding binding;
    private RegionListViewModel viewModel;
    private ArrayList<Region> regions;
    private ArrayList<String> pathChain;
    RegionsAdapter regionsAdapter;


    private Observer<ArrayList<Region>> viewModelObserver = new Observer<ArrayList<Region>>() {
        @Override
        public void onChanged(ArrayList<Region> stringRegionTreeMap) {

        }
    };

    DownloadServiceListener downloadServiceListener = new DownloadServiceListener() {
        @Override
        public void onRegionChange(Region region) {
            int index = regions.indexOf(region);
            if (index != -1) {
                try {
                    Handler mainHandler = new Handler(getContext().getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            regionsAdapter.notifyItemChanged(index);
                        } // This is your code
                    };
                    mainHandler.post(myRunnable);
                    //regionsAdapter.notifyItemChanged(index);

                } catch (Exception e) {
                    Log.e("Exception", String.valueOf(e));
                }
            }
        }

        @Override
        public void onMemoryChange() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegionListBinding.inflate(inflater, container, false);

        App app = (App) getActivity().getApplication();
        viewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(app, this)).get(RegionListViewModel.class);
        viewModel.regions.observe(getViewLifecycleOwner(), viewModelObserver);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.regionRecyclerView.setLayoutManager(layoutManager);

        pathChain = getPathChain();
        regions = viewModel.getRegionsFromPath(pathChain);

        regionsAdapter = new RegionsAdapter(regions.size(), regions, new RegionActionListener() {
            @Override
            public void onRegionDetails(Region region, int index) {
                if (region.childRegions != null) {
                    showNextRegionFragment(region);
                }
            }

            @Override
            public void onRegionDownload(Region region, int index) {
                DownloadService downloadService = DownloadService.getInstance();

                if (region.state == Region.State.isDownloading) {
                    downloadService.removeFromDownloadQueue(region);
                    downloadService.downloadNextEntity();
                } else if (region.state == Region.State.unDownloaded) {
                    downloadService.addToDownloadQueue(region);

                } else if (region.state == Region.State.downloaded) {

                }

                regionsAdapter.notifyItemChanged(index);
            }
        });

        binding.regionRecyclerView.setAdapter(regionsAdapter);
        return binding.getRoot();
    }

    private ArrayList<String> getPathChain() {
        return requireArguments().getStringArrayList(ARG_CHAIN);
    }

    public static RegionListFragment newInstance(ArrayList<String> pathChain) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CHAIN, pathChain);

        RegionListFragment fragment = new RegionListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void showNextRegionFragment(Region region) {
        Navigator navigator = Navigator.getNavigator(this);
        navigator.showRegionListFragment(RegionService.getPathChain(region));
    }

    @Override
    public String getTitle() {
        return regions.get(0).parent.displayName;
    }

    @Override
    public void onResume() {
        super.onResume();
        DownloadService.getInstance().registerListener(downloadServiceListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        DownloadService.getInstance().unRegisterListener(downloadServiceListener);

    }


}
