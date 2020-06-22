package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**

 */
public class SharePictureTab extends Fragment {

    private ImageView mUserImage;
    private EditText mTextDescripton;
    private Button mShareButton;
    Bitmap receivedImageBitMap;

    public SharePictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        mUserImage = view.findViewById(R.id.sharePic_userImage_ImageView);
        mTextDescripton = view.findViewById(R.id.sharePic_textDescription_EditText);
        mShareButton = view.findViewById(R.id.sharePic_shareImage_Button);

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImageClicked();
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareButtonClicked();
            }
        });


        return view;
    }

    private void shareButtonClicked() {

        if(receivedImageBitMap != null && !mTextDescripton.toString().equals("") ) {

            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Uploading Image..");
            dialog.show();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //Converts the image to a byte stream for ease of uploading over the internet because of its large size.
            receivedImageBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            ParseFile parseFile = new ParseFile("pic.png", bytes);
            ParseObject parseObject = new ParseObject("Photo");
            parseObject.put("picture", parseFile);
            parseObject.put("img_description", mTextDescripton.getText().toString());
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        FancyToast.makeText(getContext(),"Upload Complete!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    }
                    else {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                    dialog.dismiss();
                }
            });
        }
        else {
            FancyToast.makeText(getContext(),"Please select an image and add description", Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
        }


    }

    private void userImageClicked() {

        //Crazy permissions code...

        if(Build.VERSION.SDK_INT >= 23 && //Checks if the operating system of the user device is API 23 or above.. (Anything above this OS API level will ask for runtime permission on its own.. right?).. So this code checks for weather the user has already granted permission through the OS or not.
                ActivityCompat.checkSelfPermission(getContext(), //Basically verifying if the permission has been granted.
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ //If it has not been granted..
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1000); //Ask for it.. It's a String[] for multiple requests. Request code is unique to our specific permission of our fragment?
        }

        //We don't need to ask so we're diving right in and stealing your shit lol.. ;) Or you've already granted permissions.
        else {

            getChosenImage();

        }
    }

    private void getChosenImage() {

        FancyToast.makeText(getContext(),"Accessing the images...", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, 2000); //The intent wants an image as the result.


    }

    @Override //This method is called when the user makes an choice on a the Dialog.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Verifies that the permission was granted by the user. Once the permission is granted it modifies the settings of the app so that it always has that permission.
        if(requestCode == 1000) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }

    @Override //Accesses the image. Fragment access is different from activity access.
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK) {

                //Do something with captured image
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(
                            selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close(); //Important to close the cursor. Wastes resources otherwise.

                    receivedImageBitMap = BitmapFactory.decodeFile(picturePath);

                    mUserImage.setImageBitmap(receivedImageBitMap);


                }
                catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }


    }
}
