package com.example.downloadtest;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Downloader {
    DownloadManager.Request request;

    public final static String SUFFIX = "_2.obf.zip";
    public final static String FILE_DIRECTORY = "/OsmAnd/map/";


    public final static String TITLE_PREFIX = "Downloading ";
    public final static String DESCRIPTION_PREFIX = "Download ";

    String URL;
    Context context;
    Activity activity;

    Downloader(String _URL, Context context, Activity activity){
        this.context = context;

        //testDownload();
        testDownload2();

     /*   this.URL = _URL;
        this.context = context;
        this.activity = activity;
        request = new DownloadManager.Request(Uri.parse(URL));
        String title = URLUtil.guessFileName(URL, null, null);



        //request.setTitle(title);
        request.setTitle("Download");
        request.setDescription("Downloading ");

        //String cookie = CookieManager.getInstance().getCookie(URL);
        //request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getResultFileName("Denmark_europe"));

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(context, "Downloading started", Toast.LENGTH_SHORT).show();*/
    }

    private String getResultFileName(String name) {

        return FILE_DIRECTORY + name + SUFFIX;
    }

    public void testDownload2() {
        Uri uri = Uri.parse("http://download.osmand.net/download.php?standard=yes&file=France_corse_europe_2.obf.zip");
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);

            //Setting title of request
            request.setTitle("Denmark.zip");

            //Setting description of request
            request.setDescription("Your file is downloading");

            //set notification when download completed
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Denmark.zip");

            request.allowScanningByMediaScanner();

            //Enqueue download and save the referenceId
            long downloadReference = downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {
            Log.e("kek", "Line no: 455,Method: downloadFile: Download link is broken");

        }
    }



    public void testDownload(){
        DownloadMapAsyncTask currentTask;
        currentTask = new DownloadMapAsyncTask();
        currentTask.execute();
    }

    public void download(){
        if(checkPermission()){

        }else{
            requestPermission();
        }
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "Give permission to download", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    private class DownloadMapAsyncTask extends AsyncTask<Void , Integer, Void> {
        private DownloadManager downloadManager;
        private long currentDownloadId;
        private boolean downloadInterrupted;

        @SuppressLint("Range")
        @Override
        protected Void doInBackground(Void... params) {

            String name = "Denmark";
            String fullRegionPath = "Denmark_europe";
            String url = "http://speedtest-sgp1.digitalocean.com/5gb.test";
            String resultFileName = getResultFileName(fullRegionPath);

            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

            request.setTitle(TITLE_PREFIX + name);
            request.setDescription(DESCRIPTION_PREFIX + fullRegionPath + SUFFIX);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, resultFileName);
            downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

            currentDownloadId = downloadManager.enqueue(request);

            long bytes_total;
            long bytes_downloaded;
            int progress = 0;

            try {
                while (progress < 100 && !downloadInterrupted) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(currentDownloadId);
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();

                    bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    progress = (int) ((bytes_downloaded * 100) / bytes_total);

                    publishProgress(progress);

                    Thread.sleep(10);

                    cursor.close();
                }
            } catch (Exception e){}
            return null;
        }

        public void cancelDownloading(){
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.remove(currentDownloadId);
            downloadInterrupted = true;
        }

        public long getCurrentDownloadId() {
            return currentDownloadId;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }


    }
}
