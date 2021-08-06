package com.example.farmersmarket.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

        final Animation buttonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button);

        // Configure sort and filter spinners
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        final String[] sortOptionSelected = new String[1];
        ArrayList<String> sortOptions = new ArrayList<String>();
        sortOptions.add("Sort Options");
        sortOptions.add("Distance");
        sortOptions.add("Price");
        sortOptions.add("Units");
        ArrayAdapter<String> sortedSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortOptions) {
            @Override
            public boolean isEnabled(int position){
                if (position == 0) {
                    // Disable the first item from spinner because first item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        sortedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(sortedSpinnerAdapter);
        spSort.setOnItemSelectedListener(listener);

        final String[] filterOptionsSelected = new String[1];
        ArrayList<String> filterOptions = new ArrayList<String>();
        filterOptions.add("Filter Options");
        filterOptions.add("Delivery available");
        filterOptions.add("Under $5");
        filterOptions.add("Under $10");
        filterOptions.add("Under $15");
        ArrayAdapter<String> filterSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filterOptions) {
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    // Disable the first item from spinner because first item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(filterSpinnerAdapter);
        spFilter.setOnItemSelectedListener(listener);

        // Set onClickListener for search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.startAnimation(buttonAnimation);

                // Error checking
                String searchStr = etSearchBar.getText().toString();
                if (searchStr.isEmpty()) {
                    Toast.makeText(getContext(), "Search can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Erase previous search from UI
                if (!allListings.isEmpty()) {
                    allListings.clear();
                    adapter.notifyDataSetChanged();
                }

                // Search for listings using search algorithm
                SearchAlgorithm.search(searchStr, allListings, adapter, spSort, spFilter);
            }
        });
    }
}