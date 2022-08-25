package com.mypath.instagramapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mypath.instagramapp.Post;
import com.mypath.instagramapp.PostAdapter;
import com.mypath.instagramapp.R;
//import com.parse.FindCallback;
//import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    protected SwipeRefreshLayout swipeContainer;

    public static final String TAG = "PostFragment";

   protected PostAdapter adapter;

   protected List<Post> allposts;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvPosts = view.findViewById(R.id.rvPost);

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer);


        allposts = new ArrayList<>();
        adapter = new PostAdapter(allposts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        // Setup refresh listener which triggers new data loading

        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        swipeContainer.setOnRefreshListener(this::queryPosts);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);

        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting Posts", e);
                return;
            }
            for (Post post : posts) {
                Log.i(TAG, "Posts: " + post.getDescription()+" Username: "+post.getUser().getUsername());
            }

//            allposts.addAll(posts);
//            adapter.notifyDataSetChanged();

            // Remember to CLEAR OUT old items before appending in the new ones
            adapter.clear();

            // ...the data has come back, add new items to your adapter...
            adapter.addAll(posts);

            // Now we call setRefreshing(false) to signal refresh has finished
            swipeContainer.setRefreshing(false);
        });
    }
}