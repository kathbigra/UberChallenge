package com.example.faisal.uberchallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;


import com.example.faisal.uberchallenge.adapters.GridViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    public static String FLICKR_IMAGE_STATIC_URL = "http://farm[0].staticflickr.com/[1]/[2]_[3].jpg";
    public static String FLICKR_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&\n" +
            "format=json&nojsoncallback=1&safe_search=1&text=[0]&page=[1]";

    //private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;

    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;

    private ArrayList<GridItem> mGridData;
    int pageNumber = 1;
    private String searchString = "kittens";
    private boolean isLoading = false;
    private boolean userScrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchString = extras.getString("searchString");
        }
        mGridView = (GridView) findViewById(R.id.resultView);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isLoading && userScrolled && firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                    pageNumber++;
                    new FetchResultTask().execute();
                }
            }
        });
        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view == mGridView && motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    userScrolled = true;
                }
                return false;
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
        new FetchResultTask().execute();

    }

    private String getSearchUrl() {
        String url = FLICKR_URL;
        String url2 = url
                .replace("[0]", searchString)
                .replace("[1]", Integer.toString(pageNumber));
        return url2;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class FetchResultTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            JSONObject response = new JSONObject();
            try {
                response = getJSONObjectFromURL(); // calls method to get JSON object

                JSONObject resultObject = new JSONObject(response.toString());
                JSONArray photos = resultObject.getJSONObject("photos").getJSONArray("photo");
                populateImages(photos);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private JSONObject getJSONObjectFromURL() throws IOException, JSONException {
            HttpURLConnection urlConnection = null;
            URL url = new URL(getSearchUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            char[] buffer = new char[1024];
            String jsonString = new String();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonString = sb.toString();
            System.out.println("JSON: " + jsonString);
            urlConnection.disconnect();
            return new JSONObject(jsonString);
        }

        @Override
        protected void onPostExecute(String result) {
            mGridAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            isLoading = false;

        }

        @Override
        protected void onPreExecute() {
            isLoading = true;
            mProgressBar.setVisibility(View.VISIBLE);
        }

        private void populateImages(final JSONArray photos) throws JSONException, IOException {
            GridItem item;
            for (int i = 0; i < photos.length(); i++) {
                item = new GridItem();
                item.setImage(getUrl(photos.getJSONObject(i)));
                Bitmap bitMap = null;
                InputStream in = new java.net.URL(item.getImage()).openStream();
                bitMap = BitmapFactory.decodeStream(in);
                item.setBitMap(bitMap);
                mGridData.add(item);

            }
        }

        private String getUrl(JSONObject photo) throws JSONException {
            String url = FLICKR_IMAGE_STATIC_URL;
            String url2 = url
                    .replace("[0]", photo.getString("farm"))
                    .replace("[1]", photo.getString("server"))
                    .replace("[2]", photo.getString("id"))
                    .replace("[3]", photo.getString("secret"));
            return url2;
        }

    }

}
