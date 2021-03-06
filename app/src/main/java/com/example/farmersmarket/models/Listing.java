package com.example.farmersmarket.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Listing")
public class Listing extends ParseObject {

    // Tag for logging statements
    public static final String TAG = "Listing";

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PRICE = "price";
    public static final String KEY_UNITS = "units";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COLORS = "colors";
    public static final String KEY_SELL_BY = "sellBy";
    public static final String KEY_DELIVERY = "delivery";
    public static final String KEY_ZIP = "zip";

    // Getters and setters

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public List<ParseFile> getImages() {
        return getList(KEY_IMAGES);
    }

    public void setImages(List<ParseFile> images) {
        put(KEY_IMAGES, images);
    }

    public double getLatitude() {
        return getDouble(KEY_LATITUDE);
    }

    public void setLatitude(double latitude) {
        put(KEY_LATITUDE, latitude);
    }

    public double getLongitude() {
        return getDouble(KEY_LONGITUDE);
    }

    public void setLongitude(double longitude) {
        put(KEY_LONGITUDE, longitude);
    }

    public int getPrice() {
        return getInt(KEY_PRICE);
    }

    public void setPrice(int price) {
        put(KEY_PRICE, price);
    }

    public int getUnits() {
        return getInt(KEY_UNITS);
    }

    public void setUnits(int units) {
        put(KEY_UNITS, units);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    // TODO CHECK
    public List<String> getColors() {
        try {
            JSONArray jsonColors = getJSONArray(KEY_COLORS);
            List<String> processedColors = new ArrayList<String>();
            for (int i = 0; i < jsonColors.length(); i += 1) {
                processedColors.add(jsonColors.getString(i));
            }
            return processedColors;
        } catch (JSONException e) {
            Log.e(TAG, "JSON error with colors", e);
            return null;
        }
    }

    public void setColors(List<String> colors) {
        JSONArray parseColors = new JSONArray();
        for (String color : colors) {
            parseColors.put(color);
        }
        put(KEY_COLORS, parseColors);
    }

    public Date getSellBy() {
        return getDate(KEY_SELL_BY);
    }

    public void setSellBy(Date sellBy) {
        put(KEY_SELL_BY, sellBy);
    }

    public boolean getDelivery() {
        return getBoolean(KEY_DELIVERY);
    }

    public void setDelivery(boolean delivery) {
        put(KEY_DELIVERY, delivery);
    }
}
