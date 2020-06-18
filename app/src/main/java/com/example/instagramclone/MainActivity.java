//You are still in the working branch.


package com.example.instagramclone;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText mGPA, mYear, mMajor, mName;
    private EditText mGetGpa, mGetYear, mGetMajor, mEnterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mainToolbar);

        mName = findViewById(R.id.nameInput_TextView);
        mGPA = findViewById(R.id.gpaInput_TextView);
        mYear = findViewById(R.id.yearInput_TextView);
        mMajor = findViewById(R.id.majorInput_TextView);

        mEnterName = findViewById(R.id.retrieveName_TextView);
        mGetGpa = findViewById(R.id.retrieveGPA_TextView);
        mGetYear = findViewById(R.id.retrieveYear_TextView);
        mGetMajor = findViewById(R.id.retrieveMajor_TextView);

    }

    public void createStudentParseObject(View buttonView) {

        ParseObject profile = new ParseObject("Students"); //Creates a new parse object class

        //Check for empty text fields.
        if(mName.getText().toString().equals("") | mGPA.getText().toString().equals("") | mYear.getText().toString().equals("") |
        mMajor.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this,"Please enter data in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for duplicate names. Not really working.
        if(checkForDuplicates(mName.getText().toString()) == true) {
           Toast.makeText(MainActivity.this, "Duplicate student found:" + mName.getText().toString(), Toast.LENGTH_SHORT).show();
           return;
        }

        else {

            try {
                profile.put("name", mName.getText().toString());
                profile.put("gpa", Double.parseDouble(mGPA.getText().toString()));
                profile.put("year", mYear.getText().toString());
                profile.put("major", mMajor.getText().toString());
                profile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }); //Saves the object in the background in a new thread. Does occupy the UI thread.
            }
            catch (Exception e) {

                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void retrieveStudentParseData(View buttonView){
        ParseQuery<ParseObject> studentDataQuery = ParseQuery.getQuery("Students"); //Gets objects of Students class

        String name = mEnterName.getText().toString();

        studentDataQuery.whereEqualTo("name", name); //Pulls data based on user input of student name.
        studentDataQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject student, ParseException e) {

                if(e == null) {
                    mEnterName.setText(student.get("name").toString());
                    mGetGpa.setText(student.get("gpa").toString());
                    mGetYear.setText(student.get("year").toString());
                    mGetMajor.setText(student.get("major").toString());

                    Toast.makeText(MainActivity.this, "Data Retrieved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public boolean checkForDuplicates(final String studentName) {
        final boolean[] duplicateFound = new boolean[1];

        ParseQuery<ParseObject> studentDataQueryAll = ParseQuery.getQuery("Students");

        studentDataQueryAll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> students, ParseException e) {

                for(ParseObject student : students) {

                    if (student.get("name").toString().equals(studentName)) {

//                        Toast.makeText(MainActivity.this, "Inside for loop, Duplicate Found: " + student.get("name").toString(), Toast.LENGTH_SHORT).show();
                        duplicateFound[0] = true;
                    }

                    else {
                        duplicateFound[0] = false;
                    }


                }
            }
        });

        return duplicateFound[0];
    }
}


//    public void fancyToast(View buttonView) {
//        FancyToast.makeText(this, "Hello World !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//        Toast.makeText(MainActivity.this, "Regular Toast", Toast.LENGTH_LONG).show();
//    }

