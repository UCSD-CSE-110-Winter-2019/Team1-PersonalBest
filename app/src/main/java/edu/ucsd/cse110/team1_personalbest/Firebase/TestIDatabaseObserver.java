package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.util.Log;

import java.util.Map;

public class TestIDatabaseObserver implements IDatabaseObserver {

    private static final String TAG = "[TestIDatabaseObserver]";

    public TestIDatabaseObserver() {

    }

    @Override
    public void update(User user) {
        if ( user == null ) return;
        Log.d(TAG, "IDatabaseObserver Update: " + user.getName() + " " + user.getEmail() + " " + user.getFriends());
    }

    @Override
    public void update(Map<String, User> allUsers) {
        if ( allUsers == null ) return;
        Log.d(TAG, "IDatabaseObserver Update: " + allUsers.entrySet());
    }
}
