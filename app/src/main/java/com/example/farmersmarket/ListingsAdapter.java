package com.example.farmersmarket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.fragments.DetailedListingFragment;
import com.example.farmersmarket.models.Listing;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder> {

    private Context context;
    private List<Listing> listings;

    private FragmentManager fragmentManager;

    public ListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and attach it to a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsAdapter.ViewHolder holder, int position) {
        // Find listing and bind it to the view
        Listing listing = listings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listings.size();
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
            tvUsername = itemView.findViewById(R.id.tvName);
            tvName = itemView.findViewById(R.id.tvName);
            ivListingPic = itemView.findViewById(R.id.ivListingPic);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }

        public void bind(Listing listing) {
            // Display UI components
            Log.i("ListingsAdapter", listing.getDescription());
            /* Glide.with(context)
                    .load(listing.getAuthor().getProfilePic())
                    .circleCrop()
                    .into(ivProfilePic); */
            tvUsername.setText(listing.getAuthor().getUsername());
            tvName.setText(listing.getAuthor().getName());
            tvDescription.setText(listing.getDescription());
            // Log.i("ListingsAdapter", listing.getImages().toString());
            Glide.with(context)
                    .load(listing.getImages().get(0))
                    .into(ivListingPic);

            // Set onClickListeners
            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = listings.get(position);
                        goToProfileScreen(listing);
                    }
                }
            });
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = listings.get(position);
                        goToProfileScreen(listing);
                    }
                }
            });
            ivListingPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = listings.get(position);
                        goToDetailedListingScreen(listing);
                    }
                }
            });
            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = listings.get(position);
                        goToDetailedListingScreen(listing);
                    }
                }
            });
        }
    }

    private void goToProfileScreen(Listing listing) {
        // Fragment fragment = new ProfileFragment(listing.getAuthor());
        //fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void goToDetailedListingScreen(Listing listing) {
        Fragment fragment = new DetailedListingFragment(listing);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}

