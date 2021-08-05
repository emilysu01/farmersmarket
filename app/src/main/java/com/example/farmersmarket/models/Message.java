package com.example.farmersmarket.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    public ParseUser getSender() {
        return getParseUser(KEY_SENDER);
    }

    public void setSender(ParseUser sender) {
        put(KEY_SENDER, sender);
    }

    public ParseUser getRecipient() {
        return getParseUser(KEY_RECIPIENT);
    }

    public void setRecipient(ParseUser recipient) {
        put(KEY_RECIPIENT, recipient);
    }

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }
}
