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
import com.parse.ParseUser;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder> {

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LISTINGS = "listings";

    private Context context;
    private List<Listing> listings;

    public ListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsAdapter.ViewHolder holder, int position) {
        Listing listing = listings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvName;
        private ImageView ivListingPic;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            ivListingPic = itemView.findViewById(R.id.ivListingPic);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }

        public void bind(Listing listing) {
            Glide.with(context)
                    .load(listing.getKeyAuthor().getParseFile(KEY_PROFILE_PIC).getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
            tvUsername.setText(listing.getKeyAuthor().getUsername());
            tvName.setText(listing.getKeyAuthor().getString(KEY_NAME));
            tvDescription.setText(listing.getKeyDescription());
            Glide.with(context)
                    .load(listing.getKeyImage().getUrl())
                    .into(ivListingPic);
        }
    }
}
