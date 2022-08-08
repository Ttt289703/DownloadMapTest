package com.example.downloadmapprototype.model.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MemoryService {
    private static final long MEGA_BYTE = 1048576;

    public static int getUsedMemoryPercent() {
        long totalMemory = getTotalMemory();
        long availableMemory = getAvailableMemory();

        int progress = (int) (availableMemory * 100 / totalMemory);
        return (100 - progress);
    }

    public static long getAvailableMemory() {
        StatFs statFs = getStats(true);
        long availableBlocks = statFs.getAvailableBlocksLong();
        long blockSize = statFs.getBlockSizeLong();
        long freeBytes = availableBlocks * blockSize;

        return (freeBytes / MEGA_BYTE);
    }

    public static long getTotalMemory() {
        StatFs statFs = getStats(true);
        return ((statFs.getBlockCountLong()) * (statFs.getBlockSizeLong())) / MEGA_BYTE;
    }

    public static long getBlockSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        return stat.getBlockSizeLong();
    }

    public static long getAvailableBlockSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        return stat.getAvailableBlocksLong();
    }

    private static StatFs getStats(boolean external) {
        String path;

        if (external) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = Environment.getRootDirectory().getAbsolutePath();
        }

        return new StatFs(path);
    }
}
