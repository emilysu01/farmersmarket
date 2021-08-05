package com.example.farmersmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmersmarket.LocationUtils;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

        // Set up animations
        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_button);
        final Animation textAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_text);

        // Set onClickListener for login link
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bounce animation
                tvLogin.startAnimation(textAnimation);

                // Move to login screen
                goToLoginActivity();
            }
        });

        // Set onClickListener for sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bounce animation
                btnSignUp.startAnimation(buttonAnimation);

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
        final boolean[] duplicateUsername = new boolean[1];
        ParseQuery<ParseUser> usernameQuery = ParseQuery.getQuery("User");
        usernameQuery.whereEqualTo(User.KEY_USERNAME, username);
        usernameQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with checking duplicate username");
                    duplicateUsername[0] = true;
                }
                if (!objects.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "An account with this username already exists", Toast.LENGTH_SHORT).show();
                    duplicateUsername[0] = true;
                }
            }
        });
        if (duplicateUsername[0]) {
            return false;
        }

        final boolean[] duplicateEmail = new boolean[1];
        ParseQuery<ParseUser> emailQuery = ParseQuery.getQuery("User");
        emailQuery.whereEqualTo(User.KEY_EMAIL, email);
        emailQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with checking duplicate username");
                    duplicateEmail[0] = true;
                }
                if (!objects.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "An account with this email already exists", Toast.LENGTH_SHORT).show();
                    duplicateEmail[0] = true;
                }
            }
        });
        if (duplicateEmail[0]) {
            return false;
        }

        return true;
    }

    private void signUp(String firstName, String lastName, String username, String email, String password, String zip) {
        Log.i(TAG, "Attempting to sign up user with username " + username);

        // Retrieve the user's current location
        double[] coordinates = getCoordinates();
        if (coordinates == null) {
            Log.e(TAG, "Error with retrieving user's location for sign up");
            return;
        }

        // Create new user
        ParseUser newParseUser = new ParseUser();
        newParseUser.put(User.KEY_USERNAME, username);
        newParseUser.put(User.KEY_PASSWORD, password);
        newParseUser.put(User.KEY_EMAIL, email);
        newParseUser.put(User.KEY_NAME, firstName + " " + lastName);
        // newParseUser.put(User.KEY_COORDINATES, coordinates);
        newParseUser.put(User.KEY_ZIP, zip);

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
            // Check for permission
            LocationUtils.checkAndRequestPermissions(this, SignUpActivity.this);

            // Retrieve location
            LatLng location = LocationUtils.getCoordinates(this, SignUpActivity.this);
            double[] arrCoordinates = new double[]{location.latitude, location.longitude};
            return arrCoordinates;
        } catch (Exception e) {
            Toast.makeText(this, "There was an issue with retrieving your location. Please try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error with retrieving location", e);
            return null;
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}