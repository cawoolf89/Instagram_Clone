package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity {

    private EditText mUserName, mUserNameLogin, mUserPassword, mUserPasswordLogin;
    private Button mSignUp, mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        mUserName = findViewById(R.id.signUp_userName);
        mUserPassword = findViewById(R.id.signUp_userPassword);
        mUserNameLogin = findViewById(R.id.logIn_UserName);
        mUserPasswordLogin = findViewById(R.id.logIn_Password);

        mSignUp = findViewById(R.id.signUp_Button);
        mLogin = findViewById(R.id.logIn_Button);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser appUser = new ParseUser();
                appUser.setUsername(mUserName.getText().toString());
                appUser.setPassword(mUserPassword.getText().toString());

                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            FancyToast.makeText(SignUpLoginActivity.this, "Sign Up Success! Welcome " +  appUser.get("username"), Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(SignUpLoginActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(mUserNameLogin.getText().toString(), mUserPasswordLogin.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            FancyToast.makeText(SignUpLoginActivity.this, "Login Success! Welcome " + user.get("username"), Toast.LENGTH_SHORT).show();

                            Intent welcomeActivity = new Intent(SignUpLoginActivity.this, WelcomeActivity.class);
                            startActivity(welcomeActivity);
                        }

                        else {
                            Toast.makeText(SignUpLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
