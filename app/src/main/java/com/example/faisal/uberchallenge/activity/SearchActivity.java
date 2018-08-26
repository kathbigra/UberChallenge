package com.example.faisal.uberchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.faisal.uberchallenge.R;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        //Start the new activity which will fetch search results and display them. The search String is passed asthe parameter for the new activity
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(SearchActivity.this, ResultActivity.class);
                searchIntent.putExtra("searchString", ((EditText) findViewById(R.id.searchText)).getText().toString());
                SearchActivity.this.startActivity(searchIntent);
            }
        });
    }
}
