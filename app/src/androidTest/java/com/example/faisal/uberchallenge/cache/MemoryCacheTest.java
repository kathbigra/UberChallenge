package com.example.faisal.uberchallenge.cache;

import android.graphics.Bitmap;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)

@SmallTest


public class MemoryCacheTest {


    Bitmap bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);

    @Test
    public void testGetAndPutBitmapImage() {

        MemoryCache memoryCache = new MemoryCache();
        memoryCache.put("DummyUrl", bmp);
        Assert.assertEquals(bmp, memoryCache.get("DummyUrl"));
    }

    @Test
    public void testGetBitmapImageIfCacheNotThere() {
        MemoryCache memoryCache = new MemoryCache();
        Assert.assertNull(memoryCache.get("DummyUrl"));
    }

    @Test
    public void testCheckSizeForSizeGreaterThanLimit() {

        MemoryCache memoryCache = new MemoryCache();
        memoryCache.setLimit(400);
        memoryCache.put("DummyUrl1", bmp);
        memoryCache.put("DummyUrl2", bmp);
        memoryCache.put("DummyUrl3", bmp);
        memoryCache.put("DummyUrl4", bmp);
        memoryCache.put("DummyUrl5", bmp);
        Assert.assertNull(memoryCache.get("DummyUrl1"));
    }
}
