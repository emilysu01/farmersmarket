package com.example.farmersmarket;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FruityviceClient {

    public static List<String> getAllCategories() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONArray rawJsonArray = new JSONArray(res);
                            List<String> processedJsonArray = new ArrayList<String>();
                            for (int i = 0; i < rawJsonArray.length(); i++) {
                                String category = rawJsonArray.getJSONObject(i).getString("name").toLowerCase();
                                processedJsonArray.add(category);
                                Log.i("JSON Objects", category.getClass().toString());
                            }

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

        return new ArrayList<String>();
    }

}
