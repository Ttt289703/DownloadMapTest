package com.example.downloadmapprototype;

import android.app.Application;

import com.example.downloadmapprototype.model.data.RegionDataHolder;
import com.example.downloadmapprototype.model.downloader.DownloadService;

public class App extends Application {
    //RegionService regionService = new RegionService();
    //RegionDownloader regionDownloader = new RegionDownloader();


    @Override
    public void onCreate() {
        super.onCreate();
        RegionDataHolder dataHolder = new RegionDataHolder(this);
        DownloadService.getInstance().setContext(this);
    }

}
