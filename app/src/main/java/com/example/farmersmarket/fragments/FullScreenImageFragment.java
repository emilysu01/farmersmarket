package com.example.farmersmarket.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.parse.ParseFile;

public class FullScreenImageFragment extends Fragment {

    private ParseFile imageToDisplay;

    // UI components
    private ImageView ivImage;

    // Required empty public constructor
    public FullScreenImageFragment() {

    }

    public FullScreenImageFragment(ParseFile imageToDisplay) {
        this.imageToDisplay = imageToDisplay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        ivImage = view.findViewById(R.id.ivImage);

        // Display UI
        Glide.with(getContext())
                .load(imageToDisplay.getUrl())
                .into(ivImage);
    }
}