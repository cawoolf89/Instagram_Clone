package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Msg", "Trail push");
        Log.i("Msg", "Trail push");
        Log.i("Msg", "This is the master branch");
        Log.i("Msg", "This is the merge from the master into the working branch");
        //You are still in the working branch.
    }
}
