package com.example.downloadmapprototype.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.contract.HasCustomTitle;
import com.example.downloadmapprototype.contract.Navigator;
import com.example.downloadmapprototype.databinding.ActivityMainBinding;
import com.example.downloadmapprototype.screens.regions.RegionListFragment;
import com.example.downloadmapprototype.screens.start.StartFragment;

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
            showStartFragment();
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

    private void showStartFragment() {
        launchFragment(StartFragment.newInstance(), false);
    }

    @Override
    public void showRegionListFragment(ArrayList<String> displayRegionQueue) {
        launchFragment(RegionListFragment.newInstance(displayRegionQueue), true);
    }
}