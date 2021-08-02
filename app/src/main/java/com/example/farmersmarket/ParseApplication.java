package com.example.farmersmarket;

import android.app.Application;
import android.os.Build;

import com.example.farmersmarket.models.Conversation;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register Listing class with Parse
        ParseObject.registerSubclass(Listing.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Conversation.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APPLICATION_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
