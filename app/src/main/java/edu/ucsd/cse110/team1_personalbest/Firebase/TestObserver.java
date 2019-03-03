package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.util.Log;

import java.util.Map;

public class TestObserver implements Observer {

    private static final String TAG = "[TestObserver]";

    public TestObserver() {

    }

    @Override
    public void update(User user) {
        if ( user == null ) return;
        Log.d(TAG, "Observer Update: " + user.getName() + " " + user.getEmail() + " " + user.getFriends());
    }

    @Override
    public void update(Map<String, User> allUsers) {
        if ( allUsers == null ) return;
        Log.d(TAG, "Observer Update: " + allUsers.entrySet());
    }
}
