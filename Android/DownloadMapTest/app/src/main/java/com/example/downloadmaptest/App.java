package com.example.downloadmaptest;

import android.app.Application;

import com.example.downloadmaptest.model.downloader.DownloadService;
import com.example.downloadmaptest.screens.regions.RegionService;

public class App extends Application {
    private RegionService regionService;


    @Override
    public void onCreate() {
        super.onCreate();
        regionService = RegionService.getInstance(this);
        DownloadService.getInstance().setContext(this);
    }

    public RegionService getRegionService(){
        return regionService;
    }
}
