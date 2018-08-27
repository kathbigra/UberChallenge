package com.example.faisal.uberchallenge.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.example.faisal.uberchallenge.activity.ResultActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

@SmallTest
public class ImageLoaderTest {
    private Context instrumentationCtx;

   /* @BeforeClass
    public static void setupBeforeClass() {
        Looper.prepare();
    }*/

    @Before
    public void setupBeforeFunction() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void setupAfterFunction() {
        instrumentationCtx = null;
    }

    @Test
    public void testDisplayImage() {
        ImageLoader imageLoader = new ImageLoader(instrumentationCtx);
        ImageView imageView = new ImageView(instrumentationCtx);
        imageLoader.displayImage("DummyUrl", imageView);
        Assert.assertNotNull(imageView.getDrawable());
    }

    @Rule
    public ActivityTestRule<ResultActivity> rule = new ActivityTestRule<>(ResultActivity.class);

    @Test
    public void testBitMapDisplayer() {
        ImageLoader imageLoader = new ImageLoader(instrumentationCtx);
        ImageView img = new ImageView(instrumentationCtx);
        String url = "DummyUrl";
        imageLoader.displayImage(url, img);
        ImageLoader.Photo photo = imageLoader.new Photo(url, img);
        ImageLoader.BitmapDisplayer bd = imageLoader.new BitmapDisplayer(null, photo);
        rule.getActivity().runOnUiThread(bd);
        Assert.assertNotNull(photo.imageView.getDrawable());
    }

    @Test
    public void testClearCache() {
        ImageLoader imageLoader = new ImageLoader(instrumentationCtx);
        imageLoader.clearCache();
        Assert.assertEquals(instrumentationCtx.getCacheDir().list().length, 0);
    }
}
