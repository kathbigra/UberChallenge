package com.example.faisal.uberchallenge.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        //Find the dir to save cached images. Try for storage card, if not get the app cache dir.
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "UberChallenge");
        boolean created = false;
        if (!cacheDir.exists())
            created = cacheDir.mkdirs();
        if (!created)
            cacheDir = context.getCacheDir();
    }

    //Get the file based on URL.
    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }
}
