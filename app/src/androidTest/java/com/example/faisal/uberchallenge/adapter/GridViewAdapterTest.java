package com.example.faisal.uberchallenge.adapter;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.faisal.uberchallenge.R;
import com.example.faisal.uberchallenge.activity.ResultActivity;
import com.example.faisal.uberchallenge.activity.SearchActivity;
import com.example.faisal.uberchallenge.adapters.GridViewAdapter;
import com.example.faisal.uberchallenge.components.GridItem;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GridViewAdapterTest {

    private Context instrumentationCtx;

    GridViewAdapter gridViewAdapter;

   /* @BeforeClass
    public static void setupBeforeClass() {
        Looper.prepare();


    }*/

    @Before
    public void setupBeforeFunction() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        ArrayList<GridItem> gridData = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(
                instrumentationCtx, R.layout.grid_item_layout, gridData);
    }

    @After
    public void setupAfterFunction() {
        instrumentationCtx = null;
        gridViewAdapter = null;
    }
    @Test
    public void testmGridData() {
        Assert.assertEquals(gridViewAdapter.getCount(), 0);
    }

    @Test
    public void testAddTomGridData() {
        gridViewAdapter.getmGridData().clear();
        gridViewAdapter.addToGridData(new GridItem());
        Assert.assertEquals(gridViewAdapter.getCount(), 1);
    }

}