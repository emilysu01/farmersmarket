package com.example.farmersmarket.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmersmarket.R;
import com.example.farmersmarket.adapters.AllMessagesAdapter;
import com.example.farmersmarket.models.Conversation;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AllMessagesFragment extends Fragment {

    // Tag for logging statements
    public static final String TAG = "AllMessagesFragment";

    // UI components
    private RecyclerView rvAllMessages;

    // Data structure for last messages with the current user
    private ArrayList<Conversation> allConversations = new ArrayList<Conversation>();
    private AllMessagesAdapter adapter;

    // Required empty public constructor
    public AllMessagesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        rvAllMessages = view.findViewById(R.id.rvAllMessages);

        // Configure adapter
        adapter = new AllMessagesAdapter(getContext(), allConversations);
        rvAllMessages.setAdapter(adapter);
        rvAllMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        // Query latest messages
        queryLatestMessages();
    }

    private void queryLatestMessages() {
        ParseQuery<Conversation> person1Query = ParseQuery.getQuery("Conversation");
        person1Query.whereEqualTo(Conversation.KEY_USER_1, ParseUser.getCurrentUser());

        ParseQuery<Conversation> person2Query = ParseQuery.getQuery("Conversation");
        person2Query.whereEqualTo(Conversation.KEY_USER_2, ParseUser.getCurrentUser());

        List<ParseQuery<Conversation>> queries = new ArrayList<ParseQuery<Conversation>>();
        queries.add(person1Query);
        queries.add(person2Query);

        ParseQuery<Conversation> finalQuery = ParseQuery.or(queries);
        finalQuery.include(Conversation.KEY_LATEST_MESSAGE);

        finalQuery.orderByDescending("createdAt");

        person1Query.findInBackground(new FindCallback<Conversation>() {
            @Override
            public void done(List<Conversation> conversations, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with retrieving all conversations", e);
                    return;
                }
                Log.i("ALL CONVOS", conversations.toString());
                allConversations.addAll(conversations);
                adapter.notifyDataSetChanged();
            }
        });
    }
}