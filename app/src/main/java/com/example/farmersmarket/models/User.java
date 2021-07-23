package com.example.farmersmarket.models;

import android.util.Log;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class User {

    // Tag for logging statements
    public static final String TAG = "User";

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_NAME = "name";
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_ZIP = "zip";

    // ParseUser field (there were issues with creating a class that extends ParseUser)
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
            JSONArray parseCoordinates = new JSONArray();
            parseCoordinates.put(coordinates[0]);
            parseCoordinates.put(coordinates[1]);
            newUser.put(KEY_COORDINATES, parseCoordinates);
            newUser.put(KEY_ZIP, zip);
            this.parseUser = newUser;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
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
        return parseUser.getString(KEY_NAME);
    }

    public void setName(String name) {
        parseUser.put(KEY_NAME, name);
    }

    public double[] getCoordinates() {
        try {
            JSONArray rawCoordinates = parseUser.getJSONArray(KEY_COORDINATES);
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
            parseUser.put(KEY_COORDINATES, parseCoordinates);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
    }

    public String getZip() {
        return parseUser.getString(KEY_ZIP);
    }

    public void setZip(String zip) {
        parseUser.put(KEY_ZIP, zip);
    }

    public ParseFile getProfilePic() {
        return parseUser.getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile profilePic) {
        parseUser.put(KEY_PROFILE_PIC, profilePic);
    }

}
