package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.farmersmarket.LocationUtils;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.Listing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploreFragment extends Fragment {

    // Tag for logging
    public static final String TAG = "ExploreFragment";

    // UI components
    private MapView mvMap;

    // GoogleMap field
    private GoogleMap thisMap;

    private FragmentManager fragmentManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Set Google Map and configure it
            thisMap = googleMap;
            thisMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                private float currentZoom = -1;
                @Override
                public void onCameraChange(CameraPosition camera) {
                    if (camera.zoom != currentZoom){
                        currentZoom = camera.zoom;
                        //here you will then check your markers
                        retrieveListings(googleMap);
                    }
                }
            });

            // Add gestures
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Retrieve user's current location
            LatLng currentLocation = LocationUtils.getCoordinates(getContext());

            // Add a marker for the user's location
            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your current location"));
            
            // Move the camera to the map coordinates and zoom in closer
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Triggered when user click any marker on the map
                    Listing listing = (Listing) marker.getTag();
                    goToDetailedListingScreen(listing);
                    return false;
                }
            });

            retrieveListings(googleMap);
        }
    };

    private void retrieveListings(GoogleMap googleMap) {
        // Determine the visible region of the map
        Projection projection = googleMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();
        LatLng sw = visibleRegion.latLngBounds.southwest;
        LatLng ne = visibleRegion.latLngBounds.northeast;
        double left = sw.longitude;
        double bottom = sw.latitude;
        double right = ne.longitude;
        double top = ne.latitude;

        // Find listings on the visible region of the map
        queryListings(left, right, top, bottom);
    }
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

        // Retrieve UI components
        mvMap = view.findViewById(R.id.mvMap);

        // Configure map
        mvMap.onCreate(savedInstanceState);
        mvMap.getMapAsync(callback);

        // Set fragment manager
        fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
    }

    private void queryListings(double left, double right, double top, double bottom) {
        // Query database for listings on screen
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_AUTHOR);
        query.whereGreaterThanOrEqualTo(Listing.KEY_LONG, left);
        query.whereLessThanOrEqualTo(Listing.KEY_LONG, right);
        query.whereGreaterThanOrEqualTo(Listing.KEY_LAT, bottom);
        query.whereLessThanOrEqualTo(Listing.KEY_LAT, top);
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                // Check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting listings", e);
                    return;
                }
                // Add a marker for each listing on screen
                for (Listing listing : listings) {
                    LatLng coordinates = new LatLng(listing.getLatitude(), listing.getLongitude());
                    Marker marker = thisMap.addMarker(new MarkerOptions().position(coordinates).title(listing.getCategory()));
                    marker.setTag(listing);
                }
            }
        });
    }

    private void goToDetailedListingScreen(Listing listing) {
        Fragment fragment = new DetailedListingFragment(listing);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
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