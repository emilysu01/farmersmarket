package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.farmersmarket.R;

public class SearchFragment extends Fragment {

    // UI components
    private EditText etSearchBar;
    private ImageButton btnSearch;

    // Required empty public constructor
    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        etSearchBar = view.findViewById(R.id.etSearchBar);
        btnSearch = view.findViewById(R.id.btnSearch);

        // Set onClickListener
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Error checking
                String search = etSearchBar.getText().toString();
                if (search.isEmpty()) {
                    Toast.makeText(getContext(), "Search can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                search(search);
            }
        });
    }

    // TODO: Implement searching algorithm
    private void search(String search) {
        Toast.makeText(getContext(), "Searching", Toast.LENGTH_SHORT).show();
    }
}