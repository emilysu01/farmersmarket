package com.example.farmersmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    RecyclerView rvListings;

    List<Listing> allListings = new ArrayList<>();
    ListingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve UI components
        rvListings = findViewById(R.id.rvListings);

        adapter = new ListingsAdapter(this, allListings);
        rvListings.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListings.setLayoutManager(linearLayoutManager);

        queryListings();

    }

    private void queryListings() {
        // Specify that we want to query Listing.class data
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        // Include data referred by user key
        query.include(Listing.KEY_AUTHOR);
        // Order posts by newest first
        query.addDescendingOrder("createdAt");
        // Start an asynchronous call for listings
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                // Check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                // for (Listing listing : listings) {
                //    Log.i(TAG, "Listing author: " + listing.getKeyAuthor().getUsername() + " Listing description: " + listing.getKeyDescription());
                // }

                // Save received listings to data structure and notify adapter of new data
                allListings.addAll(listings);
                adapter.notifyDataSetChanged();
            }
        });
    }
}