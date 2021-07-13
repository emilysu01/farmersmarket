package com.example.farmersmarket;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class User extends ParseObject {

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PIC = "profilePic";
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
}
