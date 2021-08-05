package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmersmarket.R;
import com.example.farmersmarket.SearchAlgorithm;
import com.example.farmersmarket.adapters.ShortListingsAdapter;
import com.example.farmersmarket.models.Listing;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    // UI components
    private EditText etSearchBar;
    private ImageButton btnSearch;
    private Spinner spSort;
    private Spinner spFilter;
    private RecyclerView rvSearchResults;

    // Listings data structure and adapter
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
        spSort = view.findViewById(R.id.spSort);
        spFilter = view.findViewById(R.id.spFilter);

        // Configure adapter
        adapter = new ShortListingsAdapter(getContext(), allListings);
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Configure spinners
        final String[] sortOptionSelected = new String[1];
        ArrayList<String> sortOptions = new ArrayList<String>();
        sortOptions.add("distance");
        sortOptions.add("price");
        sortOptions.add("units");
        ArrayAdapter<String> sortedSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortOptions) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        sortedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(sortedSpinnerAdapter);

        final String[] filterOptionsSelected = new String[1];
        ArrayList<String> filterOptions = new ArrayList<String>();
        filterOptions.add("delivery");
        filterOptions.add("alt");
        ArrayAdapter<String> filterSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filterOptions) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(filterSpinnerAdapter);

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

                // Search for listings using search algorithm
                SearchAlgorithm.search(searchStr, allListings, adapter, spSort, spFilter);
            }
        });
    }
}