package com.example.faisal.uberchallenge.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {

    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));//Last argument true for LRU ordering
    private long size = 0;//current allocated size
    private long limit = 1000000;//max memory in bytes. ~1MB

    public MemoryCache() {
        //use 1/4th of maximum memory
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(MemoryCache.class.toString(), "MemoryCache will use up to " + limit / 1024 / 1024 + "MB");
    }

    //Get bitmap from in memory cache.
    public Bitmap get(String url) {
        if (!cache.containsKey(url))
            return null;
        return cache.get(url);

    }

    //Put a new image to in memory cache
    public void put(String url, Bitmap bitmap) {
        try {
            //If key already exists, remove detail from total size used.
            if (cache.containsKey(url))
                size -= getSizeInBytes(cache.get(url));
            cache.put(url, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(MemoryCache.class.toString(), "cache size=" + size + " for " + cache.size() + " assets ");
        if (size > limit) {
            //Clean things up if size is exceeding limits.
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.i(MemoryCache.class.toString(), "Clean cache. New size " + cache.size());
        }
    }

    public void clear() {
        cache.clear();
        size = 0;
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
