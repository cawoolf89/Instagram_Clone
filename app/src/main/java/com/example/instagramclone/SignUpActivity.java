package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import static android.widget.Toast.*;

public class SignUpActivity extends AppCompatActivity {

    private EditText mUserName, mUserPassword, mUserEmail;
    private Button mSignUp, mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        mUserName = findViewById(R.id.signUp_UserName_EditText);
        mUserPassword = findViewById(R.id.signUp_Password_EditText);
        mUserEmail = findViewById(R.id.signUp_Email_EditText);

        mSignUp = findViewById(R.id.signUp_Button);
        mLogin = findViewById(R.id.signUp_logIn_Button);

        //Controls the flow of the program at the SignUp Activity
        if(ParseUser.getCurrentUser() != null) {
              ParseUser.getCurrentUser().logOut(); //Logs out the current user if there is one, so they have to log back in after the app is closed.
      //      transitionToSocialMedia(); //Takes an already signed in user directly to SocialMediaActivity
        }


        //onClick listeners for calling methods because there was a bug setting the onClick function in the design tab? It's not finding methods.
        //Still seems you can just manually write the method in and it will work.
        //This is kind of cool because now the method is private and encapsulated ;)
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(startLogin);
            }
        });

    }

    private void signUpUser() {
        final ParseUser appUser = new ParseUser();

        final String userName = mUserName.getText().toString();
        final String userPassword = mUserPassword.getText().toString();
        final String userEmail = mUserEmail.getText().toString();

        boolean startSignUp  = validateInputFields(userName, userPassword, userEmail);

        if(startSignUp == false) {
            return;
        }

        else {
            appUser.setUsername(userName);
            appUser.setPassword(userPassword);
            appUser.setEmail(userEmail);

            final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this); //Deprecated?
            progressDialog.setMessage("Signing up " + mUserName.getText().toString());
            progressDialog.show();

            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUpActivity.this, "Sign Up Successful! Welcome " + userName, Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        clearEditTextViews();
                        transitionToSocialMedia();
                        progressDialog.dismiss();
                    } else {
                        makeText(SignUpActivity.this, e.getMessage(), LENGTH_LONG).show();
                        clearEditTextViews();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void clearEditTextViews() {
        mUserName.getText().clear();
        mUserPassword.getText().clear();
        mUserEmail.getText().clear();
    }

    private boolean validateInputFields(String userName, String userPassword, String userEmail) {
        boolean validInputs = true;

        if (userEmail.equals("")) {

            makeText(SignUpActivity.this,"Please enter a valid email.", LENGTH_SHORT).show();
            validInputs = false;
        }

        else if (userName.equals("")) {
            makeText(SignUpActivity.this,"Please enter a user name", LENGTH_SHORT).show();
            validInputs = false;
        }

        else if(userPassword.equals("")) {
            makeText(SignUpActivity.this,"Please enter a password.", LENGTH_SHORT).show();
            validInputs = false;
        }

        return validInputs;
    }

    public void rootLayoutTapped(View vew) { //Hides the keyboard when the user clicks outside the keyboard field.

        //Prevents app from crashing when the keyboard isnt actually showing.
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); //Controlls various input methods to the app? Hence it can hide the keyboard.
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMedia() {
        Intent socialMediaIntent = new Intent(SignUpActivity.this, SocialMediaActivity.class);
        startActivity(socialMediaIntent);
    }

    //Resets the text fields for the Sign Up UI
    @Override
    protected void onResume() {
        super.onResume();
        clearEditTextViews();
    }
}
