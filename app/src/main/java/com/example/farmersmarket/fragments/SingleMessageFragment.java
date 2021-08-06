package com.example.farmersmarket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.farmersmarket.R;
import com.example.farmersmarket.adapters.SingleMessageAdapter;
import com.example.farmersmarket.models.Conversation;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SingleMessageFragment extends Fragment {

    // Tag for logging statements
    public static final String TAG = "MessageFragment";

    public static final int MAX_MESSAGES_TO_SHOW = 50;

    // UI components
    private RecyclerView rvMessages;
    private EditText etMessaage;
    private ImageButton imgBtnSend;

    // Adapter components
    private ArrayList<Message> allMessages;
    private boolean firstLoad;
    private SingleMessageAdapter adapter;

    private Conversation thisConvo;

    // Required empty public constructor
    public SingleMessageFragment() {

    }

    public SingleMessageFragment(Conversation conversation) {
        this.thisConvo = conversation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        refreshMessages();

        // Retrieve UI components
        rvMessages = view.findViewById(R.id.rvMessages);
        etMessaage = view.findViewById(R.id.etMessage);
        imgBtnSend = view.findViewById(R.id.imgBtnSend);

        final Animation buttonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button);

        // Set onClickListener for send button
        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBtnSend.startAnimation(buttonAnimation);

                // Retrieve typed text
                String typedText = etMessaage.getText().toString();

                // Error checking
                if (typedText.isEmpty()) {
                    Toast.makeText(getContext(), "Message cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(typedText);
            }
        });

        // Configure adapter
        allMessages = new ArrayList<Message>();
        firstLoad = true;
        adapter = new SingleMessageAdapter(getActivity(), ParseUser.getCurrentUser().getObjectId(), allMessages);
        rvMessages.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMessages.setLayoutManager(linearLayoutManager);
    }

    private void sendMessage(String message) {
        // Create new message
        Message parseMessage = new Message();
        parseMessage.put(Message.KEY_SENDER, ParseUser.getCurrentUser());
        if (thisConvo.getPerson1().equals(ParseUser.getCurrentUser())) {
            parseMessage.put(Message.KEY_RECIPIENT, thisConvo.getPerson2());
        } else {
            parseMessage.put(Message.KEY_RECIPIENT, thisConvo.getPerson2());
        }
        parseMessage.put(Message.KEY_MESSAGE, message);

        parseMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error while sending message. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error while sending message", e);
                }
                Log.i(TAG, "Message sent successfully!");

                thisConvo.setLatestMessage(parseMessage);
                thisConvo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving latest message", e);
                        }
                    }
                });

                // Reset UI after saving
                etMessaage.setText("");

                refreshMessages();
            }
        });
    }

    private void refreshMessages() {
        ParseUser user1 = thisConvo.getPerson1();
        ParseUser user2 = thisConvo.getPerson2();

        ParseQuery<Message> user1ToUser2 = ParseQuery.getQuery("Message");
        user1ToUser2.whereEqualTo(Message.KEY_SENDER, user1);
        user1ToUser2.whereEqualTo(Message.KEY_RECIPIENT, user2);

        ParseQuery<Message> user2ToUser1 = ParseQuery.getQuery("Message");
        user2ToUser1.whereEqualTo(Message.KEY_SENDER, user2);
        user2ToUser1.whereEqualTo(Message.KEY_RECIPIENT, user1);

        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        queries.add(user1ToUser2);
        queries.add(user2ToUser1);

        ParseQuery<Message> finalQuery = ParseQuery.or(queries);
        // ParseQuery<Message> finalQuery = user1ToUser2;
        finalQuery.include(Message.KEY_SENDER);
        finalQuery.include(Message.KEY_RECIPIENT);
        finalQuery.setLimit(MAX_MESSAGES_TO_SHOW);
        finalQuery.orderByAscending("createdAt");

        finalQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with retrieving messages", e);
                    return;
                }
                Log.i("USER", messages.toString());

                allMessages.clear();
                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();
                rvMessages.scrollToPosition(allMessages.size() - 1);
            }
        });
    }
}