package com.example.downloadmapprototype.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Region {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public boolean hasMap;
    public ArrayList<Region> childRegions;
    public State state;
    public Type type;
    public Region parent;
    public int downloadProgress = 0;
    public String displayName;

    public Region() {
    }

    public enum State {
        unDownloaded,
        isDownloading,
        downloaded
    }

    public enum Type {
        continent,
        hillshade,
        srtm,
        map,
        other
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public void setState(State state) {
        this.state = state;
    }

}

