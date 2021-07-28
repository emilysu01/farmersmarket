package com.example.farmersmarket.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {

    // Tag for logging statements
    public static final String TAG = "Message";

    // Database keys
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIPIENT = "recipient";
    public static final String KEY_MESSAGE = "message";

    // Getters and setters
    public String getObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    public void setObjectId(String objectId) {
        put(KEY_OBJECT_ID, objectId);
    }

    public Date getMessageTime() {
        return getDate(KEY_CREATED_AT);
    }

    public void setMessageTime(Date time) {
        put(KEY_CREATED_AT, time);
    }

    public User getSender() {
        return new User(getParseUser(KEY_SENDER));
    }

    public void setSender(User sender) {
        put(KEY_SENDER, sender.userToParseUser());
    }

    public User getRecipient() {
        return new User(getParseUser(KEY_RECIPIENT));
    }

    public void setRecipient(User recipient) {
        put(KEY_RECIPIENT, recipient.userToParseUser());
    }

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

    public String getSenderId() {
        return getParseUser(KEY_SENDER).getObjectId();
    }
}
