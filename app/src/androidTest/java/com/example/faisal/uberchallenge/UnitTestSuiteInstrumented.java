package com.example.faisal.uberchallenge;

import com.example.faisal.uberchallenge.activity.ResultActivityTest;
import com.example.faisal.uberchallenge.activity.SearchActivityTest;
import com.example.faisal.uberchallenge.adapter.GridViewAdapterTest;
import com.example.faisal.uberchallenge.cache.FileCacheTest;
import com.example.faisal.uberchallenge.cache.MemoryCache;
import com.example.faisal.uberchallenge.cache.MemoryCacheTest;
import com.example.faisal.uberchallenge.components.ImageLoader;
import com.example.faisal.uberchallenge.components.ImageLoaderTest;
import com.example.faisal.uberchallenge.utilities.NetworkUtility;
import com.example.faisal.uberchallenge.utility.FileUtilityTest;
import com.example.faisal.uberchallenge.utility.NetworkUtilityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileCacheTest.class,
        GridViewAdapterTest.class,
        ImageLoaderTest.class,
        MemoryCacheTest.class,
        ResultActivityTest.class,
        SearchActivityTest.class,
        FileUtilityTest.class,
        NetworkUtilityTest.class
})


public class UnitTestSuiteInstrumented {
}
