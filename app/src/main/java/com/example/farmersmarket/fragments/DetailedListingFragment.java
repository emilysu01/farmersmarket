package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

public class DetailedListingFragment extends Fragment {

    // UI components
    private ImageView ivProfilePic;
    private TextView tvName;
    private ImageView ivListingPic;
    private TextView tvDescription;
    private Button btnContact;
    private MapView mvSellerLocation;

    // Current listing
    private Listing listing;

    // Required empty public constructor
    public DetailedListingFragment() {

    }

    public DetailedListingFragment(Listing listing) {
        this.listing = listing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvName = view.findViewById(R.id.tvName);
        ivListingPic = view.findViewById(R.id.ivListingPic);
        tvDescription = view.findViewById(R.id.tvDescription);
        btnContact = view.findViewById(R.id.btnContact);
        mvSellerLocation = view.findViewById(R.id.mvSellerLocation);

        // Display UI
        ParseUser user = listing.getAuthor();
        Glide.with(getContext())
                .load(user.getParseFile(User.KEY_PROFILE_PIC).getUrl())
                .circleCrop()
                .into(ivProfilePic);
        tvName.setText(user.getString(User.KEY_NAME));
        Glide.with(getContext())
                .load(listing.getImages().get(0).getUrl())
                .into(ivListingPic);
        tvDescription.setText(listing.getDescription());
        mvSellerLocation.onCreate(savedInstanceState);
        mvSellerLocation.getMapAsync(callback);

        // Set onClickListener to go to new screen
        ivListingPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFullScreenImageScreen();
            }
        });

        // Set onClickListeners for profile picture and name
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileScreen(listing);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileScreen(listing);
            }
        });
    }

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
        }
    };

    private void goToProfileScreen(Listing listing) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = new ProfileFragment(listing.getAuthor());
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void goToFullScreenImageScreen() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = new FullScreenImageFragment(listing.getImages().get(0));
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    @Override
    public void onResume() {
        mvSellerLocation.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvSellerLocation.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvSellerLocation.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvSellerLocation.onLowMemory();
    }
}