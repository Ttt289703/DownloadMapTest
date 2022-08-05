package com.example.downloadmapprototype.model.downloader;

import com.example.downloadmapprototype.model.data.Region;

public interface DownloadServiceListener {

    void onRegionChange(Region region);

    void onMemoryChange();
}
