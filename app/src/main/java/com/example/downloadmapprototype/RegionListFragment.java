package com.example.downloadmapprototype;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.downloadmapprototype.contract.HasCustomTitle;
import com.example.downloadmapprototype.contract.Navigator;
import com.example.downloadmapprototype.databinding.FragmentRegionListBinding;
import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;
import com.example.downloadmapprototype.model.downloader.DownloadService;
import com.example.downloadmapprototype.model.downloader.DownloadServiceListener;

import java.util.ArrayList;

public class RegionListFragment extends Fragment implements HasCustomTitle {
    private static final String ARG_REGIONS = "ARG_REGIONS";

    private FragmentRegionListBinding binding;
    private RegionsAdapter regionsAdapter;
    private ArrayList<Region> regions;

    public ArrayList<Region> getRegions() {
        return requireArguments().getParcelableArrayList(ARG_REGIONS);
    }

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
                        }
                    };
                    mainHandler.post(myRunnable);
                } catch (Exception e) {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.regionRecyclerView.setLayoutManager(layoutManager);
        regions = getRegions();

        regionsAdapter = new RegionsAdapter(regions.size(), regions, new RegionActionListener() {
            @Override
            public void onRegionDetails(Region region, int index) {
                Toast.makeText(getContext(), "Region: " + region.name, Toast.LENGTH_SHORT).show();
                if (region.childRegions != null) {
                    showNextRegionFragment(region.childRegions);
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

    public static RegionListFragment newInstance(ArrayList<Region> regions) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_REGIONS, regions);
        RegionListFragment fragment = new RegionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return regions.get(0).parent.name;
    }

    private void showNextRegionFragment(ArrayList<Region> regions) {
        Navigator navigator = Navigator.getNavigator(this);
        navigator.showRegionListFragment(regions);
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
