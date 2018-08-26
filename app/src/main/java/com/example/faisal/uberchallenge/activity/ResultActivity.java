package com.example.faisal.uberchallenge.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faisal.uberchallenge.R;
import com.example.faisal.uberchallenge.adapters.GridViewAdapter;
import com.example.faisal.uberchallenge.components.GridItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.faisal.uberchallenge.utilities.NetworkUtility.getJsonString;
import static com.example.faisal.uberchallenge.utilities.NetworkUtility.getUrlConnection;

public class ResultActivity extends AppCompatActivity {

    //Holder url for fetching individual images.
    public static String FLICKR_IMAGE_STATIC_URL = "http://farm[0].staticflickr.com/[1]/[2]_[3].jpg";
    //Query URL to fetch results from flickr api. With holders for query string and page number.
    public static String FLICKR_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&\n" +
            "format=json&nojsoncallback=1&safe_search=1&text=[0]&page=[1]";

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private int pageNumber = 1;
    private String searchString = "";
    private boolean isLoading = false;
    private boolean userScrolled = false;

    @Override
    public void onBackPressed() {
        //Clear the cache before executing a new search.
        super.onBackPressed();
        mGridAdapter.imageLoader.clearCache();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ResultActivity.class.getSimpleName(), "Starting result activity");
        setContentView(R.layout.result_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchString = extras.getString("searchString");
        }
        mGridView = (GridView) findViewById(R.id.resultView);
        addListenersToGridView();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, new ArrayList<GridItem>());
        mGridView.setAdapter(mGridAdapter);
        //Clear cache at the start of this activity, before executing new search.
        mGridAdapter.imageLoader.clearCache();
        Log.i(ResultActivity.class.getSimpleName(), "Run search query");
        new FetchResultTask().execute();
    }

    /**
     * Adding various listeners on the grid view component
     */
    private void addListenersToGridView() {
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Do Nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isLoading && userScrolled && firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0 && pageNumber != -1) {
                    Log.i(ResultActivity.class.getSimpleName(), "Reached end of page. Fetching new page data.");
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
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ResultActivity.class.getSimpleName(), "Image clicked at postion: " + position);
                Toast toast = Toast.makeText(getApplicationContext(), mGridAdapter.getmGridData().get(position).getTitle(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private class FetchResultTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            JSONObject response = new JSONObject();
            try {
                //Get result from query string
                response = getJSONObjectFromURL();
                Log.i(ResultActivity.class.getSimpleName(), "Query successful for page number: " + pageNumber);
            } catch (IOException e) {
                Log.e(ResultActivity.class.getSimpleName(), e.getMessage());
            } catch (JSONException e) {
                Log.e(ResultActivity.class.getSimpleName(), e.getMessage());
            }
            return response.toString();
        }

        /**
         * Method to execute actual search query from Flickr
         *
         * @return result
         * @throws IOException
         * @throws JSONException
         */
        private JSONObject getJSONObjectFromURL() throws IOException, JSONException {
            URL url = new URL(getSearchUrl());
            HttpURLConnection urlConnection = getUrlConnection(url);
            urlConnection.connect();
            String jsonString = getJsonString(url);
            System.out.println("JSON: " + jsonString);
            urlConnection.disconnect();
            return new JSONObject(jsonString);
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                Log.i(ResultActivity.class.getSimpleName(), "Inside post execute.");
                JSONObject resultObject = null;
                resultObject = new JSONObject(result);
                JSONArray photosArray = resultObject.getJSONObject("photos").getJSONArray("photo");
                //Stop if no more results are coming or if the query results in 0 match.
                if (photosArray.length() == 0) {
                    pageNumber = -1;
                    return;
                }
                //Get all the images details and process then to create Image views and notify the adapter
                populateImages(photosArray);
                mGridAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                isLoading = false;
            } catch (JSONException e) {
                Log.e(ResultActivity.class.getSimpleName(), e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            Log.i(ResultActivity.class.getSimpleName(), "Inside pre execute.");
            isLoading = true;
            mProgressBar.setVisibility(View.VISIBLE);
        }

        private void populateImages(final JSONArray photos) throws JSONException {
            GridItem item;
            Log.i(ResultActivity.class.getSimpleName(), "Populating grid item for every new Image.");
            for (int i = 0; i < photos.length(); i++) {
                item = new GridItem();
                item.setUrl(getUrl(photos.getJSONObject(i)));
                item.setTitle(photos.getJSONObject(i).getString("title"));
                mGridAdapter.addToGridData(item);
            }
            Log.i(ResultActivity.class.getSimpleName(), "Populated details for: " + photos.length() + " assets.");
        }

        /**
         * Get the individual image url
         *
         * @return Final URL
         */
        private String getUrl(JSONObject photo) throws JSONException {
            String holderUrl = FLICKR_IMAGE_STATIC_URL;
            String downloadUrl = holderUrl
                    .replace("[0]", photo.getString("farm"))
                    .replace("[1]", photo.getString("server"))
                    .replace("[2]", photo.getString("id"))
                    .replace("[3]", photo.getString("secret"));
            return downloadUrl;
        }

        /**
         * Get the search url by replacing the holders for query string and page number to be fetched
         *
         * @return Final URL
         */
        private String getSearchUrl() {
            String holderURL = FLICKR_URL;
            String queryURL = holderURL
                    .replace("[0]", searchString)
                    .replace("[1]", Integer.toString(pageNumber));
            return queryURL;
        }
    }
}
