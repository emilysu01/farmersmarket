package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.farmersmarket.models.Conversation;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.Message;
import com.example.farmersmarket.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailedListingFragment extends Fragment {

    public static final String TAG = "DetailedListingFragment";

    // UI components
    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvUsername;
    private ImageView ivListingPic;
    private TextView tvDescription;
    private TextView tvCategory;
    private TextView tvPrice;
    private TextView tvUnits;
    private TextView tvSellByDate;
    private TextView tvDelivery;
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
        tvUsername = view.findViewById(R.id.tvUsername);
        ivListingPic = view.findViewById(R.id.ivListingPic);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvUnits = view.findViewById(R.id.tvUnits);
        tvSellByDate = view.findViewById(R.id.tvSellByDate);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        btnContact = view.findViewById(R.id.btnContact);
        mvSellerLocation = view.findViewById(R.id.mvSellerLocation);

        // Display UI
        ParseUser user = listing.getAuthor();
        Glide.with(getContext())
                .load(user.getParseFile(User.KEY_PROFILE_PIC).getUrl())
                .circleCrop()
                .into(ivProfilePic);
        tvName.setText(user.getString(User.KEY_NAME));
        tvUsername.setText("@" + user.getString(User.KEY_USERNAME));
        Glide.with(getContext())
                .load(listing.getImages().get(0).getUrl())
                .into(ivListingPic);
        tvDescription.setText(listing.getDescription());
        tvCategory.setText(listing.getCategory());
        tvPrice.setText("$" + String.valueOf(listing.getPrice()));
        tvUnits.setText(String.valueOf(listing.getUnits()));
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(listing.getSellBy());
        tvSellByDate.setText(strDate);
        boolean delivery = listing.getDelivery();
        if (delivery) {
            tvDelivery.setText("Available");
        } else {
            tvDelivery.setText("Unavailable");
        }
        mvSellerLocation.onCreate(savedInstanceState);
        OnMapReadyCallback callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng coordinates = new LatLng(listing.getLatitude(), listing.getLongitude());
                // Add a marker on the map coordinates
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("Listing location"));
                // Move the camera to the map coordinates and zoom in closer
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                // Add gestures
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
            }
        };
        mvSellerLocation.getMapAsync(callback);

        // Set onClickListener to go to full screen image screen
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
        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileScreen(listing);
            }
        });

        // Set onClickListener for contact button
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser other = listing.getAuthor();

                ParseQuery<Conversation> query1 = ParseQuery.getQuery("Conversation");
                query1.whereEqualTo(Conversation.KEY_USER_1, ParseUser.getCurrentUser());
                query1.whereEqualTo(Conversation.KEY_USER_2, other);

                ParseQuery<Conversation> query2 = ParseQuery.getQuery("Conversation");
                query2.whereEqualTo(Conversation.KEY_USER_1, other);
                query2.whereEqualTo(Conversation.KEY_USER_2, ParseUser.getCurrentUser());

                List<ParseQuery<Conversation>> queries = new ArrayList<ParseQuery<Conversation>>();
                queries.add(query1);
                queries.add(query2);

                ParseQuery<Conversation> finalQuery = ParseQuery.or(queries);
                finalQuery.include(Conversation.KEY_LATEST_MESSAGE);

                finalQuery.orderByDescending("createdAt");
                finalQuery.findInBackground(new FindCallback<Conversation>() {
                    @Override
                    public void done(List<Conversation> objects, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error with retrieving conversations", e);
                            return;
                        }
                        if (objects.isEmpty()) {
                            Conversation conversation = new Conversation();
                            conversation.setPerson1(ParseUser.getCurrentUser());
                            conversation.setPerson2(listing.getAuthor());
                            conversation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error saving conversation", e);
                                        return;
                                    }
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    Fragment fragment = new SingleMessageFragment(conversation);
                                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                                }
                            });
                        } else {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            Fragment fragment = new SingleMessageFragment(objects.get(0));
                            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        }
                    }
                });
            }
        });
    }

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