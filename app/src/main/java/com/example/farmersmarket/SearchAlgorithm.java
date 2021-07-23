package com.example.farmersmarket;

import android.util.Log;

import com.example.farmersmarket.adapters.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchAlgorithm {

    // Tag for logging statements
    public static final String TAG = "SearchAlgorithm";

    // Search results data structure
    private static ArrayList<Listing> searchResults = new ArrayList<Listing>();

    public static void search(String rawSearch, ArrayList<Listing> listings, ShortListingsAdapter adapter) {
        // Regularize input by making everything lowercase
        String processedSearch = rawSearch.toLowerCase();
        String[] splitSearch = processedSearch.split("\\s+");

        // Search
        searchByKeywords(splitSearch, listings, adapter);
    }

    private static void searchByKeywords(String[] searchKeywords, ArrayList<Listing> listings, ShortListingsAdapter adapter) {
        // Set up and make API call to Fruityvice
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                try {
                    // Parse API call results to get all categories
                    JSONArray rawJsonArray = new JSONArray(res);
                    ArrayList<String> allCategories = new ArrayList<String>();
                    for (int i = 0; i < rawJsonArray.length(); i++) {
                        String category = rawJsonArray.getJSONObject(i).getString("name").toLowerCase();
                        allCategories.add(category);
                    }

                    // Find the search category
                    String category = findCategory(searchKeywords, allCategories);
                    // If there are no matching categories, stop the search
                    if (category.isEmpty()) {
                        return;
                    }

                    // Query the database for relevant listings and update UI
                    queryListings(category, listings, adapter);

                } catch (JSONException e) {
                    Log.i(TAG, "Error with parsing Fruityvice data", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "Error with retrieving Fruityvice data", t);
            }
        });
    }

    private static String findCategory(String[] searchKeywords, List<String> allCategories) {
        // Determine the category of the listing
        for (String keyword : searchKeywords) {
            if (allCategories.contains(keyword)) {
                return keyword;
            }
        }
        return "";
    }

    private static void queryListings(String category, ArrayList<Listing> allListings, ShortListingsAdapter adapter) {
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_AUTHOR);
        query.whereEqualTo(Listing.KEY_CATEGORY, category);
        // TODO: Implement optimization techniques
        query.setLimit(100);
        ArrayList<Listing> searchResults = new ArrayList<Listing>();
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with retrieving listings from Parse", e);
                    return;
                } else {
                    Log.i("Posts", listings.toString());
                    searchResults.addAll(listings);

                    allListings.addAll(listings);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
