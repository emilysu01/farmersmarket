package com.example.farmersmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder> {

    private Context context;
    private List<Listing> listings;

    public ListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
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
            Glide.with(context)
                    .load(listing.getAuthor().getParseFile(User.KEY_PROFILE_PIC).getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
            tvUsername.setText(listing.getAuthor().getUsername());
            tvName.setText(listing.getAuthor().getString(User.KEY_NAME));
            tvDescription.setText(listing.getDescription());
            Glide.with(context)
                    .load(listing.getImage().getUrl())
                    .into(ivListingPic);
        }
    }
}
