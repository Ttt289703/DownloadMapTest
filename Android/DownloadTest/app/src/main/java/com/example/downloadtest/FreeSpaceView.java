package com.example.downloadtest;

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

import java.io.File;

public class FreeSpaceView extends ConstraintLayout {
    private static final long MEGA_BYTE = 1048576;



    public FreeSpaceView(@NonNull Context context) {
        super(context);
        init(context);

    }

    public FreeSpaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public FreeSpaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public FreeSpaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.free_space_view, this, true);

        freeSpaceView = this.findViewById(R.id.tv_freeMemory);
        progressBar = this.findViewById(R.id.progressBar_freeMemory);

        progressBar.setProgress(50);
        //Log.e("asd", String.valueOf(getFreeMemory(Environment.getDataDirectory())));


        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stats.getAvailableBlocksLong() * stats.getBlockSizeLong();
        long megAvailable = bytesAvailable / 1048576;
        Log.e("", "Available MB : " + megAvailable);
        File path = Environment.getDataDirectory();
        StatFs stat2 = new StatFs(path.getPath());
        long blockSize = stat2.getBlockSize();
        long availableBlocks = stat2.getAvailableBlocks();
        String format = Formatter.formatFileSize(context, availableBlocks * blockSize);
        freeSpaceView.setText(String.format("Available Memory: %s", format));

        Log.e("int total space", String.valueOf(totalSpace(false)));
        Log.e("int free space", String.valueOf(freeSpace(false)));
        Log.e("ext total space", String.valueOf(totalSpace(true)));
        Log.e("ext free space", String.valueOf(freeSpace(true)));

        int progress = freeSpace(true) * 100 / totalSpace(true);
        progressBar.setProgress(100 - progress);
    }
    public static int totalSpace(boolean external)
    {
        StatFs statFs = getStats(external);
        long total = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / MEGA_BYTE;
        return (int) total;
    }
    public static int freeSpace(boolean external)
    {
        StatFs statFs = getStats(external);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        long freeBytes = availableBlocks * blockSize;

        return (int) (freeBytes / MEGA_BYTE);
    }

    private static StatFs getStats(boolean external){
        String path;

        if (external){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        else{
            path = Environment.getRootDirectory().getAbsolutePath();
        }

        return new StatFs(path);
    }
}
