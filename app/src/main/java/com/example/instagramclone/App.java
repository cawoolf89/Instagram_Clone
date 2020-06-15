package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("U7DMuWG6D1jLDXF3IUQYYC16BWPrcAX3AShEdqHk")
                // if defined
                .clientKey("ZNO5oPJHJN5MeXMpncis6YPjvVNWO1vavAEvx2vn")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
