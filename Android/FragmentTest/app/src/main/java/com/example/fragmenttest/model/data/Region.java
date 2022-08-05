package com.example.fragmenttest.model.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Region implements Parcelable {
    public String name;
    public boolean hasMap;
    public ArrayList<Region> childRegions;
    public State state;
    public Type type;
    public Region parent;
    public int downloadProgress = 0;
    public String displayName;

    public Region() {
    }

    public Region(String name, State state) {
        this.name = name;
        this.state = state;
    }

    public Region(Parcel in) {
        this.name = in.readString();
        this.hasMap = in.readByte() != 0;
        this.childRegions = in.readArrayList(Region.class.getClassLoader());
        this.state = (State) in.readSerializable();
        this.type = (Type) in.readSerializable();
        this.parent = in.readParcelable(Region.class.getClassLoader());
        this.downloadProgress = in.readInt();
        this.displayName = in.readString();
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

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel in) {
            return new Region(in);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeByte((byte) (hasMap ? 1 : 0));
        parcel.writeList(childRegions);
        parcel.writeSerializable(state);
        parcel.writeSerializable(type);
        parcel.writeParcelable(parent, 0);
        parcel.writeInt(downloadProgress);
        parcel.writeString(displayName);
    }
}

