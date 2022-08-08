package com.example.downloadmapprototype;

import android.app.Application;

import com.example.downloadmapprototype.model.data.RegionService;
import com.example.downloadmapprototype.model.downloader.DownloadMapService;

public class App extends Application {
    RegionService regionService;
    DownloadMapService downloadMapService;
    @Override
    public void onCreate() {
        super.onCreate();
        regionService = RegionService.getInstance(this);
        downloadMapService = DownloadMapService.getInstance(this);
    }

    public RegionService getRegionService(){
        return regionService;
    }

    public DownloadMapService getDownloadMapService(){
        return downloadMapService;
    }

}
