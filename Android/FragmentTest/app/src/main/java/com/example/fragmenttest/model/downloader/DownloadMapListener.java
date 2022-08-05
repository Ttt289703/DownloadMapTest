package com.example.fragmenttest.model.downloader;

public interface DownloadMapListener {

    void onProgressChange(int progress);

    void onSuccess();

    void onError();

    void onStart(long downloadId);
}
