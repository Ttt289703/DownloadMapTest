package com.example.customrecycleritemtest.model;

public class Region {
    public Region(String name){
        this.name = name;
    }
    public String name;
    public State state;
    public String irl;
}

enum State{
    unDownloaded,
    isDownloading,
    downloaded,
    expandable;
}
