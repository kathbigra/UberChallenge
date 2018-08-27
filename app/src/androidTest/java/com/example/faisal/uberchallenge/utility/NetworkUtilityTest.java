package com.example.faisal.uberchallenge.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.faisal.uberchallenge.utilities.NetworkUtility;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

@RunWith(AndroidJUnit4.class)
public class NetworkUtilityTest {
    private Context instrumentationCtx;

   /* @BeforeClass
    public static void setupBeforeClass() {
        Looper.prepare();
    }*/

    @Before
    public void setupBeforeFunction() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testDownloadImage() {
        try {
            File file = new File(instrumentationCtx.getCacheDir(),"tempFile.jpg");
            if (file.exists()) file.delete();
            URL url = new URL("http://farm1.static.flickr.com/578/23451156376_8983a8ebc7.jpg");
            Bitmap bmp = NetworkUtility.downloadImage(url, file);
            Assert.assertTrue(file.exists());
            Assert.assertNotNull(bmp);
            file.delete();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
