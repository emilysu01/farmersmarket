package com.example.farmersmarket;

import android.app.Application;

import com.example.farmersmarket.models.Listing;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register Listing class with Parse
        ParseObject.registerSubclass(Listing.class);

        // TODO: Put API keys in separate doc
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9yw3iEXVrqGQrwetAJSottc652q5vhytsWNeHdDv")
                .clientKey("IUBzLc80uWiM1lAm4tp3AfbUZHRuKdgHy4Vz1afj")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
