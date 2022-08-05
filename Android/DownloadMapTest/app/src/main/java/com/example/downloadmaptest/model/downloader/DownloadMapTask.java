package com.example.downloadmaptest.model.downloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.downloadmaptest.screens.regions.RegionService;

public class DownloadMapTask extends AsyncTask<Void, Integer, Void> {
    DownloadEntity downloadEntity;
    DownloadMapListener downloadMapListener;
    DownloadManager.Request request;
    Context context;
    DownloadManager downloadManager;
    long currentDownloadId;
    boolean isUpdating = false;
    boolean isDownloaded = false;
    boolean isCanceled = false;
    DownloadMapTask(Context context, DownloadEntity downloadEntity, DownloadMapListener listener){
        this.downloadEntity = downloadEntity;
        downloadMapListener = listener;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int progress = 0;
        Log.e("downloadEntity.URL downloadEntity.URL===", downloadEntity.URL);

        request = new DownloadManager.Request(Uri.parse(downloadEntity.URL));
        request.setTitle("Download " + downloadEntity.region.name);
        request.setDescription("Downloading " + downloadEntity.region.name);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        String subpath = RegionService.getRegionDownloadPath(downloadEntity.region) + "_" + RegionService.getContinent(downloadEntity.region).name + "_2.obf.zip";

        /*String subpath = RegionService.getRegionPath(downloadEntity.region) + "_" + RegionService.getContinent(downloadEntity.region).name + "_2.obf.zip";*/
        Log.e("subpath subpath===", subpath);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,subpath);

        currentDownloadId = downloadManager.enqueue(request);
        downloadMapListener.onStart(currentDownloadId);
        downloadEntity.id = currentDownloadId;

        isUpdating = true;
        try{
            while(progress < 100 && isUpdating){
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(currentDownloadId);
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();
                @SuppressLint("Range") long bytes_downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") long bytes_total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));


                int previousProgress = progress;
                progress = (int) ((bytes_downloaded * 100) / bytes_total);

                if(previousProgress != progress){
                    downloadMapListener.onProgressChange(progress);
                }
                publishProgress(progress);

                Thread.sleep(200);
                cursor.close();
            }
            if(progress == 100){
                isDownloaded = true;
            }
            isUpdating = false;
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public void cancelDownload(){
        isUpdating = false;
        downloadManager.remove(currentDownloadId);
        isCanceled = true;
    }

    public DownloadEntity getDownloadEntity(){
        return downloadEntity;
    }

    public long getDownloadId(){
        return currentDownloadId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if(isCanceled){
            downloadMapListener.onError();
        }else{
            Log.e("onPostExecute currentDownloadId===", String.valueOf(currentDownloadId));
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(currentDownloadId);
            Cursor cursor = downloadManager.query(query);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if(status == DownloadManager.STATUS_SUCCESSFUL) {
                downloadMapListener.onSuccess();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onCancelled(Void unused) {
        super.onCancelled(unused);
        downloadMapListener.onError();
    }
}
