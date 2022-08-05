package com.example.downloadmapprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.downloadmapprototype.contract.HasCustomTitle;
import com.example.downloadmapprototype.contract.Navigator;
import com.example.downloadmapprototype.databinding.ActivityMainBinding;
import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;
import com.example.downloadmapprototype.model.data.RegionDataHolder;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Navigator {

    private ActivityMainBinding binding;

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
    }

    private final FragmentManager.FragmentLifecycleCallbacks fragmentListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentResumed(fm, f);
            updateUi();
        }

        @Override
        public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentStarted(fm, f);
            updateUi();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            ArrayList<Region> flatListOfRegions = RegionService.getFlatContinentAndCountriesList(RegionDataHolder.regions);
            showStartFragment(flatListOfRegions);

        }
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentListener, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateUi() {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof HasCustomTitle) {
            getSupportActionBar().setTitle(((HasCustomTitle) fragment).getTitle());
        } else {
            getSupportActionBar().setTitle("Download Maps");
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    private void launchFragment(Fragment fragment, boolean replace) {
        if (replace) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainerView, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, fragment).commit();

        }
    }

    private void showStartFragment(ArrayList<Region> regions) {
        launchFragment(StartFragment.newInstance(regions), false);
    }

    @Override
    public void showRegionListFragment(ArrayList<Region> regions) {
        launchFragment(RegionListFragment.newInstance(regions), true);
    }
}