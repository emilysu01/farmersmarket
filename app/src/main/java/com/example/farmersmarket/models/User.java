package com.example.farmersmarket.models;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class User {

    // Tag for logging statements
    public static final String TAG = "User";

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_ZIP = "zip";

    // ParseUser field
    // There were issues with creating a class that directly extends ParseUser
    private ParseUser parseUser;

    // Constructor given a ParseUser
    public User(ParseUser parseUser) {
        this.parseUser = parseUser;
    }

    // Constructor given user information
    public User(String username, String password, String email, String name, double[] coordinates, String zip) {
        try {
            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.put(KEY_NAME, name);
            JSONArray jsonCoordinates = new JSONArray();
            jsonCoordinates.put(coordinates[0]);
            jsonCoordinates.put(coordinates[1]);
            newUser.put(KEY_COORDINATES, jsonCoordinates);
            newUser.put(KEY_ZIP, zip);
            this.parseUser = newUser;
        } catch (JSONException e) {
            Log.e(TAG, "JSON error with co-ordinates", e);
        }
    }

    // Retrieve ParseUser
    public ParseUser userToParseUser() {
        return parseUser;
    }

    // Getters and setters
    public String getUserId() {
        return parseUser.getString(KEY_OBJECT_ID);
    }

    public void setUserId(String userId) {
        parseUser.put(KEY_OBJECT_ID, userId);
    }

    public Date getCreatedAt() {
        return parseUser.getDate(KEY_CREATED_AT);
    }

    public void setCreatedAt(Date createdAt) {
        parseUser.put(KEY_CREATED_AT, createdAt);
    }

    public String getUsername() {
        return parseUser.getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        parseUser.put(KEY_USERNAME, username);
    }

    public String getPassword() {
        return parseUser.getString(KEY_PASSWORD);
    }

    public void setPassword(String password) {
        parseUser.put(KEY_PASSWORD, password);
    }

    public String getEmail() {
        return parseUser.getString(KEY_EMAIL);
    }

    public void setEmail(String email) {
        parseUser.put(KEY_EMAIL, email);
    }

    public String getName() {
        // return parseUser.getString(KEY_NAME);
        try {
            return parseUser.fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setName(String name) {
        parseUser.put(KEY_NAME, name);
    }

    public ParseFile getProfilePic() {
        return parseUser.getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile profilePic) {
        parseUser.put(KEY_PROFILE_PIC, profilePic);
    }

    public double[] getCoordinates() {
        try {
            JSONArray jsonCoordinates = parseUser.getJSONArray(KEY_COORDINATES);
            double latitude = jsonCoordinates.getDouble(0);
            double longitude = jsonCoordinates.getDouble(1);
            return new double[]{latitude, longitude};
        } catch (JSONException e) {
            Log.e(TAG, "JSON error with co-ordinates", e);
            return null;
        }
    }

    public void setCoordinates(double[] coordinates) {
        try {
            JSONArray jsonCoordinates = new JSONArray();
            jsonCoordinates.put(coordinates[0]);
            jsonCoordinates.put(coordinates[1]);
            parseUser.put(KEY_COORDINATES, jsonCoordinates);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error with co-ordinates", e);
        }
    }

    public String getZip() {
        return parseUser.getString(KEY_ZIP);
    }

    public void setZip(String zip) {
        parseUser.put(KEY_ZIP, zip);
    }
}
