package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmersmarket.ListingsAdapter;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    private RecyclerView rvListings;

    private List<Listing> allListings = new ArrayList<>();
    private ListingsAdapter adapter;

    // Required empty public constructor
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        rvListings = view.findViewById(R.id.rvListings);

        // Configure adapter
        adapter = new ListingsAdapter(getContext(), allListings);
        rvListings.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListings.setLayoutManager(linearLayoutManager);

        // Retrieve listings from database
        queryListings();
    }

    private void queryListings() {
        // Specify that we want to query Listing.class data
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        // Include data referred by user key
        query.include(Listing.KEY_AUTHOR);
        // Order posts by newest first
        query.addDescendingOrder("createdAt");
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