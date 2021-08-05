package com.example.farmersmarket;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchAlgorithm {

    // Tag for logging statements
    public static final String TAG = "SearchAlgorithm";

    // Search results data structure
    private static ArrayList<Listing> searchResults = new ArrayList<Listing>();

    public static void search(String rawSearch, ArrayList<Listing> listings, ShortListingsAdapter adapter, Spinner spSort, Spinner spFilter) {
        // Regularize input by making everything lowercase
        String processedSearch = rawSearch.toLowerCase();
        String[] splitSearch = processedSearch.split("\\s+");

        // Search
        searchByKeywords(splitSearch, listings, adapter, spSort, spFilter);
    }

    private static void searchByKeywords(String[] searchKeywords, ArrayList<Listing> listings, ShortListingsAdapter adapter, Spinner spSort, Spinner spFilter) {
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
                    queryListings(category, listings, adapter, spSort, spFilter);

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

    private static void queryListings(String category, ArrayList<Listing> allListings, ShortListingsAdapter adapter, Spinner spSort, Spinner spFilter) {
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_AUTHOR);
        query.whereEqualTo(Listing.KEY_CATEGORY, category);
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
                    // Log.i("SEARCH RESULTS HERE", searchResults.toString());

                    allListings.addAll(listings);
                    adapter.notifyDataSetChanged();

                    spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SearchAlgorithm.sorting(searchResults, parent.getItemAtPosition(position).toString(), allListings, adapter);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SearchAlgorithm.filtering(searchResults, parent.getItemAtPosition(position).toString(), allListings, adapter);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });
    }

    public static void sorting(ArrayList<Listing> searchResults, String criteria, ArrayList<Listing> allListings, ShortListingsAdapter adapter) {
        HashMap<Listing, Integer> mappingToCriteria = new HashMap<Listing, Integer>();
        for (Listing listing : searchResults) {
            mappingToCriteria.put(listing, listing.getInt(criteria));
        }

        List<Map.Entry<Listing, Integer>> orderedCriteria = new ArrayList<Map.Entry<Listing, Integer>>();
        orderedCriteria.addAll(mappingToCriteria.entrySet());

        Collections.sort(orderedCriteria, new Comparator<Map.Entry<Listing, Integer> >() {
            @Override
            public int compare(Map.Entry<Listing, Integer> o1, Map.Entry<Listing, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<Listing, Integer> sortedCriteria = new LinkedHashMap<Listing, Integer>();
        for (Map.Entry<Listing, Integer> entry : orderedCriteria) {
            sortedCriteria.put(entry.getKey(), entry.getValue());
        }

        allListings.clear();
        allListings.addAll(sortedCriteria.keySet());
        adapter.notifyDataSetChanged();

    }

    public static void filtering(ArrayList<Listing> searchResults, String criteria, ArrayList<Listing> allListings, ShortListingsAdapter adapter) {
        Log.i("CRITERIA", criteria);
        ArrayList<Listing> updatedSearchResults = new ArrayList<Listing>();
        for (Listing listing : searchResults) {
            if (listing.getBoolean(criteria)) {
                updatedSearchResults.add(listing);
            }
        }
        Log.i("UPDATED SEARCH RESULTS", updatedSearchResults.toString());
        allListings.clear();
        allListings.addAll(updatedSearchResults);
        adapter.notifyDataSetChanged();
    }
}
