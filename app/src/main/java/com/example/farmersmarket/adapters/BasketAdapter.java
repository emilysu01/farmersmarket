package com.example.farmersmarket.adapters;

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
import com.example.farmersmarket.R;
import com.example.farmersmarket.fragments.DetailedListingFragment;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    public static final String TAG = "BasketAdapter";
    private Context context;
    private List<Listing> basketListings;

    private FragmentManager fragmentManager;

    public BasketAdapter(Context context, List<Listing> basketListings) {
        this.context = context;
        this.basketListings = basketListings;
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    public Context getContext() {
        return this.context;
    }
    @NonNull
    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and attach it to a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket_listing, parent, false);
        return new BasketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {
        // Find listing and bind it to the view
        Listing listing = basketListings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return basketListings.size();
    }

    public void deleteItem(int position) {
    }

    public void deleteTask(int position) {
        Listing listingToDelete = basketListings.get(position);
        basketListings.remove(position);
        notifyItemRemoved(position);
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(User.KEY_BASKET);
        query.whereEqualTo(User.KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                parseUser.put(User.KEY_BASKET, basketListings);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issue with saving posts", e);
                            return;
                        }
                    }
                });
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // UI components
        private ImageView ivListingImage;
        private TextView tvListingTitle;
        private TextView tvUsername;
        private TextView tvName;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Retrieve UI components
            ivListingImage = itemView.findViewById(R.id.ivListingImage);
            tvListingTitle = itemView.findViewById(R.id.tvListingTitle);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Listing listing) {
            // Display UI
            try {
                Glide.with(context)
                        .load(listing.getImages().get(0).getUrl())
                        .into(ivListingImage);
                String category = listing.getCategory();
                tvListingTitle.setText(category.substring(0,1).toUpperCase() + category.substring(1).toLowerCase());
                tvUsername.setText("@" + listing.getParseUser(Listing.KEY_AUTHOR).fetchIfNeeded().getString(User.KEY_USERNAME));
                tvName.setText(listing.getParseUser(Listing.KEY_AUTHOR).fetchIfNeeded().getString(User.KEY_NAME));
                tvDescription.setText(listing.getDescription());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            // Set onClickListener
            ivListingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = basketListings.get(position);
                        goToDetailedListingScreen(listing);
                    }
                }
            });
            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = basketListings.get(position);
                        goToDetailedListingScreen(listing);
                    }
                }
            });
            tvListingTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing listing = basketListings.get(position);
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
