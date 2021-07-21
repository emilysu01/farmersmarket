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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchAlgorithm {

    public static final String TAG = "SearchAlgorithm";

    public ArrayList<Listing> searchByKeyword(String rawSearch) {
        // Regularize input by making everything lowercase
        String processedSearch = rawSearch.toLowerCase();
        String[] splitSearch = processedSearch.split("\\s+");

        List<String> allCategories = getAllCategories();
        String category = checkCategory(splitSearch, allCategories);
        if (category.isEmpty()) {
            return new ArrayList<Listing>();
        }

        queryListings(category);





        return new ArrayList<Listing>();
    }

    private void queryListings(String category) {
        // Initialize query
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);

        // Get the full details of the user who made the post
        query.include(Listing.KEY_CATEGORY);
        query.addDescendingOrder(Listing.KEY_CREATED_AT);
        query.whereEqualTo(Listing.KEY_CATEGORY, category);
        // query.addDescendingOrder(Listing.KEY_CREATED_AT);
        // Limit the number of posts the user sees to 20
        query.setLimit(100);

        // getInBackground is used to retrieve a single item from the backend
        // findInBackground is used to retrieve all items from the backend
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // Add all retrieved posts to data model and notify adapter
                // allPosts.addAll(posts);
                // adapter.notifyDataSetChanged();
            }
        });
    }

    public List<String> getAllCategories() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        try {
                            Log.i("MainActivity", "Success");
                            JSONArray rawJsonArray = new JSONArray(res);
                            List<JSONObject> processedJsonArray = new ArrayList<JSONObject>();
                            for (int i = 0; i < rawJsonArray.length(); i++) {
                                JSONObject jsonObj = rawJsonArray.getJSONObject(i);
                                processedJsonArray.add(jsonObj);
                                Log.i("JSON Objects", jsonObj.getString("name"));
                            }

                            // Log.i("MainActivity", jsonObj.getString("ID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("MainActivity", "Failure");
                        Log.i("MainActivity", "Error: " + t.toString());
                    }
                }
        );

        return new ArrayList<String>();
    }

    public String checkCategory(String[] searchKeywords, List<String> categories) {
        for (String keyword : searchKeywords) {
            if (categories.contains(keyword)) {
                return keyword;
            }
        }
        return "";
    }
}
