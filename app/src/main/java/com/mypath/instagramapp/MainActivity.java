package com.mypath.instagramapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mypath.instagramapp.fragments.ComposeFragment;

import com.mypath.instagramapp.fragments.PostFragment;
import com.mypath.instagramapp.fragments.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // do stuff with the user

            BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);

            bottom_navigation.setOnItemSelectedListener(item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:

                        fragment = new PostFragment();
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            });
            bottom_navigation.setSelectedItemId(R.id.action_home);
        } else {
            // show the signup or login screen
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}