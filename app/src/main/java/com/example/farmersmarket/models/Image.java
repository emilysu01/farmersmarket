package com.example.farmersmarket.models;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class Image {

    private byte[] imageData;


    /* public static Image parseObjectToImage(ParseObject parseObject) {
        Image newImage = new Image();
        newImage.
    } */

    public static Image parseFileProcess(ParseFile parseFile) throws ParseException {
        Image newImage = new Image();
        newImage.imageData = parseFile.getData();
        return newImage;
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
