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

        User user = new User();
        user.setEmail("Allen@gmail.com");
        user.setDailySteps("03/14/2019",1000);
        user.setDailySteps("03/15/2019",2000);
        user.setDailySteps("03/16/2019",1500);
        user.setDailySteps("03/17/2019",790);
        user.setDailySteps("03/13/2019",1100);
        user.setDailySteps("03/12/2019",1000);
        user.setDailySteps("03/11/2019",2500);

        user.setIntentionalSteps("03/14/2019",900);
        user.setIntentionalSteps("03/15/2019",1900);
        user.setIntentionalSteps("03/16/2019",1300);
        user.setIntentionalSteps("03/17/2019",700);
        user.setIntentionalSteps("03/13/2019",1000);
        user.setIntentionalSteps("03/12/2019",800);
        user.setIntentionalSteps("03/11/2019",1400);

        return user;
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
