package com.mypath.instagramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.Objects;
//import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public ImageButton ibUser;
    private ImageView ivPostUser;
    private TextView tvDescription;
    private RelativeLayout rlViewpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // do stuff with the user
            ibUser = findViewById(R.id.ibUser);
            ImageButton ibPost = findViewById(R.id.ibPost);
            ivPostUser = findViewById(R.id.ivPostUser);
            tvDescription = findViewById(R.id.tvDescription);
            rlViewpost = findViewById(R.id.rlViewpost);

            ibUser.setOnClickListener(view -> {
                Intent i = new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(i);
//                finish();
            });

            ibPost.setOnClickListener(view -> {
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                startActivity(i);
//                finish();
            });

            queryPosts();
        } else {
            // show the signup or login screen
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }


    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        Log.i(TAG, "queryPosts: ");
        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting Post", e);
                return;
            }
            Log.i(TAG, "Size: " + posts.size());

                int ind = -1;
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getUser().getObjectId().contentEquals(ParseUser.getCurrentUser().getObjectId())) {
                        Post post = posts.get(i);
                        ind = i;
                        Log.i(TAG, "Post: " + post.getDescription());
                    }
                }
            if (ind != -1) {
                Post post = posts.get(ind);
                Log.i(TAG, "Post user: " + post.getDescription());
                tvDescription.setText(post.getDescription());
                File photoFile = null;
                try {
                    photoFile = post.getImage().getFile();
                } catch (ParseException ev) {
                    ev.printStackTrace();
                }

                Bitmap takenImage = BitmapFactory.decodeFile(Objects.requireNonNull(photoFile).getAbsolutePath());

                Glide.with(MainActivity.this)
                        .load(takenImage)
                        .into(ivPostUser);

                rlViewpost.setVisibility(View.VISIBLE);

            }
        });
    }
}