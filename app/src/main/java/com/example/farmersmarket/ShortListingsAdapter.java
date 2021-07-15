package com.example.farmersmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.fragments.DetailedListingFragment;
import com.example.farmersmarket.fragments.ProfileFragment;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;

import java.util.List;

public class ShortListingsAdapter extends RecyclerView.Adapter<ShortListingsAdapter.ViewHolder> {

    private Context context;
    private List<Listing> listings;

    private FragmentManager fragmentManager;

    public ShortListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ShortListingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and attach it to a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_short_listing, parent, false);
        return new ShortListingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortListingsAdapter.ViewHolder holder, int position) {
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
        private ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Retrieve UI components
            ivPicture = itemView.findViewById(R.id.ivPicture);
        }

        public void bind(Listing listing) {
            // Display UI components
            Glide.with(context)
                    .load(listing.getImage().getUrl())
                    .into(ivPicture);
            ivPicture.setOnClickListener(new View.OnClickListener() {
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

    private void goToDetailedListingScreen(Listing listing) {
        Fragment fragment = new DetailedListingFragment(listing);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

}
