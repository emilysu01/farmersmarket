package com.example.farmersmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.farmersmarket.R;
import com.example.farmersmarket.fragments.HomeFragment;
import com.example.farmersmarket.fragments.ListFragment;
import com.example.farmersmarket.fragments.ProfileFragment;
import com.example.farmersmarket.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    // UI components
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve UI components
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set bottom navigation view functionality
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Check which menu item was clicked and navigate to the correct screen
                Fragment fragment;
                int clicked = item.getItemId();
                if (clicked == R.id.action_home) {
                    fragment = new HomeFragment();
                } else if (clicked == R.id.action_search) {
                    fragment = new SearchFragment();
                } else if (clicked == R.id.action_list) {
                    fragment = new ListFragment();
                } else {
                    fragment = new ProfileFragment(ParseUser.getCurrentUser());
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set defaut selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu (adds items to the action bar if it's present)
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Must return true for the menu to be displayed
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Log out
        if (item.getItemId() == R.id.miLogOut) {
            logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
    }
}