package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SignUpLoginActivity extends AppCompatActivity {

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

        if(ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut(); //Logs out the current user if there is one, so they have to log back in after the app is closed.
        }

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser appUser = new ParseUser();

                final String userName = mUserName.getText().toString();
                final String userPassword = mUserPassword.getText().toString();
                final String userEmail = mUserEmail.getText().toString();

                if (userName.equals("") | userPassword.equals("") | userEmail.equals("")) {

                    makeText(SignUpLoginActivity.this,"Please enter a value for all fields.", LENGTH_SHORT).show();
                }

                else {

                    appUser.setUsername(userName);
                    appUser.setPassword(userPassword);
                    appUser.setEmail(userEmail);

                    final ProgressDialog progressDialog = new ProgressDialog(SignUpLoginActivity.this); //Deprecated?
                    progressDialog.setMessage("Signing up " + mUserName.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpLoginActivity.this,"Sign Up Successful! Welcome " + userName, Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                                clearEditTextViews();
                                progressDialog.dismiss();
                            } else {
                                makeText(SignUpLoginActivity.this, e.getMessage(), LENGTH_LONG).show();
                                clearEditTextViews();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startLogin = new Intent(SignUpLoginActivity.this, LoginActivity.class);
                startActivity(startLogin);
            }
        });

    }

    private void clearEditTextViews() {
        mUserName.getText().clear();
        mUserPassword.getText().clear();
        mUserEmail.getText().clear();
    }
}
