package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;

import java.util.Map;

public interface IUserSession {
    public  boolean isSetup();

    public  void setup(final Activity a);

    public void updateCurrentUserObject();
    public void updateAllUsers();

    public  User getCurrentUser();

    public User getUser(String email);

    public void setCurrentUser(final User newUser);

    void setCurrentUserWithoutWrite(final User newUser);

    public void writeUserToDB(final User user);

    public void addFriend(String email, Activity activity);

    void setUsers(final Map<String,User> userList);
}
