//You are in the working branch.

package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private Button mSignUpLoginButton, mStudentDataButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mainToolbar);

        mSignUpLoginButton = findViewById(R.id.signUpLogIn_Button);
        mStudentDataButton = findViewById(R.id.studentData_Button);

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Loading..");

        mSignUpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpLoginIntent = new Intent(MainActivity.this, SignUpLoginActivity.class);
                startActivity(signUpLoginIntent);
                mProgressDialog.show();

            }
        });

        mStudentDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentDataIntent = new Intent(MainActivity.this, StudentDataActivity.class);
                startActivity(studentDataIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog.dismiss();
    }
}


