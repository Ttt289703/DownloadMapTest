package com.example.customrecycleritemtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.customrecycleritemtest.model.Region;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Region> regions = new ArrayList<Region>();

        regions.add(new Region("Germany"));
        regions.add(new Region("Ukraine"));
        regions.add(new Region("Poland"));
        regions.add(new Region("Spain"));
        regions.add(new Region("Belarus"));
        regions.add(new Region("France"));
        regions.add(new Region("Finland"));
        regions.add(new Region("asd"));
        regions.add(new Region("asd1"));
        regions.add(new Region("asd2"));
        regions.add(new Region("asd3"));
        regions.add(new Region("asd4"));
        regions.add(new Region("asd5"));
        regions.add(new Region("asd6"));
        regions.add(new Region("asd7"));

        RegionAdapter regionAdapter = new RegionAdapter(regions);
        RecyclerView recyclerView = findViewById(R.id.rv_regions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(regionAdapter);

    }
}