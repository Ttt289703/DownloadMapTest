package com.example.downloadmapprototype.screens.regions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.downloadmapprototype.App;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private App app;
    private Fragment fragment;

    public ViewModelFactory(App app, Fragment fragment) {
        this.app = app;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (RegionListViewModel.class.equals(modelClass)) {
            ViewModel viewModel = new RegionListViewModel(app.getRegionService(), app.getDownloadMapService());
            return (T) viewModel;
        } else {
            throw new IllegalStateException("Unknown view model class");
        }
    }
}
