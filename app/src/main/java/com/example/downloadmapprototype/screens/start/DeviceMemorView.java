package com.example.downloadmapprototype.screens.start;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.databinding.DeviceMemoryViewBinding;
import com.example.downloadmapprototype.databinding.FragmentStartBinding;
import com.example.downloadmapprototype.model.util.MemoryService;

import java.io.File;

public class DeviceMemorView extends ConstraintLayout {

    private TextView freeSpaceView;
    private ProgressBar progressBar;
    Context context;

    public DeviceMemorView(@NonNull Context context) {
        super(context);
        init(context);
        updateProgress();
    }

    public DeviceMemorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        updateProgress();
    }

    public DeviceMemorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        updateProgress();
    }

    public DeviceMemorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        updateProgress();
    }

    public void updateProgress() {
        int progress = MemoryService.getUsedMemoryPercent();
        progressBar.setProgress(progress);

        String format = Formatter.formatFileSize(context, MemoryService.getAvailableBlockSize() * MemoryService.getBlockSize());
        freeSpaceView.setText(String.format("Available Memory: %s", format));
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.device_memory_view, this, true);

        freeSpaceView = this.findViewById(R.id.tv_freeMemory);
        progressBar = this.findViewById(R.id.progressBar_freeMemory);
    }
}
