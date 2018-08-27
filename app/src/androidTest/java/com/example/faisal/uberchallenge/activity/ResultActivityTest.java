package com.example.faisal.uberchallenge.activity;


import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.GridView;

import com.example.faisal.uberchallenge.R;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResultActivityTest {
    private Context instrumentationCtx;

    /*@BeforeClass
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

    @Rule
    public ActivityTestRule<ResultActivity> rule = new ActivityTestRule<>(ResultActivity.class);

    @Test
    public void testOnCreate() {
        ResultActivity activity = rule.getActivity();
        View viewById = activity.findViewById(R.id.resultView);
        Assert.assertNotNull(viewById);
        Assert.assertTrue(viewById instanceof GridView);
    }

    @Test
    public void testGridViewListener() {
        ResultActivity activity = rule.getActivity();
        View viewById = activity.findViewById(R.id.resultView);
        ((GridView) viewById).performClick();
        ((GridView) viewById).scrollTo(100, 100);
    }
}
