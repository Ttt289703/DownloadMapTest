package com.example.downloadmapprototype.model.data;

import java.util.ArrayList;

public interface RegionServiceListener {
    void onRegionsChange(ArrayList<Region> regions, Region updatedRegion);
}
