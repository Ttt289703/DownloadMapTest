package com.example.downloadmaptest.model.downloader;

import android.content.Context;
import android.util.Log;

import com.example.downloadmaptest.model.data.Region;
import com.example.downloadmaptest.screens.regions.RegionService;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class DownloadService {
    private static final String SUFFIX = "_2.obf.zip";
    private static final String BASEURL = "https://download.osmand.net/download.php?standard=yes&file=";

    private DownloadMapTask currentDownloadTask = null;
    private Context context;

    ArrayList<DownloadServiceListener> listeners = new ArrayList<>();

    public ArrayDeque<DownloadEntity> downloadQueue = new ArrayDeque<>();

    public void setContext(Context context) {
        this.context = context;
    }

    private static class Instance {
        private final static DownloadService instance = new DownloadService();
    }

    public static DownloadService getInstance() {
        return Instance.instance;
    }

    public void removeFromDownloadQueue(Region region) {
        DownloadEntity searchableEntity = null;
        for (DownloadEntity entity : downloadQueue) {
            if (entity.region == region) {
                searchableEntity = entity;
            }
        }
        if (currentDownloadTask != null && currentDownloadTask.getDownloadEntity() == searchableEntity) {
            currentDownloadTask.cancelDownload();
            downloadQueue.remove(searchableEntity);
            region.state = Region.State.unDownloaded;
        } else {
            downloadQueue.remove(searchableEntity);
            region.state = Region.State.unDownloaded;
        }
    }

    public void addToDownloadQueue(Region region) {
        region.state = Region.State.isDownloading;
        downloadQueue.addLast(makeEntity(region));
        downloadNextEntity();
    }


    public void downloadNextEntity() {
        if (!downloadQueue.isEmpty() && currentDownloadTask == null) {
            startDownloadTask(downloadQueue.getFirst());
        }
    }

    private DownloadEntity makeEntity(Region region) {
        DownloadEntity result = new DownloadEntity();
        result.region = region;
        /*result.URL = BASEURL + RegionService.getFullRegionPath(region) + SUFFIX;*/
        result.URL = BASEURL + RegionService.getFullRegionDownloadPath(region) + SUFFIX;
        Log.e("makeEntity-URL", result.URL);

        return result;
    }

    public boolean isRegionDownloading(Region region) {
        for (DownloadEntity entity : downloadQueue) {
            if (entity.region == region) {
                return true;
            }
        }
        return false;
    }

    private void startDownloadTask(DownloadEntity entity) {
        currentDownloadTask = new DownloadMapTask(context, entity, new DownloadMapListener() {
            @Override
            public void onProgressChange(int progress) {
                Log.e("currentDownloadTas===", "onProgressChange");

                currentDownloadTask.downloadEntity.region.downloadProgress = progress;
                notifyListenersOnRegionChange(currentDownloadTask.downloadEntity.region);
                notifyListenersOnMemoryChange();
            }

            @Override
            public void onSuccess() {
                Log.e("currentDownloadTask===", "onSuccess");
                removeFromDownloadQueue(currentDownloadTask.downloadEntity.region);
                currentDownloadTask.downloadEntity.region.state = Region.State.downloaded;
                notifyListenersOnRegionChange(currentDownloadTask.downloadEntity.region);
                notifyListenersOnMemoryChange();
                currentDownloadTask = null;
                downloadNextEntity();
            }

            @Override
            public void onError() {
                Log.e("currentDownloadTask===", "onError");

                removeFromDownloadQueue(currentDownloadTask.downloadEntity.region);
                currentDownloadTask.downloadEntity.region.state = Region.State.unDownloaded;
                notifyListenersOnRegionChange(currentDownloadTask.downloadEntity.region);
                notifyListenersOnMemoryChange();
                currentDownloadTask = null;
                downloadNextEntity();
            }

            @Override
            public void onStart(long downloadId) {
                Log.e("currentDownloadTask===", "onStart");

                currentDownloadTask.downloadEntity.region.state = Region.State.isDownloading;
                notifyListenersOnRegionChange(currentDownloadTask.downloadEntity.region);
            }
        });
        currentDownloadTask.execute();
    }

    public void registerListener(DownloadServiceListener listener) {
        listeners.add(listener);
    }

    public void unRegisterListener(DownloadServiceListener listener) {
        listeners.remove(listener);
    }

    private void notifyListenersOnRegionChange(Region region) {
        for (DownloadServiceListener listener : listeners) {
            listener.onRegionChange(region);
        }
    }

    private void notifyListenersOnMemoryChange() {
        for (DownloadServiceListener listener : listeners) {
            listener.onMemoryChange();
        }
    }

}
