package com.example.farmersmarket.fragments;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmersmarket.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class ExploreFragment extends Fragment {

    // UI components
    private MapView mvMap;

    private GoogleMap thisMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Set the map coordinates to SF
            // TODO: Update to make dynamic
            LatLng sf = new LatLng(37.484928, -122.148201);
            // Add a marker on the map coordinates
            googleMap.addMarker(new MarkerOptions().position(sf).title("Marker in SF"));
            // Move the camera to the map coordinates and zoom in closer
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sf));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            // Add gestures
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            Projection projection = googleMap.getProjection();
            VisibleRegion visibleRegion = projection.getVisibleRegion();
            LatLngBounds bounds = visibleRegion.latLngBounds;
            
            Log.i("ExploreFragment", bounds.toString());
        }
    };

    // Required empty public constructor
    public ExploreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mvMap = view.findViewById(R.id.mvMap);

        mvMap.onCreate(savedInstanceState);
        mvMap.getMapAsync(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvMap.onLowMemory();
    }
}