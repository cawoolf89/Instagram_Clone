package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String codeName = (String) currentUser.get("codeName");
        setTitle("Spy Co: " + codeName);

        mToolbar = findViewById(R.id.toolbar_layout_ID);
        setSupportActionBar(mToolbar);

        mViewPager = findViewById(R.id.mySocialMediaViewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);

        mTabLayout = findViewById(R.id.mySocialMediaTabLayout);
        mTabLayout.setupWithViewPager(mViewPager, false);
    }
}
