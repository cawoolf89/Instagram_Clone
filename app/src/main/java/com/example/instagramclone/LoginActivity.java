package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserNameLogin, mPasswordLogin;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserNameLogin = findViewById(R.id.logIn_UserName_EditText);
        mPasswordLogin = findViewById(R.id.logIn_Password_EditText);
        mLogin = findViewById(R.id.logIn_Button);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    private void userLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this); //Deprecated?
        progressDialog.setMessage("Logging in " + mUserNameLogin.getText().toString());
        progressDialog.show();

        ParseUser.logInInBackground(mUserNameLogin.getText().toString(), mPasswordLogin.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e == null) {
                    FancyToast.makeText(LoginActivity.this, "Welcome! " + user.getUsername().toString(), Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    clearEditTextViews();
                    transitionToSocialMedia();
                    progressDialog.dismiss();
                }

                else {
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    clearEditTextViews();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void clearEditTextViews() {
       mUserNameLogin.getText().clear();
       mPasswordLogin.getText().clear();
    }

    public void rootLayoutTapped(View vew) { //Hides the keyboard when the user clicks outside the keyboard field.

        try { //Prevents app from crashing when the keyboard isnt actually showing.
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMedia() {
        Intent socialMediaIntent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(socialMediaIntent);
    }

}
