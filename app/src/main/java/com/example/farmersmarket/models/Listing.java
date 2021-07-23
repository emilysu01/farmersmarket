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
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_PRICE = "price";
    public static final String KEY_UNITS = "units";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COLORS = "colors";
    public static final String KEY_SELL_BY = "sellBy";
    public static final String KEY_DELIVERY = "delivery";

    // Getters and setters
    @Override
    public String getObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    @Override
    public void setObjectId(String objectId) {
        put(KEY_OBJECT_ID, objectId);
    }

    @Override
    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public void setCreatedAt(Date createdAt) {
        put(KEY_CREATED_AT, createdAt);
    }

    public User getAuthor() {
        ParseUser parseUser = getParseUser(KEY_AUTHOR);
        return new User(parseUser);
    }

    public void setAuthor(User author) {
        put(KEY_AUTHOR, author.userToParseUser());
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

    public double[] getCoordinates() {
        try {
            JSONArray rawCoordinates = getJSONArray(KEY_COORDINATES);
            double latitude = rawCoordinates.getDouble(0);
            double longitude = rawCoordinates.getDouble(1);
            return new double[]{latitude, longitude};
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception", e);
            return new double[1];
        }
    }

    public void setCoordinates(double[] coordinates) {
        try {
            JSONArray parseCoordinates = new JSONArray();
            parseCoordinates.put(coordinates[0]);
            parseCoordinates.put(coordinates[1]);
            put(KEY_COORDINATES, parseCoordinates);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
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

    public List<String> getColors() {
        try {
            JSONArray rawColors = getJSONArray(KEY_COLORS);
            List<String> processedColors = new ArrayList<String>();
            for (int i = 0; i < rawColors.length(); i += 1) {
                processedColors.add(rawColors.getString(i));
            }
            return processedColors;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception", e);
            return new ArrayList<String>();
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

    public boolean isDelivery() {
        return getBoolean(KEY_DELIVERY);
    }

    public void setDelivery(boolean delivery) {
        put(KEY_DELIVERY, delivery);
    }
}
