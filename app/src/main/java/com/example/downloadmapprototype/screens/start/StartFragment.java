package com.example.downloadmapprototype.screens.start;

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
import com.example.downloadmapprototype.model.downloader.DownloadMapService;
import com.example.downloadmapprototype.model.downloader.DownloadMapServiceListener;
import com.example.downloadmapprototype.screens.regions.RegionListFragment;

import java.util.ArrayList;

public class StartFragment extends Fragment {
    FragmentStartBinding binding;
    DownloadMapService downloadMapService;
    DeviceMemorView memoryView;

    DownloadMapServiceListener downloadMapServiceListener = new DownloadMapServiceListener() {
        @Override
        public void onRegionsProgressChange(Region region, int progress) {
        }

        @Override
        public void onDownloaded(Region region) {
        }

        @Override
        public void onStartDownloading(Region region) {
        }

        @Override
        public void onCancelDownloading(Region region) {
        }

        @Override

        public void onMemoryChange() {
            synchronized (memoryView) {
                memoryView.updateProgress();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        memoryView = binding.getRoot().findViewById(R.id.device_memory_view);
        downloadMapService = DownloadMapService.getInstance(getContext());
        downloadMapService.registerListener(downloadMapServiceListener);
        RegionListFragment fragment = RegionListFragment.newInstance(new ArrayList<>());
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
