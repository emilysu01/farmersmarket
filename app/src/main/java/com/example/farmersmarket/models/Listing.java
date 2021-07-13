package com.example.farmersmarket.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Listing")
public class Listing extends ParseObject {

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SELL_BY = "sellBy";


    // Getters and setters
    public String getKeyObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    public String getKeyCreatedAt() {
        return String.valueOf(getCreatedAt());
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public String getType() {
        return getString(KEY_TYPE);
    }

    public Date getSellBy() {
        return getDate(KEY_SELL_BY);
    }

}
