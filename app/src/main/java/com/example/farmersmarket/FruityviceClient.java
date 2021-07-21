package com.example.farmersmarket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

}
