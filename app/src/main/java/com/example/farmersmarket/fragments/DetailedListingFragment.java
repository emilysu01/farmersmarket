package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class DetailedListingFragment extends Fragment {

    private ImageView ivProfilePic;
    private TextView tvName;
    private ImageView ivListingPic;
    private TextView tvDescription;
    private Button btnContact;

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
        ivListingPic = view.findViewById(R.id.ivListingPic);
        tvDescription = view.findViewById(R.id.tvDescription);
        btnContact = view.findViewById(R.id.btnContact);

        // Set onClickListeners
        ParseUser user = listing.getAuthor();
        Glide.with(getContext())
                .load(user.getParseFile(User.KEY_PROFILE_PIC).getUrl())
                .circleCrop()
                .into(ivProfilePic);
        tvName.setText(user.getString(User.KEY_NAME));
        Glide.with(getContext())
                .load(listing.getImage().getUrl())
                .into(ivListingPic);
        tvDescription.setText(listing.getDescription());
    }
}