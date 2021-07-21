package com.example.farmersmarket.models;

import com.parse.ParseClassName;
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

    public Listing() {
        this.objectId = getString(KEY_OBJECT_ID);
        this.createdAt = getDate(KEY_CREATED_AT);
        this.author = new User(getParseUser(KEY_AUTHOR));
        this.description = getString(KEY_DESCRIPTION);
        List<Object> rawImages = getList(KEY_IMAGES);
        this.images = Image.rawToProcessedList(rawImages);
        List<Object> rawLocation = getList(KEY_LOCATION);
        this.location = new double[]{(double) rawLocation.get(0), (double) rawLocation.get(1)};
        this.price = getInt(KEY_PRICE);
        this.units = getInt(KEY_UNITS);
        this.category = getString(KEY_CATEGORY);
        List<Object> rawColors = getList(KEY_COLORS);
        this.colors = new ArrayList<String>();
        for (Object color : rawColors) {
            this.colors.add((String) color);
        }
        this.sellBy = getDate(KEY_SELL_BY);
        this.delivery = getBoolean(KEY_DELIVERY);
    }

    // Getters and setters


    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Date getSellBy() {
        return sellBy;
    }

    public void setSellBy(Date sellBy) {
        this.sellBy = sellBy;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }
}
