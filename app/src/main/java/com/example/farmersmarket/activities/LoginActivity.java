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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    // UI components
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Retrieve UI components
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        // Set onClickListeners for sign up link and log in button
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to sign up screen
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve typed text
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Error checking
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username cannot be empty. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log in
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        Log.i(TAG, "Attempting to log in user " + username);

        // Log in with Parse
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "Issue with login. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with logging in ", e);
                    return;
                }
                Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();;

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