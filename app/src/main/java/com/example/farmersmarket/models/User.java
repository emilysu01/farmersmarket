package com.example.farmersmarket.models;

import android.util.Log;
import android.widget.Toast;

import com.example.farmersmarket.activities.LoginActivity;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";

    private String userId;
    private String username;
    private String password;
    private String email;
    private Image profilePic;
    private String name;
    private double[] location;

    public User() {

    }
    
    public static ParseUser userToParseUser(String username, String password, String email, String name, double[] location) throws JSONException {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put(User.KEY_NAME, name);
        JSONArray parseLocation = new JSONArray();
        parseLocation.put(location[0]);
        parseLocation.put(location[1]);
        newUser.put(User.KEY_LOCATION, parseLocation);
        return newUser;
    }

    public User(ParseObject user) {
        userId = user.getString(KEY_OBJECT_ID);
        username = user.getString(KEY_USERNAME);
        password = user.getString(KEY_PASSWORD);
        email = user.getString(KEY_EMAIL);
        try {
            profilePic = Image.parseFileProcess(user.getParseFile(KEY_PROFILE_PIC));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        name = user.getString(KEY_NAME);
        List<Object> rawLocation = user.getList(KEY_LOCATION);
        location = new double[]{(double) rawLocation.get(0), (double) rawLocation.get(1)};
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
