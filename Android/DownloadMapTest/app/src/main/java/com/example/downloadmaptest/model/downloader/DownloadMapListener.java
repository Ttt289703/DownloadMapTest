package com.example.downloadmaptest.model.downloader;

public interface DownloadMapListener {

    void onProgressChange(int progress);

    void onSuccess();

    void onError();

    void onStart(long downloadId);
}
