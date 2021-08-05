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
import com.example.farmersmarket.fragments.SingleMessageFragment;
import com.example.farmersmarket.models.Conversation;
import com.example.farmersmarket.models.Message;
import com.example.farmersmarket.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AllMessagesAdapter extends RecyclerView.Adapter<AllMessagesAdapter.ViewHolder> {

    // Tag for logging statements
    public static final String TAG = "AllMessagesAdapter";

    private Context context;
    private ArrayList<Conversation> conversations;
    private FragmentManager fragmentManager;

    public AllMessagesAdapter(Context context, ArrayList<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_message, parent, false);
        return new AllMessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMessagesAdapter.ViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfilePic;
        private TextView tvName;
        private TextView tvMessagePreview;

        private View.OnClickListener messageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Conversation conversation = conversations.get(position);
                    goToMessage(conversation);
                }
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Retrieve UI components
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessagePreview = itemView.findViewById(R.id.tvMessagePreview);
        }

        public void bind(Conversation conversation) {
            ParseQuery<Message> messageQuery = ParseQuery.getQuery(Message.class);
            messageQuery.whereEqualTo(Message.KEY_OBJECT_ID, conversation.getLatestMessage().getObjectId());
            messageQuery.include(Message.KEY_SENDER);
            messageQuery.include(Message.KEY_RECIPIENT);
            messageQuery.getFirstInBackground(new GetCallback<Message>() {
                @Override
                public void done(Message message, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error with retrieving all message", e);
                        return;
                    }
                    Glide.with(context)
                            .load(message.getParseUser(Message.KEY_SENDER).getParseFile(User.KEY_PROFILE_PIC).getUrl())
                            .into(ivProfilePic);
                    tvName.setText(message.getParseUser(Message.KEY_SENDER).getString(User.KEY_NAME));
                    tvMessagePreview.setText(message.getMessage());

                    ivProfilePic.setOnClickListener(messageClickListener);
                    tvName.setOnClickListener(messageClickListener);
                    tvMessagePreview.setOnClickListener(messageClickListener);
                }
            });
        }
    }

    private void goToMessage(Conversation conversation) {
        Fragment fragment = new SingleMessageFragment(conversation);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}
