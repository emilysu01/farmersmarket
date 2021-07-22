package com.example.farmersmarket.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Image")
public class Image extends ParseObject {

    public static final String TAG = "Image";

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_IMAGE = "image";

    public String id;
    public ParseFile parseFile;
    private byte[] imageData;

    public static Image jsonObjectToImage(JSONObject jsonObject) {
        try {
            Image image = new Image();
            image.id = jsonObject.getString(KEY_OBJECT_ID);
            image.parseFile = queryParseFile(image.id);
            return image;
        } catch (JSONException e) {
            Log.e(TAG, "Error with converting JSON object to Image object");
            e.printStackTrace();
        }
        return null;
    }

    public static ParseFile queryParseFile(String id) {
        // Specify that we want to query Listing.class data
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        // query.whereEqualTo(KEY_OBJECT_ID, id);
        // Start an asynchronous call for listings
        final ParseFile[] fileToReturn = new ParseFile[1];
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                } else {
                    fileToReturn[0] = parseObject.getParseFile(KEY_IMAGE);
                    Log.i("FileToReturn", fileToReturn[0].toString());
                }
            }
        });
        return fileToReturn[0];
    }

    public static Image parseFileProcess(ParseFile parseFile) throws ParseException {
        Image newImage = new Image();
        newImage.imageData = parseFile.getData();
        return newImage;
    }

    public static byte[] parseFileToByteArray(Image image) {
        ParseFile parseFile = image.parseFile;
        final byte[][] dataToReturn = new byte[1][1];
        parseFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                } else {
                    dataToReturn[0] = data;
                }
            }
        });
        return dataToReturn[0];
    }
    public static List<Image> rawToProcessedList(List<Object> rawData) {
        if (rawData.isEmpty()) {
            return new ArrayList<Image>();
        }
        List<Image> processedData = new ArrayList<Image>();
        for (Object image : rawData) {
            processedData.add((Image) image);
        }
        return processedData;
    }
}
