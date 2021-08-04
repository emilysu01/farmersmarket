package com.example.farmersmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.farmersmarket.R;
import com.example.farmersmarket.fragments.AllMessagesFragment;
import com.example.farmersmarket.fragments.BasketFragment;
import com.example.farmersmarket.fragments.ExploreFragment;
import com.example.farmersmarket.fragments.HomeFragment;
import com.example.farmersmarket.fragments.ListFragment;
import com.example.farmersmarket.fragments.ProfileFragment;
import com.example.farmersmarket.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    // Tag for logging statements
    public static final String TAG = "MainActivity";

    // UI components
    private BottomNavigationView bottomNavigationView;

    // Fragment manager
    private final FragmentManager fragmentManager = getSupportFragmentManager();

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
                // Determine which menu item was clicked and navigate to the correct screen
                Fragment fragment;
                int clicked = item.getItemId();
                if (clicked == R.id.action_home) {
                    fragment = new HomeFragment();
                } else if (clicked == R.id.action_explore) {
                    fragment = new ExploreFragment();
                } else if (clicked == R.id.action_search) {
                    fragment = new SearchFragment();
                } else if (clicked == R.id.action_list) {
                    fragment = new ListFragment();
                } else {
                    fragment = new AllMessagesFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Must return true for the menu to be displayed
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Log out
        if (item.getItemId() == R.id.miLogOut) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.miProfile) {
            fragmentManager.beginTransaction().replace(R.id.flContainer, new ProfileFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.miBasket) {
            fragmentManager.beginTransaction().replace(R.id.flContainer, new BasketFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}