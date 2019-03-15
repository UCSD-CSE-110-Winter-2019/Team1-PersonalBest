package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.util.Log;

import java.util.Map;

public class UserSessionUpdater implements IDatabaseObserver {
    @Override
    public void update(User user) {
        Log.d("Updater", "got user " + user.getEmail() + user.getGraphData());
        UserSession.setCurrentUserWithoutWrite(user);
        UserSession.safeToWrite1 = true;
    }

    @Override
    public void update(Map<String, User> allUsers) {
        UserSession.setUsers(allUsers);
        UserSession.safeToWrite2 = true;
    }
}
