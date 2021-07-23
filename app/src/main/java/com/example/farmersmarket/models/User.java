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

    public ParseUser userToParseUser() {
        return parseUser;
    }

    // Getters and setters
    public String getUserId() {
        return parseUser.getString(KEY_OBJECT_ID);
    }

    public String getUsername() {
        return parseUser.getString(KEY_USERNAME);
    }

    public String getPassword() {
        return parseUser.getString(KEY_PASSWORD);
    }

    public String getEmail() {
        return parseUser.getString(KEY_EMAIL);
    }

    public String getName() {
        return parseUser.getString(KEY_NAME);
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

    public String getZip() {
        return parseUser.getString(KEY_ZIP);
    }

    public ParseFile getProfilePic() {
        return parseUser.getParseFile(KEY_PROFILE_PIC);
    }

}
