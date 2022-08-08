package com.example.downloadmapprototype.screens.regions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;
import com.example.downloadmapprototype.model.data.RegionServiceListener;
import com.example.downloadmapprototype.model.downloader.DownloadMapService;
import com.example.downloadmapprototype.model.downloader.DownloadMapServiceListener;

import java.util.ArrayList;

class RegionsData {
    ArrayList<Region> regions;
    Region updatedRegion;
}

public class RegionListViewModel extends ViewModel {
    private RegionService regionService;
    private DownloadMapService downloadMapService;

    private MutableLiveData<RegionsData> _regions = new MutableLiveData<>();
    LiveData<RegionsData> regions = _regions;

    public RegionListViewModel(RegionService regionService, DownloadMapService downloadMapService) {
        this.regionService = regionService;
        this.downloadMapService = downloadMapService;
        regionService.registerListener(regionServiceListener);
        downloadMapService.registerListener(downloadMapServiceListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        regionService.unregisterListener(regionServiceListener);
        downloadMapService.unRegisterListener(downloadMapServiceListener);
    }

    private RegionServiceListener regionServiceListener = new RegionServiceListener() {
        @Override
        public void onRegionsChange(ArrayList<Region> regions, Region updatedRegion) {
            RegionsData data = new RegionsData();
            data.regions = regions;
            data.updatedRegion = updatedRegion;
            _regions.setValue(data);
        }
    };

    private DownloadMapServiceListener downloadMapServiceListener = new DownloadMapServiceListener() {
        @Override
        public void onRegionsProgressChange(Region region, int progress) {
            regionService.setRegionProgress(region, progress);
        }

        @Override
        public void onDownloaded(Region region) {
            regionService.setRegionState(region, Region.State.downloaded);
        }

        @Override
        public void onStartDownloading(Region region) {
            regionService.setRegionState(region, Region.State.isDownloading);
        }

        @Override
        public void onCancelDownloading(Region region) {
            regionService.setRegionState(region, Region.State.unDownloaded);
            regionService.setRegionProgress(region, 0);
        }

        @Override
        public void onMemoryChange() {
        }
    };

    public void downloadRegion(Region region) {
        downloadMapService.downloadMap(region);
    }

    public void cancelDownload(Region region) {
        downloadMapService.cancelDownloading(region);
    }

}
