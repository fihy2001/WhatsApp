package com.example.whatsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    public void onCreate() {

        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Fygo83LY3SaAiWR4kUBTkJoQnbiNGPgIyr1lYVqP")
                // if defined
                .clientKey("qP80GAWHtRjpEMp6FpTzctjDTVcNDSzJGbYBV1Oi")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }

}
