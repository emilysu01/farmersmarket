package com.example.farmersmarket.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.R;
import com.example.farmersmarket.models.Message;

import java.util.List;

public class SingleMessageAdapter extends RecyclerView.Adapter<SingleMessageAdapter.MessageViewHolder> {

    public static final String TAG = "ChatAdapter";

    public static final int MESSAGE_INCOMING = 0;
    public static final int MESSAGE_OUTGOING = 1;

    private List<Message> allMessages;
    private Context context;
    private String userId;

    public SingleMessageAdapter(Context context, String userId, List<Message> messages) {
        this.allMessages = messages;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.incoming_message, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.outgoing_message, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMessageAdapter.MessageViewHolder holder, int position) {
        Message message = allMessages.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message is incoming or outgoing
        Message message = allMessages.get(position);
        String senderId = message.getSenderId();
        if (senderId != null && !senderId.equals(this.userId)) {
            return MESSAGE_INCOMING;
        }
        return MESSAGE_OUTGOING;
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        private ImageView ivOtherProfilePic;
        private TextView tvBody;
        private TextView tvName;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            ivOtherProfilePic = itemView.findViewById(R.id.ivOtherProfilePic);
            tvBody = itemView.findViewById(R.id.tvMessage);
            tvName = itemView.findViewById(R.id.tvName);
        }

        @Override
        void bindMessage(Message message) {
            Glide.with(context)
                    .load(message.getSender().getProfilePic().getUrl())
                    .circleCrop()
                    .into(ivOtherProfilePic);
            tvBody.setText(message.getMessage());
            tvName.setText(message.getSender().getName());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        private ImageView ivMyProfilePic;
        private TextView tvBody;
        // private TextView tvName;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            ivMyProfilePic = itemView.findViewById(R.id.ivMyProfilePic);
            tvBody = itemView.findViewById(R.id.tvMessage);
            // tvName = itemView.findViewById(R.id.tvName);
        }

        @Override
        void bindMessage(Message message) {
            Log.i(TAG, "OUTGOING MESSAGE");
            Log.i("SENDER", message.getSender().toString());
            Glide.with(context)
                    .load(message.getSender().getProfilePic().getUrl())
                    .circleCrop()
                    .into(ivMyProfilePic);
            tvBody.setText(message.getMessage());
            Log.i(TAG,message.getSender().getName());
            // tvName.setText(message.getSender().getName());
        }
    }
}