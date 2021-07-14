package com.example.farmersmarket.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LISTINGS = "listings";

    // Getters and setters
    public String getKeyUserId() {
        return getString(KEY_USER_ID);
    }

    public String getKeyName() {
        return getString(KEY_NAME);
    }

    public String getKeyUsername() {
        return getString(KEY_USERNAME);
    }

    public String getKeyPassword() {
        return getString(KEY_PASSWORD);
    }

    public String getKeyEmail() {
        return getString(KEY_EMAIL);
    }

    public ParseFile getKeyProfilePic() {
        return getParseFile(KEY_PROFILE_PIC);
    }

    public ParseGeoPoint getKeyLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    // public List<Listing> getKeyListings() {
    //     return KEY_LISTINGS;
    // }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public void setEmail(String email) {
        put(KEY_EMAIL, email);
    }

    public void setPassword(String password) {
        put(KEY_PASSWORD, password);
    }

    public void setZip(String zip) {
        put(KEY_ZIP, zip);
    }
}
