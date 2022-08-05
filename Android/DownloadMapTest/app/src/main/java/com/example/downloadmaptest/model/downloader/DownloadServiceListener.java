package com.example.downloadmaptest.model.downloader;

import com.example.downloadmaptest.model.data.Region;

public interface DownloadServiceListener {

    void onRegionChange(Region region);

    void onMemoryChange();
}
