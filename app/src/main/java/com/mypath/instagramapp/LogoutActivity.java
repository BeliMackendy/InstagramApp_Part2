package com.mypath.instagramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.parse.ParseUser;

public class LogoutActivity extends AppCompatActivity {
    public static final String TAG="LogoutActivity";
    private Button btLogout;

    private ImageButton ibPost;
    private ImageButton ibHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btLogout = findViewById(R.id.btLogout);

        ibHome = findViewById(R.id.ibHome);
        ibPost = findViewById(R.id.ibPost);

        ibPost.setOnClickListener(view -> {
            Intent i = new Intent(LogoutActivity.this,PostActivity.class);
            startActivity(i);
//                finish();
        });

        ibHome.setOnClickListener(view -> {
            Intent i = new Intent(LogoutActivity.this,MainActivity.class);
            startActivity(i);
//                finish();
        });

        btLogout.setOnClickListener(view -> {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent i = new Intent(LogoutActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
}