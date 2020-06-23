package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Spy Co: " + ParseUser.getCurrentUser().getUsername());

        mToolbar = findViewById(R.id.toolbar_layout_ID);
        setSupportActionBar(mToolbar);

        mViewPager = findViewById(R.id.mySocialMediaViewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);

        mTabLayout = findViewById(R.id.mySocialMediaTabLayout);
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override //Performs actions based on menu item.
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Check for and grant permissions. Starts action for uploading image based on camera icon.
        if(item.getItemId() == R.id.postImage_MenuItem) {
            if(Build.VERSION.SDK_INT >= 23 && //Checks if the operating system of the user device is API 23 or above.. (Anything above this OS API level will ask for runtime permission on its own.. right?).. So this code checks for weather the user has already granted permission through the OS or not.
                    ActivityCompat.checkSelfPermission(SocialMediaActivity.this, //Basically verifying if the permission has been granted.
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ //If it has not been granted..
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},3000); //Ask for it.. It's a String[] for multiple requests. Request code is unique to our specific permission of our fragment? Yes Request codes are unique values. 
            }

            //We don't need to ask so we're diving right in and stealing your shit lol.. ;) Or you've already granted permissions.
            else {

                captureImage();
            }

        }

        //Performs logout from menu item.
        else if(item.getItemId() == R.id.logOutUser_MenuItem) {
            ParseUser.getCurrentUser().logOut();
            ParseUser.getCurrentUser().logOut();
            Intent logInScreen = new Intent(SocialMediaActivity.this, SignUpActivity.class);
            startActivity(logInScreen);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override //Verifies permission setting and starts method for selecting and uploading image.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000) {
            if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }
    }


    //Passes the intents and address for finding the image.
    private void captureImage() {

        Intent captureImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //This is the actual code the access the gallery.
        startActivityForResult(captureImageIntent, 4000); //Unique requestCode here
    }

    @Override //Access the camera, captures an image, and uploads it to the server.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000 && resultCode == RESULT_OK) {

            try {

                final ProgressDialog dialog = new ProgressDialog(SocialMediaActivity.this);
                dialog.setMessage("Uploading Image..");
                dialog.show();

                //Converts image into BitMap
                Uri capturedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                //Uploads image to server.
                ParseFile parseFile = new ParseFile("myimage.png",bytes); //File the holds the picture
                ParseObject parseObject = new ParseObject("Photo"); //Class that the picture goes in, and picture object.
                parseObject.put("picture", parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            FancyToast.makeText(SocialMediaActivity.this,"Upload Complete!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(SocialMediaActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });

            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SocialMediaActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
