package com.example.farmersmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.farmersmarket.R;
import com.example.farmersmarket.activities.MapsFragment;
import com.google.android.gms.maps.MapView;

public class TestActivity extends AppCompatActivity {

    private MapView mvTest;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mvTest = findViewById(R.id.map);

        Fragment fragment = new MapsFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }


}