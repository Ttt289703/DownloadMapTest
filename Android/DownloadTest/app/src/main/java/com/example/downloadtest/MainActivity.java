package com.example.downloadtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.downloadtest.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RegionsAdapter regionsAdapter;

    ArrayList<Region> regions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        regions = RegionService.getRegions(this);

        regionsAdapter = new RegionsAdapter(regions.size(), regions, new RegionActionListener() {
            @Override
            public void onRegionDetails(Region region, int index) {
                Toast.makeText(getApplicationContext(), "Region: " + region.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegionDownload(Region region, int index) {
                Toast.makeText(getApplicationContext(), "Region: " + region.name, Toast.LENGTH_SHORT).show();
                regionsAdapter.notifyItemChanged(index);
                download();

            }
        });


        binding.recyclerViewRegion.setAdapter(regionsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewRegion.setLayoutManager(layoutManager);



    }

    public void download(){
        String URL = "https://download.osmand.net/download.php?standard=yes&file=France_corse_europe_2.obf.zip";
        Downloader downloader = new Downloader(URL, this, this);
    }
}