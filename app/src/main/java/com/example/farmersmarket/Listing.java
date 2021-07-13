package com.example.farmersmarket;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

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

    public ParseUser getKeyAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public ParseFile getKeyImage() {
        return getParseFile(KEY_IMAGE);
    }

    public String getKeyDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseGeoPoint getKeyLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public String getKeyType() {
        return getString(KEY_TYPE);
    }

    public Date getKeySellBy() {
        return getDate(KEY_SELL_BY);
    }

}
