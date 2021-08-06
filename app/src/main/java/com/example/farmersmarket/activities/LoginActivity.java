package com.example.farmersmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.farmersmarket.LocationUtils;
import com.example.farmersmarket.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    // Tag for logging statements
    public static final String TAG = "LoginActivity";

    // UI components
    private ImageView ivLogo;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check and ask for permissions
        LocationUtils.checkAndRequestPermissions(this, LoginActivity.this);

        // Check if a user is already logged in
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        // Retrieve UI components
        ivLogo = findViewById(R.id.ivLogo);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        // Set up animations
        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_button);
        final Animation textAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_text);

        // Display UI
        Glide.with(this)
                .load(getResources().getDrawable(R.mipmap.ic_basket))
                .apply(new RequestOptions().override(1000, 1000))
                .into(ivLogo);

        // Set onClickListener for sign up link
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bounce animation
                tvSignUp.startAnimation(textAnimation);

                // Move to sign up screen
                goToSignUpActivity();
            }
        });

        // Set onClickListener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bounce animation
                btnLogin.startAnimation(buttonAnimation);

                // Retrieve typed text
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Error checking
                boolean loginErrors = loginErrorChecking(username, password);
                if (!loginErrors) {
                    return;
                }

                // Log in
                login(username, password);
            }
        });
    }

    private boolean loginErrorChecking(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Username cannot be empty. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Password cannot be empty. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String username, String password) {
        Log.i(TAG, "Attempting to log in user with username: " + username);

        // Log in with Parse
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "There was an issue with login. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

                // Move to main screen
                goToMainActivity();
            }
        });
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}