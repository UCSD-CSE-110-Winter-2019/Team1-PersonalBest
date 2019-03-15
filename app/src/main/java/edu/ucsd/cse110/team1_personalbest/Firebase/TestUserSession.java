package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;

import java.util.Map;

public class TestUserSession implements IUserSession {
    private User user;

    public TestUserSession() {
        user = new User();
    }
    @Override
    public boolean isSetup() {
        return false;
    }

    @Override
    public void setup(Activity a) {

    }

    @Override
    public void updateCurrentUserObject() {

    }

    @Override
    public void updateAllUsers() {

    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public User getUser(String email) {
        return new User();
    }

    @Override
    public void setCurrentUser(User newUser) {

    }

    @Override
    public void setCurrentUserWithoutWrite(User newUser) {

    }

    @Override
    public void writeUserToDB(User user) {

    }

    @Override
    public void addFriend(String email, Activity activity) {

    }

    @Override
    public void setUsers(Map<String, User> userList) {

    }
}
