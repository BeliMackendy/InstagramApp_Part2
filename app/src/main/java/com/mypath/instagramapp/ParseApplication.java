package com.mypath.instagramapp;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ozMuRgycVKYd9fbXq3FXlRjepjfxTRnV9Hay2sC2")
                .clientKey("3hXjQkIkdyo21yBEI1RoBx34bEts9CvMZUx8nmY9")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
