package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmersmarket.R;
import com.example.farmersmarket.SearchAlgorithm;
import com.example.farmersmarket.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    // UI components
    private EditText etSearchBar;
    private ImageButton btnSearch;
    private RecyclerView rvSearchResults;

    private static ArrayList<Listing> allListings = new ArrayList<>();
    private static ShortListingsAdapter adapter;

    // Required empty public constructor
    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        etSearchBar = view.findViewById(R.id.etSearchBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);

        // Configure adapter
        adapter = new ShortListingsAdapter(getContext(), allListings);
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Set onClickListener for search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Error checking
                String searchStr = etSearchBar.getText().toString();
                if (searchStr.isEmpty()) {
                    Toast.makeText(getContext(), "Search can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                search(searchStr);
            }
        });
    }

    private void search(String search) {
        SearchAlgorithm.search(search, allListings, adapter);
    }
}