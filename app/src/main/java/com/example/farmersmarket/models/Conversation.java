package com.example.farmersmarket.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

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

    @Override
    public String getObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    @Override
    public void setObjectId(String objectId) {
        put(KEY_OBJECT_ID, objectId);
    }

    @Override
    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public void setCreatedAt(Date createdAt) {
        put(KEY_CREATED_AT, createdAt);
    }

    public User getPerson1() {
        ParseUser parseUser = getParseUser(KEY_PERSON_1);
        return new User(parseUser);
    }

    public void setPerson1(User person1) {
        put(KEY_PERSON_1, person1.userToParseUser());
    }

    public User getPerson2() {
        ParseUser parseUser = getParseUser(KEY_PERSON_2);
        return new User(parseUser);
    }

    public void setPerson2(User person2) {
        put(KEY_PERSON_2, person2.userToParseUser());
    }

    public Message getLatestMessage() {
        return (Message) getParseObject(KEY_LATEST_MESSAGE);
    }

    public void setLatestMessage(Message latestMessage) {
        put(KEY_LATEST_MESSAGE, latestMessage);
    }

}
