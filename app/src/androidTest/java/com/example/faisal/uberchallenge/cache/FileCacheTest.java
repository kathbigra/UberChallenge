package com.example.faisal.uberchallenge.cache;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileCacheTest {
    private Context instrumentationCtx= InstrumentationRegistry.getContext();

    @Test
    public void testGetFile() {
        FileCache fileCache = new FileCache(instrumentationCtx);
        String url = "DummyUrl";
        Assert.assertEquals(fileCache.getFile(url).getName(), String.valueOf(url.hashCode()));
    }

    @Test
    public void testClearCache() {
        FileCache fileCache = new FileCache(instrumentationCtx);
        fileCache.clear();
        Assert.assertNull(instrumentationCtx.getCacheDir().list());
    }
}
