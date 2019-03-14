package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.util.Log;

import java.util.Map;

public class UserSessionUpdater implements IDatabaseObserver {
    @Override
    public void update(User user) {
        Log.d("Updater", "got user " + user.getEmail() + user.getFriends());
        UserSession.setCurrentUserWithoutWrite(user);
    }

    @Override
    public void update(Map<String, User> allUsers) {
        UserSession.setUsers(allUsers);
    }
}
