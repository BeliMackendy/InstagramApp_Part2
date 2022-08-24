package com.mypath.instagramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.parse.FindCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.Objects;
//import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ImageView ivPostUser;
    private TextView tvDescription;
    private RelativeLayout rlViewpost;
    private BottomNavigationView bottom_navigation;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // do stuff with the user

            ivPostUser = findViewById(R.id.ivPostUser);
            tvDescription = findViewById(R.id.tvDescription);
            rlViewpost = findViewById(R.id.rlViewpost);
            bottom_navigation = findViewById(R.id.bottom_navigation);

            bottom_navigation.setOnItemSelectedListener(item -> {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_compose:
                        Toast.makeText(MainActivity.this, "Compose", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profile:
                    default:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            });
            bottom_navigation.setSelectedItemId(R.id.action_home);

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