package com.example.downloadmapprototype.model.downloader;

import com.example.downloadmapprototype.model.data.Region;

public interface DownloadMapServiceListener {
    void onRegionsProgressChange(Region region, int progress);

    void onDownloaded(Region region);

    void onStartDownloading(Region region);

    void onCancelDownloading(Region region);

    void onMemoryChange();
}
