package com.example.instagramclone;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {


    private EditText mCodeNameInput, mDOBInput, mFelonyInput, mPoliticalInput;
    private Button mSubmit, mGetUserInfo, mClearText, mLogOut;

    public ProfileTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false); //This view is the main view that everything in the fragment interacts with. Similar to the setContentView in regular activities.

        mCodeNameInput = view.findViewById(R.id.profileCodeNameInput_EditText);
        mDOBInput = view.findViewById(R.id.profileDOBInput_EditText);
        mFelonyInput = view.findViewById(R.id.profileFelonyInput_EditText);
        mPoliticalInput = view.findViewById(R.id.profilePoliticalInput_EditText);

        mSubmit = view.findViewById(R.id.profileSubmit_Button);
        mGetUserInfo = view.findViewById(R.id.profileGet_Button);
        mClearText = view.findViewById(R.id.profileClear_Button);
        mLogOut = view.findViewById(R.id.profileLogOut_Button);

        final ParseUser parseUser = ParseUser.getCurrentUser(); //Since we logged in here from the other page. Gets the current user and then passes more info into that profile.

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateUserInfo(parseUser);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show(); //Need to call getContext() when your getting the context of a fragment.
                }
            }
        });

        mGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getUserInfo(parseUser);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputFields();
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Intent logOut = new Intent(getContext(), LoginActivity.class);
                startActivity(logOut);
            }
        });

        return view;
    }

    private void updateUserInfo(@NonNull ParseUser parseUser) {
        String name = mCodeNameInput.getText().toString();
        String dob = mDOBInput.getText().toString();
        String felony = mFelonyInput.getText().toString();
        String politic = mPoliticalInput.getText().toString();

        boolean emptyInput = emptyStringCheck(name,dob,felony,politic);

        if(emptyInput == true) {
            Toast.makeText(getContext(),"All fields must have an input", Toast.LENGTH_SHORT).show();
            return; //Prevents the user from inputting empty data.
        }

        confirmInfoUpdate(parseUser,name, dob, felony, politic);

    }

    private void confirmInfoUpdate(@NonNull final ParseUser parseUser, final String name, final String dob, final String felony, final String politic) {

        final AlertDialog.Builder confirmAlert = new AlertDialog.Builder(getContext());
        confirmAlert.setCancelable(false);
        confirmAlert.setTitle("Are you sure you want to update the user info?");

        confirmAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performInfoUpdate(parseUser,name,dob,felony,politic);

            }
        });

        confirmAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearInputFields();
                return;
            }
        });

        confirmAlert.show();

    }

    private void performInfoUpdate(@NonNull ParseUser parseUser, String name, String dob, String felony, String politic) {
        parseUser.put("codeName", name);
        parseUser.put("dateOfBirth", dob);
        parseUser.put("felonyCharges", felony);
        parseUser.put("politicalParty", politic);

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getContext(),"Data Upload Success", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                }
                else {
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    clearInputFields();
                }
            }
        });
    }

    private void getUserInfo(@NonNull ParseUser parseUser) {

            mCodeNameInput.setText(parseUser.get("codeName")+"");//Prevents the app from crashing if a null value is returned. Pass .toString() on a null object.
            mDOBInput.setText(parseUser.get("dateOfBirth")+"");
            mFelonyInput.setText(parseUser.get("felonyCharges")+"");
            mPoliticalInput.setText(parseUser.get("politicalParty")+"");
    }

    private void clearInputFields() {
        mCodeNameInput.getText().clear();
        mDOBInput.getText().clear();
        mFelonyInput.getText().clear();
        mPoliticalInput.getText().clear();
    }

    private boolean emptyStringCheck(String name, String dob, String felony, String politic) {

        boolean stringCheck = true;

        String[] inputStrings = {name, dob, felony, politic};

        for(String s : inputStrings) {
            if (s.equals("")) {
                stringCheck = true;
            }

            else {
                stringCheck = false;
            }
        }

        return stringCheck;
    }

}
