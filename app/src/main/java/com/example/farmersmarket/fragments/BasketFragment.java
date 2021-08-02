package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmersmarket.R;
import com.example.farmersmarket.adapters.BasketAdapter;
import com.example.farmersmarket.adapters.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    public static final String TAG = "BasketFragment";
    // public ArrayList<Listing> basket;

    private List<Listing> basketListings = new ArrayList<Listing>();
    private BasketAdapter adapter;

    private RecyclerView rvBasket;

    // Required empty public constructor
    public BasketFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        rvBasket = view.findViewById(R.id.rvBasket);

        // Configure adapter
        adapter = new BasketAdapter(getContext(), basketListings);
        rvBasket.setAdapter(adapter);
        rvBasket.setLayoutManager(new LinearLayoutManager(getContext()));

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("basket");
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // Check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                basketListings.addAll(users.get(0).getList("basket"));
                adapter.notifyDataSetChanged();
            }
        });
    }
}