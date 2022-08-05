package com.example.fragmenttest.model.downloader;

import com.example.fragmenttest.model.data.Region;

public interface DownloadServiceListener {

    void onRegionChange(Region region);

    void onMemoryChange();
}
