package com.example.farmersmarket.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.DoubleClickListener;
import com.example.farmersmarket.R;
import com.example.farmersmarket.fragments.DetailedListingFragment;
import com.example.farmersmarket.fragments.ProfileFragment;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeListingsAdapter extends RecyclerView.Adapter<HomeListingsAdapter.ViewHolder> {

    // Tag for logging statements
    public static final String TAG = "HomeListingsAdapter";

    // Context (passed in from constructor)
    private Context context;

    // Listings data structure
    private List<Listing> allListings;

    // Fragment manager for navigating to detailed listing pages
    private FragmentManager fragmentManager;

    public HomeListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.allListings = listings;
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and attach it to a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListingsAdapter.ViewHolder holder, int position) {
        // Find listing and bind it to the view
        Listing listing = allListings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return allListings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // UI components
        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvName;
        private ImageView ivListingPic;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Retrieve UI components
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            ivListingPic = itemView.findViewById(R.id.ivListingPic);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Listing listing) {
            // Display UI
            Glide.with(context)
                    .load(listing.getAuthor().getParseFile(User.KEY_PROFILE_PIC).getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
            tvUsername.setText("@" + listing.getAuthor().getString(User.KEY_USERNAME));
            tvName.setText(listing.getAuthor().getString(User.KEY_NAME));
            tvDescription.setText(listing.getDescription());
            Glide.with(context)
                    .load(listing.getImages().get(0).getUrl())
                    .into(ivListingPic);

            // Set onClickListeners
            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = allListings.get(position);
                        goToProfileScreen(listing);
                    }
                }
            });
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = allListings.get(position);
                        goToProfileScreen(listing);
                    }
                }
            });
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = allListings.get(position);
                        goToProfileScreen(listing);
                    }
                }
            });
            ivListingPic.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onDoubleClick() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = allListings.get(position);
                        addToBasket(listing);
                    }
                }
            });
            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = allListings.get(position);
                        goToDetailedListingScreen(listing);
                    }
                }
            });
        }
    }

    private void addToBasket(Listing listing) {
        // Retrieve old basket, add listing to old basket, and update basket in database
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo(User.KEY_OBJECT_ID, currentUser.getObjectId());
        query.include(User.KEY_BASKET);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with retrieving basket", e);
                    return;
                }
                List<Listing> currentBasket = parseUser.getList(User.KEY_BASKET);
                for (Listing basketListing : currentBasket) {
                    if (basketListing.getObjectId().equals(listing.getObjectId())) {
                        Toast.makeText(context, "This listing is already in your basket!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                currentBasket.add(0, listing);
                currentUser.put(User.KEY_BASKET, currentBasket);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(context, "There was an issue adding to basket. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Issue with adding to basket", e);
                            return;
                        }
                        Toast.makeText(context, "Listing added to basket successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void goToProfileScreen(Listing listing) {
        Fragment fragment = new ProfileFragment(listing.getAuthor());
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void goToDetailedListingScreen(Listing listing) {
        Fragment fragment = new DetailedListingFragment(listing);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}

