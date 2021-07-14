package com.example.farmersmarket.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmersmarket.R;
import com.example.farmersmarket.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

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

        // Set onClickListener for log in link and sign up button
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to log in screen
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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

    // TODO: Do more detailed error checking
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
        return true;
    }

    private void signUp(String firstName, String lastName, String username, String email, String password, String zip) {
        Log.i(TAG, "Attempting to sign up user " + username);

        // Create new ParseUser
        ParseUser newUser = new ParseUser();
        // Default ParseUser fields
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        // Additional User fields
        newUser.put(User.KEY_NAME, firstName + " " + lastName);
        newUser.put(User.KEY_ZIP, zip);
        newUser.put(User.KEY_LISTINGS, new ArrayList<>());

        // Sign up with Parse
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(SignUpActivity.this, "Issue with signing up. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with signing up ", e);
                    return;
                }

                Toast.makeText(SignUpActivity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();;

                // Move to main screen
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        // Move to main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}