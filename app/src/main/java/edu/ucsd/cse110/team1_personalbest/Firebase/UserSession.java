package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;

public class UserSession {
    private static User user;
    private static IDatabase database;
    private static IDatabaseObserver observer;
    private static Map<String, User> users;

    public static boolean isSetup() {
        return user != null;
    }

    public static void setup(final Activity a) {
        String userEmail = GoogleLogInService.getLastLoggedInAccount(a);
        database = new Database(a.getApplicationContext());
        user = new User();
        user.setEmail(userEmail);
        IDatabaseObserver obs = new UserSessionUpdater();
        database.register(obs);

        database.getUser(userEmail);
        database.getUsers();
    }

    public static void updateCurrentUserObject() {
        database.getUser(user.getEmail());
    }
    public static void updateAllUsers() {
        database.getUsers();
    }

    public static User getCurrentUser() {
        return user;
    }

    public static User getUser(String email) {
        return users.get(email);
    }

    public static void setCurrentUser(final User newUser) {
        user = newUser;
        writeUserToDB(user);
    }

    public static void writeUserToDB(final User user) {
        HashMap<String,Object> map = new HashMap<>();
        map.put(user.getEmail(), user);
        database.setUser(map);
    }

    public void addFriend(String email) {
        User newFriend = users.get(email);
        if (newFriend.getPendingRequests().contains(user.getEmail())) {
            user.addFriend(newFriend);
            newFriend.addFriend(user);
            newFriend.removeRequest(user);
            writeUserToDB(newFriend);
        } else {
            user.sendRequest(users.get(email));
        }
        writeUserToDB(user);
    }

    protected static void setUsers(final Map<String,User> userList) {
        users = userList;
    }

    /**
     * this method is called when garbage collector destroys the object, doing this to ensure
     * that data is saved.
     */
    @Override
    public void finalize() {
        writeUserToDB(user);
    }
}
