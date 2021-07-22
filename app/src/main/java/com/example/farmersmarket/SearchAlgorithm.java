package com.example.farmersmarket;

import android.util.Log;

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

    public static final String TAG = "SearchAlgorithm";

    public static void searchByKeyword(String rawSearch) {
        // Regularize input by making everything lowercase
        String processedSearch = rawSearch.toLowerCase();
        String[] splitSearch = processedSearch.split("\\s+");

        // Determine which category the search is in
        // List<String> allCategories =
        getCategory(splitSearch);
    }

    private static ArrayList<Listing> queryListings(String category) {
        // Initialize query
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);

        // Get the full details of the user who made the post
        query.include(Listing.KEY_AUTHOR);
        query.whereEqualTo(Listing.KEY_CATEGORY, category);
        // query.addDescendingOrder(Listing.KEY_CREATED_AT);
        // query.addDescendingOrder(Listing.KEY_CREATED_AT);

        // TODO: Implement optimization techniques
        // Limit the number of posts the user sees to 100
        query.setLimit(100);

        ArrayList<Listing> searchResults = new ArrayList<Listing>();
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                } else {
                    searchResults.addAll(listings);
                }
            }
        });
        return searchResults;
    }

    private static String checkCategory(String[] searchKeywords, List<String> categories) {
        Log.i("Check Category", "here");
        for (String keyword : searchKeywords) {
            Log.i("Keyword: " + keyword, String.valueOf(categories.contains(keyword)));
            for (String category : categories) {
                if (keyword.equals(category)) {
                    return category;
                }
            }
        }
        return "";
    }

    private static void getCategory(String[] searchKeywords) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final String[] returnedCategory = new String[1];
        client.get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONArray rawJsonArray = new JSONArray(res);
                            List<String> allCategories = new ArrayList<String>();
                            for (int i = 0; i < rawJsonArray.length(); i++) {
                                String category = rawJsonArray.getJSONObject(i).getString("name").toLowerCase();
                                allCategories.add(category);
                            }

                            for (String keyword : searchKeywords) {
                                Log.i("Contains? ", String.valueOf(allCategories.contains(keyword)));
                                if (allCategories.contains(keyword)) {
                                    returnedCategory[0] = keyword;
                                    Log.i("Keyword inside", returnedCategory[0]);
                                    break;
                                }
                            }

                            if (returnedCategory[0].isEmpty()) {
                                return;
                            }

                            // Query the database for relevant listings
                            // Initialize query
                            ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);

                            // Get the full details of the user who made the post
                            query.include(Listing.KEY_AUTHOR);
                            query.whereEqualTo(Listing.KEY_CATEGORY, returnedCategory[0]);

                            Log.i("category", returnedCategory[0]);
                            // query.addDescendingOrder(Listing.KEY_CREATED_AT);
                            // query.addDescendingOrder(Listing.KEY_CREATED_AT);

                            // TODO: Implement optimization techniques
                            // Limit the number of posts the user sees to 100
                            query.setLimit(100);

                            ArrayList<Listing> searchResults = new ArrayList<Listing>();
                            query.findInBackground(new FindCallback<Listing>() {
                                @Override
                                public void done(List<Listing> listings, ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Issue with getting posts", e);
                                        return;
                                    } else {
                                        Log.i("results? ", listings.toString());
                                        searchResults.addAll(listings);
                                        Log.i("search results? ", searchResults.toString());
                                    }
                                }
                            });


                            // Log.i("MainActivity", jsonObj.getString("ID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        // Log.i("MainActivity", "Failure");
                        // Log.i("MainActivity", "Error: " + t.toString());
                    }
                }
        );

        // Log.i("Keyword outside", returnedCategory[0]);
        // return returnedCategory[0];
    }
}
