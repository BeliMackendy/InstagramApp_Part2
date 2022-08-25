package com.mypath.instagramapp.fragments;

import android.annotation.SuppressLint;
import android.util.Log;

import com.mypath.instagramapp.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileFragment extends PostFragment{
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
            allposts.addAll(posts);
            adapter.notifyDataSetChanged();
        });
    }
}
