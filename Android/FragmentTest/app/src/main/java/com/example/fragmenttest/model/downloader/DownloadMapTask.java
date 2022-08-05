package com.example.fragmenttest.model.downloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.fragmenttest.model.data.RegionService;

public class DownloadMapTask extends AsyncTask<Void, Integer, Void> {
    DownloadEntity downloadEntity;
    DownloadMapListener downloadMapListener;
    DownloadManager.Request request;
    Context context;
    DownloadManager downloadManager;
    long currentDownloadId;
    boolean isUpdating = false;
    boolean isCanceled = false;

    DownloadMapTask(Context context, DownloadEntity downloadEntity, DownloadMapListener listener) {
        this.downloadEntity = downloadEntity;
        downloadMapListener = listener;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int progress = 0;

        request = new DownloadManager.Request(Uri.parse(downloadEntity.URL));
        request.setTitle("Download " + downloadEntity.region.name);
        request.setDescription("Downloading " + downloadEntity.region.name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String subpath = RegionService.getRegionPath(downloadEntity.region) + "_" + RegionService.getContinent(downloadEntity.region).name + "_2.obf.zip";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subpath);

        currentDownloadId = downloadManager.enqueue(request);
        downloadMapListener.onStart(currentDownloadId);
        downloadEntity.id = currentDownloadId;

        isUpdating = true;
        try {
            while (progress < 100 && isUpdating) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(currentDownloadId);
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();
                @SuppressLint("Range") long bytes_downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") long bytes_total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                progress = (int) ((bytes_downloaded * 100) / bytes_total);
                publishProgress(progress);

                Thread.sleep(50);
                cursor.close();
            }
            Thread.sleep(400);
            isUpdating = false;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void cancelDownload() {
        isUpdating = false;
        downloadManager.remove(currentDownloadId);
        isCanceled = true;
    }

    public DownloadEntity getDownloadEntity() {
        return downloadEntity;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        downloadMapListener.onProgressChange(values[0]);
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if (isCanceled) {
            downloadMapListener.onError();
        } else {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(currentDownloadId);
            Cursor cursor = downloadManager.query(query);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                downloadMapListener.onSuccess();
            }
        }
    }

    @Override
    protected void onCancelled(Void unused) {
        super.onCancelled(unused);
        downloadMapListener.onError();
    }
}
