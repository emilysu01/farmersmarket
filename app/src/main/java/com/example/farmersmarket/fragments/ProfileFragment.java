package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.example.farmersmarket.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    ImageView ivProfilePic;
    TextView tvName;
    TextView tvUsername;
    TextView tvLocation;
    RecyclerView rvShortListings;

    private List<Listing> allListings = new ArrayList<>();
    private ShortListingsAdapter adapter;

    ParseUser user = ParseUser.getCurrentUser();

    // Required empty public constructor
    public ProfileFragment() {

    }

    // Required empty public constructor
    public ProfileFragment(ParseUser user) {
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
        tvLocation = view.findViewById(R.id.tvLocation);
        rvShortListings = view.findViewById(R.id.rvShortListings);

        // Configure adapter
        adapter = new ShortListingsAdapter(getContext(), allListings);
        rvShortListings.setAdapter(adapter);
        rvShortListings.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        ParseFile profilePic = user.getParseFile(User.KEY_PROFILE_PIC);
        if (profilePic != null) {
            Glide.with(getContext())
                    .load(profilePic.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        }
        tvName.setText(user.getString(User.KEY_NAME));
        tvUsername.setText(user.getString(User.KEY_USERNAME));
        tvLocation.setText(user.getString(User.KEY_LOCATION));

        // Retrieve listings from database
        queryListings(user);
    }

    private void queryListings(ParseUser user) {
        // Specify that we want to query Listing.class data
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        // Include data referred by user key
        query.include(Listing.KEY_AUTHOR);
        // Order posts by newest first
        query.addDescendingOrder(Listing.KEY_CREATED_AT);
        // Filter by current user
        query.whereEqualTo(Listing.KEY_AUTHOR, user);

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