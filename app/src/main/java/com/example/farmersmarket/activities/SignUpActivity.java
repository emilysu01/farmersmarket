package com.example.farmersmarket.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.farmersmarket.LocationUtils;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    // Tag for logging statements
    public static final String TAG = "SignUpActivity";

    // UI components
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etZip;
    private TextView tvLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Retrieve UI components
        // Label TextViews aren't retrieved because they're not interactive
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etZip = findViewById(R.id.etZip);
        tvLogin = findViewById(R.id.tvLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Set onClickListener for login link
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to login screen
                goToLoginActivity();
            }
        });

        // Set onClickListener for sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve typed text
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String zip = etZip.getText().toString();

                // Error checking
                boolean errors = checkForErrors(firstName, lastName, username, email, password, zip);
                if (!errors) {
                    return;
                }

                // Sign up
                signUp(firstName, lastName, username, email, password, zip);
            }
        });
    }

    private boolean checkForErrors(String firstName, String lastName, String username, String email, String password, String zip) {
        // Check for empty fields
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Name cannot be blank. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Username cannot be blank. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Email cannot be blank. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Password cannot be blank. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (zip.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Zip code cannot be blank. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for duplicate usernames and emails
        boolean[] duplicates = new boolean[2];
        ParseQuery<ParseObject> usernameQuery = ParseQuery.getQuery("User");
        usernameQuery.whereEqualTo(User.KEY_USERNAME, username);
        usernameQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with checking duplicate username");
                    duplicates[0] = true;
                }
                if (objects.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "An account with this username already exists", Toast.LENGTH_SHORT).show();
                    duplicates[0] = true;
                }
            }
        });
        if (duplicates[0]) {
            return false;
        }
        ParseQuery<ParseObject> emailQuery = ParseQuery.getQuery("User");
        emailQuery.whereEqualTo(User.KEY_EMAIL, email);
        emailQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with checking duplicate email");
                    duplicates[1] = true;
                }
                if (objects.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "An account with this email already exists", Toast.LENGTH_SHORT).show();
                    duplicates[1] = true;
                }
            }
        });
        if (duplicates[1]) {
            return false;
        }
        return true;
    }

    private void signUp(String firstName, String lastName, String username, String email, String password, String zip) {
        Log.i(TAG, "Attempting to sign up user with username " + username);

        // Retrieve the user's current location
        double[] coordinates = getCoordinates();
        if (coordinates == null) {
            return;
        }

        // Create new user
        User newUser = new User(username, password, email, firstName + " " + lastName, coordinates, zip);
        ParseUser newParseUser = newUser.userToParseUser();

        // Sign up with Parse
        newParseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(SignUpActivity.this, "There was an issue with signing up. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with sign up ", e);
                    return;
                }
                Toast.makeText(SignUpActivity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();;

                // Move to main screen
                goToMainActivity();
            }
        });
    }

    private double[] getCoordinates() {
        try {
            // Checks for permission
            LocationUtils.checkAndRequestPermissions(this, SignUpActivity.this);

            // Retrieve location
            LatLng location = LocationUtils.getCoordinates(this, SignUpActivity.this);
            double[] arrCoordinates = new double[]{location.latitude, location.longitude};
            return arrCoordinates;
        } catch (Exception e) {
            Toast.makeText(this, "There was an issue retrieving your location. Please try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error with retrieving location", e);
            return null;
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}