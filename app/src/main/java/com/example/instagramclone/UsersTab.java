package com.example.instagramclone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {

    private ListView mListView;
    private ArrayList mArrayList;
    private ArrayAdapter mArrayAdapter;
    private TextView mLoadingTextView;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        mListView = view.findViewById(R.id.usersTab_ListView);
        mArrayList = new ArrayList();

        //Creates list items from the array, using the android default list item layout.
        mArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, mArrayList);

        mLoadingTextView = view.findViewById(R.id.usersTabLoading_TextView);

        populateListView();

        return view;
    }

    private void populateListView() {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

        //Makes sure we don't show the current user in the query
        userParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null) {
                    if(users.size() > 0) {
                        for(ParseUser user : users) {
                            mArrayList.add(user.getUsername());
                        }

                        //Used to populate the list view using our adapter object.
                        mListView.setAdapter(mArrayAdapter);

                        mLoadingTextView.animate().alpha(0).setDuration(2000);
                        mListView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
