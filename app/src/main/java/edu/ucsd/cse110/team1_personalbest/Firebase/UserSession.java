package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Messaging.FirestoreMessagingAdapter;
import edu.ucsd.cse110.team1_personalbest.Messaging.MessagingServiceFactory;

public class UserSession {
    private static User user;
    private static IDatabase database;
    private static IDatabaseObserver observer;
    private static Map<String, User> users;
    private static Activity activity;

    public static boolean isSetup() {
        return user != null;
    }

    public static void setup(final Activity a) {
        activity = a;
        String userEmail = GoogleLogInService.getLastLoggedInAccount(a);
        database = new Database(a.getApplicationContext());
        IDatabaseObserver obs = new UserSessionUpdater();
        database.register(obs);
        if (user == null) {
            user = new User();
            user.setEmail(userEmail);
        }
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
        if (newUser != null)
            user = newUser;
        writeUserToDB(user);
    }

    protected  static void setCurrentUserWithoutWrite(final User newUser) {
        if (newUser != null)
            user = newUser;
    }

    public static void writeUserToDB(final User user) {
        HashMap<String,Object> map = new HashMap<>();
        map.put(user.getEmail(), user);
        database.setUser(map);
    }

    public static void addFriend(String email) {
        Log.d("test", users.toString());
        User newFriend = users.get(email);
        if (newFriend == null) return;
        if (newFriend.getPendingRequests().contains(user.getEmail())) {
            user.addFriend(newFriend);
            newFriend.addFriend(user);
            newFriend.removeRequest(user);
            writeUserToDB(newFriend);
        } else {
            user.sendRequest(users.get(email));
        }
        FirestoreMessagingAdapter.subscribe(activity,
                MessagingServiceFactory.getConversationKey(user.getEmail(),
                        newFriend.getEmail()));
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