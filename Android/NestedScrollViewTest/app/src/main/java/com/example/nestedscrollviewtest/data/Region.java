package com.example.nestedscrollviewtest.data;

import java.util.List;

public class Region {
    public String name;
    public boolean hasMap;
    public List<Region> childRegion;
    public String downloadURL;
    public State state;
    public Type type;

    public Region(){

    }

    public Region(String name, State state){
        this.name = name;
        this.state = state;
    }



    public enum State{
        unDownloaded,
        isDownloading,
        downloaded,
        expandable;
    }

    public enum Type{
        continent,
        hillshade,
        srtm,
        map
    }
}

