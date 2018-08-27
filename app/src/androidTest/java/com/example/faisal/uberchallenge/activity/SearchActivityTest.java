package com.example.faisal.uberchallenge.activity;

import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.EditText;

import com.example.faisal.uberchallenge.R;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SearchActivityTest {


    @Rule
    public ActivityTestRule<SearchActivity> rule = new ActivityTestRule<>(SearchActivity.class);

    @Test
    @UiThreadTest
    public void testSearchActivity() {
        try {
            SearchActivity activity = rule.getActivity();
            EditText search = ((EditText) activity.findViewById(R.id.searchText));
            search.setText("kittens");
            ((Button) activity.findViewById(R.id.searchButton)).performClick();
            Assert.assertNotNull(search);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
