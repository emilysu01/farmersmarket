package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.example.farmersmarket.adapters.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    // Tag for logging statements
    public static final String TAG = "ProfileFragment";

    // UI components
    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvLocation;
    private RecyclerView rvShortListings;

    // Listings data structure and adapter
    private List<Listing> allListings = new ArrayList<>();
    private ShortListingsAdapter adapter;

    // Currently logged in ParseUser
    private User user = new User(ParseUser.getCurrentUser());

    // Required empty public constructor
    public ProfileFragment() {

    }

    public ProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        rvShortListings = view.findViewById(R.id.rvShortListings);

        // Configure adapter
        adapter = new ShortListingsAdapter(getContext(), allListings);
        rvShortListings.setAdapter(adapter);
        rvShortListings.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Display UI
        ParseFile profilePic = user.getProfilePic();
        if (profilePic != null) {
            Glide.with(getContext())
                    .load(profilePic.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        }
        tvName.setText(user.getName());
        tvUsername.setText(user.getUsername());

        // Retrieve listings from database
        queryListings(user);
    }

    private void queryListings(User user) {
        // Query database for listings made by the current user
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_AUTHOR);
        query.addDescendingOrder(Listing.KEY_CREATED_AT);
        query.whereEqualTo(Listing.KEY_AUTHOR, user.userToParseUser());

        // Start an asynchronous call for listings
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                // Check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // Save received listings to data structure and notify adapter of new data
                allListings.addAll(listings);
                adapter.notifyDataSetChanged();
            }
        });
    }
}