package com.example.farmersmarket.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Conversation")
public class Conversation extends ParseObject {

    // Tag for logging statements
    public static final String TAG = "Conversation";

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_PERSON_1 = "person1";
    public static final String KEY_PERSON_2 = "person2";
    public static final String KEY_LATEST_MESSAGE = "latestMessage";

    public String getPerson1() {
        return getString(KEY_PERSON_1);
    }

    public void setPerson1(String person1) {
        put(KEY_PERSON_1, person1);
    }

    public String getPerson2() {
        return getString(KEY_PERSON_2);
    }

    public void setPerson2(String person2) {
        put(KEY_PERSON_2, person2);
    }

    public Message getLatestMessage() {
        return (Message) getParseObject(KEY_LATEST_MESSAGE);
    }

    public void setLatestMessage(Message latestMessage) {
        put(KEY_LATEST_MESSAGE, latestMessage);
    }

}
