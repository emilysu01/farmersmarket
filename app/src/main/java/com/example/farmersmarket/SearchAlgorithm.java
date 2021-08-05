package com.example.farmersmarket;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farmersmarket.adapters.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchAlgorithm {

    // Tag for logging statements
    public static final String TAG = "SearchAlgorithm";

    private static Spinner thisSpSort;
    private static Spinner thisSpFilter;

    public static void search(String rawSearch, ArrayList<Listing> listings, ShortListingsAdapter adapter, Spinner spSort, Spinner spFilter) {
        // Set UI components
        thisSpSort = spSort;
        thisSpFilter = spFilter;

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
                        Toast.makeText(thisSpFilter.getContext(), "No search results. Please try another search", Toast.LENGTH_SHORT).show();
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
        query.setLimit(100);
        ArrayList<Listing> searchResults = new ArrayList<Listing>();
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with retrieving listings from Parse", e);
                    return;
                } else {
                    // Find distance for each listing
                    HashMap<Listing, Double> listingDistance = new HashMap<Listing, Double>();
                    LatLng currentUserLocation = new LatLng(ParseUser.getCurrentUser().getInt(User.KEY_LATITUDE), ParseUser.getCurrentUser().getInt(User.KEY_LONGITUDE));
                    for (Listing listing : listings) {
                        LatLng listingLocation = new LatLng(listing.getLatitude(), listing.getLongitude());
                        double distance = SphericalUtil.computeDistanceBetween(currentUserLocation, listingLocation);
                        listingDistance.put(listing, distance);
                    }

                    // Sort by distance
                    List<Map.Entry<Listing, Double>> orderedCriteria = new ArrayList<Map.Entry<Listing, Double>>();
                    orderedCriteria.addAll(listingDistance.entrySet());
                    Collections.sort(orderedCriteria, new Comparator<Map.Entry<Listing, Double> >() {
                        @Override
                        public int compare(Map.Entry<Listing, Double> o1, Map.Entry<Listing, Double> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

                    // Make a new mapping of listings to criteria in order of distance
                    HashMap<Listing, Double> sortedListings = new LinkedHashMap<Listing, Double>();
                    for (Map.Entry<Listing, Double> entry : orderedCriteria) {
                        sortedListings.put(entry.getKey(), entry.getValue());
                    }

                    // Display search results on UI
                    searchResults.addAll(sortedListings.keySet());
                    allListings.addAll(listings);
                    adapter.notifyDataSetChanged();

                    // Sorting and filtering spinners are only active after a search has been made
                    thisSpSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SearchAlgorithm.sorting(searchResults, parent.getItemAtPosition(position).toString(), allListings, adapter);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    thisSpFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        criteria = criteria.toLowerCase();

        // Make a new mapping of listings to criteria
        HashMap<Listing, Integer> mappingToCriteria = new HashMap<Listing, Integer>();
        for (Listing listing : searchResults) {
            mappingToCriteria.put(listing, listing.getInt(criteria));
        }

        // Sort the criteria score
        List<Map.Entry<Listing, Integer>> orderedCriteria = new ArrayList<Map.Entry<Listing, Integer>>();
        orderedCriteria.addAll(mappingToCriteria.entrySet());
        Collections.sort(orderedCriteria, new Comparator<Map.Entry<Listing, Integer> >() {
            @Override
            public int compare(Map.Entry<Listing, Integer> o1, Map.Entry<Listing, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Make a new mapping of listings to criteria in order
        HashMap<Listing, Integer> sortedCriteria = new LinkedHashMap<Listing, Integer>();
        for (Map.Entry<Listing, Integer> entry : orderedCriteria) {
            sortedCriteria.put(entry.getKey(), entry.getValue());
        }

        // Update UI with sorted listings
        allListings.clear();
        allListings.addAll(sortedCriteria.keySet());
        adapter.notifyDataSetChanged();
    }

    public static void filtering(ArrayList<Listing> searchResults, String criteria, ArrayList<Listing> allListings, ShortListingsAdapter adapter) {
        ArrayList<Listing> filteredResults;
        if (criteria.equals("Delivery available")) {
            filteredResults = filterDelivery(searchResults);
        } else if (criteria.equals("Under $5")) {
            filteredResults = filterPrice(searchResults, 5);
        } else if (criteria.equals("Under $10")) {
            filteredResults = filterPrice(searchResults, 10);
        } else {
            filteredResults = filterPrice(searchResults, 15);
        }

        // Update UI with filtered listings
        allListings.clear();
        allListings.addAll(filteredResults);
        adapter.notifyDataSetChanged();
    }

    private static ArrayList<Listing> filterDelivery(ArrayList<Listing> searchResults) {
        ArrayList<Listing> updatedSearchResults = new ArrayList<Listing>();
        for (Listing listing : searchResults) {
            if (listing.getBoolean(Listing.KEY_DELIVERY)) {
                updatedSearchResults.add(listing);
            }
        }
        return updatedSearchResults;
    }

    private static ArrayList<Listing> filterPrice(ArrayList<Listing> searchResults, int price) {
        ArrayList<Listing> updatedSearchResults = new ArrayList<Listing>();
        for (Listing listing : searchResults) {
            if (listing.getInt(Listing.KEY_PRICE) <= price) {
                updatedSearchResults.add(listing);
            }
        }
        return updatedSearchResults;
    }
}
