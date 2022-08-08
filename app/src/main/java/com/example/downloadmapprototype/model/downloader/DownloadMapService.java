package com.example.downloadmapprototype.model.downloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;

public class DownloadMapService {
    public final static String BASEURL = "https://download.osmand.net/download.php?standard=yes&file=";
    public final static String DOWNLOAD_SUFFIX = "_2.obf.zip";
    public final static String DOWNLOAD_DIRECTORY = "/Test/maps/";
    public final static String TITLE_PREFIX = "Downloading ";
    public final static String DESCRIPTION_PREFIX = "Download ";

    private Context context;
    private ArrayList<DownloadMapServiceListener> listeners = new ArrayList<>();
    private boolean isDownloading;
    private ArrayList<Region> downloadQueue;
    private DownloadMapTask currentDownloadTask;

    public static DownloadMapService getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadMapService(context);
        }
        return instance;
    }

    private DownloadMapService(Context context) {
        downloadQueue = new ArrayList<>();
        this.context = context;
    }

    private static DownloadMapService instance = null;

    public String getFullRegionPath(String path) {
        String regionPath = path;
        if (!regionPath.equals("")) {
            path = path.substring(0, 1).toUpperCase() + path.substring(1);
        }
        return new StringBuilder(BASEURL)
                .append(path)
                .append(DOWNLOAD_SUFFIX)
                .toString();
    }

    public void downloadMap(Region region) {
        downloadQueue.add(region);
        notifyOnStartDownloading(region);
        if (!isDownloading) {
            isDownloading = true;
            startDownloading(region);
        }
    }

    private void startDownloading(Region region) {
        currentDownloadTask = new DownloadMapTask();
        currentDownloadTask.setCurrentRegion(region);
        currentDownloadTask.execute();
    }

    public void cancelDownloading(Region region) {
        if (downloadQueue.size() >= 1) {
            Region currentTaskRegion = currentDownloadTask.getCurrentRegion();

            if (currentTaskRegion != null) {
                if (currentTaskRegion.equals(region)) {
                    currentDownloadTask.cancelDownloading();
                }
            }
            downloadQueue.remove(region);
            region.state = Region.State.unDownloaded;
        }
    }

    private String getFileStoragePath(String name) {
        return DOWNLOAD_DIRECTORY + name + DOWNLOAD_SUFFIX;
    }

    public void registerListener(DownloadMapServiceListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unRegisterListener(DownloadMapServiceListener listener) {
        if (!listeners.isEmpty()) {
            listeners.remove(listener);
        }
    }

    private void notifyOnRegionsProgressChange(Region region, int progress) {
        for (DownloadMapServiceListener listener : listeners) {
            listener.onRegionsProgressChange(region, progress);
            listener.onMemoryChange();
        }
    }

    private void notifyOnDownload(Region region) {
        for (DownloadMapServiceListener listener : listeners) {
            listener.onDownloaded(region);
            listener.onMemoryChange();
        }
    }

    private void notifyOnStartDownloading(Region region) {
        for (DownloadMapServiceListener listener : listeners) {
            listener.onStartDownloading(region);
            listener.onMemoryChange();
        }
    }

    private void notifyOnCancelDownloading(Region region) {
        for (DownloadMapServiceListener listener : listeners) {
            listener.onCancelDownloading(region);
            listener.onMemoryChange();
        }
    }

    private class DownloadMapTask extends AsyncTask<Void, Integer, Void> {
        private DownloadManager downloadManager;
        private Region currentRegion;
        private long currentRegionId;
        private boolean downloadCancelled;

        @SuppressLint("Range")
        @Override
        protected Void doInBackground(Void... params) {
            String fullRegionPath = RegionService.getFullRegionPath(currentRegion);
            String url = getFullRegionPath(fullRegionPath);
            String fileStoragePath = getFileStoragePath(fullRegionPath);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle(TITLE_PREFIX + currentRegion.getName());
            request.setDescription(DESCRIPTION_PREFIX + currentRegion.getName() + DOWNLOAD_SUFFIX);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileStoragePath);

            downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            currentRegionId = downloadManager.enqueue(request);

            int progress = 0;
            long totalBytes;
            long downloadedBytes;

            try {
                while (progress < 100 && !downloadCancelled) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(currentRegionId);
                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();

                    downloadedBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    progress = (int) ((downloadedBytes * 100) / totalBytes);

                    publishProgress(progress);

                    Thread.sleep(200);
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void cancelDownloading() {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.remove(currentRegionId);
            downloadCancelled = true;
            notifyOnCancelDownloading(currentRegion);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            notifyOnRegionsProgressChange(currentRegion, progress);
            currentRegion.downloadProgress = progress;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (downloadCancelled) {
                downloadCancelled = false;
            } else {
                notifyOnDownload(currentRegion);
                downloadQueue.remove(currentRegion);
            }

            downloadNextRegion();
        }

        private void downloadNextRegion() {
            if (downloadQueue.size() > 0) {
                startDownloading(downloadQueue.get(0));
            } else {
                isDownloading = false;
            }
        }

        public Region getCurrentRegion() {
            return currentRegion;
        }

        public void setCurrentRegion(Region currentRegion) {
            this.currentRegion = currentRegion;
        }
    }
}