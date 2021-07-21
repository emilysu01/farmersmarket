package com.example.farmersmarket.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Listing")
public class Listing extends ParseObject {

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PRICE = "price";
    public static final String KEY_UNITS = "units";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COLORS = "colors";
    public static final String KEY_SELL_BY = "sellBy";
    public static final String KEY_DELIVERY = "delivery";

    //
    private String objectId;
    private Date createdAt;
    private User author;
    private String description;
    private List<Image> images;
    private double[] location;
    private int price;
    private int units;
    private String category;
    private List<String> colors;
    private Date sellBy;
    private boolean delivery;

    /* public Listing() {
        objectId = getString(KEY_OBJECT_ID);
        createdAt = getDate(KEY_CREATED_AT);
        author = new User(getParseUser(KEY_AUTHOR));
        description = getString(KEY_DESCRIPTION);
        List<Object> rawImages = getList(KEY_IMAGES);
        images = Image.rawToProcessedList(rawImages);
        List<Object> rawLocation = getList(KEY_LOCATION);
        location = new double[]{(double) rawLocation.get(0), (double) rawLocation.get(1)};
        price = getInt(KEY_PRICE);
        units = getInt(KEY_UNITS);
        category = getString(KEY_CATEGORY);
        List<Object> rawColors = getList(KEY_COLORS);
        colors = new ArrayList<String>();
        for (Object color : rawColors) {
            this.colors.add((String) color);
        }
        sellBy = getDate(KEY_SELL_BY);
        delivery = getBoolean(KEY_DELIVERY);
    } */

    // Getters and setters


    @Override
    public String getObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // TODO: Fix later
    public User getAuthor() {
        return new User();
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // TODO: Fix later
    public List<Image> getImages() {
        // try {
            List<ParseObject> rawImages = getList(KEY_IMAGES);
            List<Image> images = new ArrayList<Image>();
            for (ParseObject image : rawImages) {
                ParseFile newImage = null;
                try {
                    newImage = image.fetchIfNeeded().getParseFile("image");
                    Log.i("Listing", newImage.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // images.add(Image.parseFileProcess(newImage));


                //images.add(Image.parseFileProcess(image));
            }
        // }
        /* catch (ParseException e) {
            e.printStackTrace();
        } */
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public double[] getLocation() {
        List<Object> rawLocation = getList(KEY_LOCATION);
        return  new double[]{(double) rawLocation.get(0), (double) rawLocation.get(1)};
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public int getPrice() {
        return getInt(KEY_PRICE);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnits() {
        return getInt(KEY_UNITS);
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getColors() {
        List<Object> rawColors = getList(KEY_COLORS);
        List<String> processedColors = new ArrayList<String>();
        for (Object color : rawColors) {
            processedColors.add((String) color);
        }
        return processedColors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Date getSellBy() {
        return getDate(KEY_SELL_BY);
    }

    public void setSellBy(Date sellBy) {
        this.sellBy = sellBy;
    }

    public boolean isDelivery() {
        return getBoolean(KEY_DELIVERY);
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }
}
