package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;
import android.util.Log;

import java.net.ContentHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Messaging.FirestoreMessagingAdapter;
import edu.ucsd.cse110.team1_personalbest.Messaging.MessagingServiceFactory;

public class UserSession {
    private static User user;
    private static IDatabase database;
    private static IDatabaseObserver observer;
    private static Map<String, User> users;
    protected static boolean safeToWrite1 = false;
    protected static boolean safeToWrite2 = false;
    private static boolean issetup = false;
    public static boolean testmode = false;
    public static boolean uitestmode = false;
    public static IUserSession testSession = new TestUserSession();

    public static boolean isSetup() {
        return user != null;
    }

    public static void setup(final Activity a) {
        if(testmode) {
            testSession.setup(a);
            return;
        }
        String userEmail = GoogleLogInService.getLastLoggedInAccount(a);
        database = new Database(a.getApplicationContext());
        IDatabaseObserver obs = new UserSessionUpdater();
        database.register(obs);
        if (user == null) {
            user = new User();
            user.setEmail(userEmail);
        }
        issetup = true;
        database.getUser(userEmail);
        database.getUsers();
    }

    public static void updateCurrentUserObject() {
        if(testmode) {
            testSession.updateCurrentUserObject();
            return;
        }
        database.getUser(user.getEmail());
    }
    public static void updateAllUsers() {
        if(testmode) {
            testSession.updateAllUsers();
            return;
        }
        database.getUsers();
    }

    public static User getCurrentUser() {
        if(testmode) {
            if(uitestmode) return user;
            return testSession.getCurrentUser();
        }
        if (user == null) return new User();
        return user;
    }

    public static User getUser(String email) {
        if(testmode) {
            return testSession.getUser(email);
        }
        if (users == null) return new User();
        if (users.get(email) == null) return new User();
        return users.get(email);
    }

    public static void setCurrentUser(final User newUser) {
        if(testmode) {
            if(uitestmode && newUser != null) user = newUser;
            testSession.setCurrentUser(newUser);
            return;
        }
        if (newUser != null)
            user = newUser;
        writeUserToDB(user);
    }

    protected static void setCurrentUserWithoutWrite(final User newUser) {
        if(testmode) {
            testSession.setCurrentUserWithoutWrite(newUser);
            return;
        }
        if (newUser != null)
            user = newUser;
    }

    public static void writeUserToDB(final User user) {
        if(testmode) {
            testSession.writeUserToDB(user);
            return;
        }
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = format.format(calendar.getTime());

        Log.d("UserSession", ""+user.getGraphData().toString());
        if (user.getEmail() != null && safeToWrite1 && safeToWrite2 && issetup) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(user.getEmail(), user);
            database.setUser(map);
        }
    }

    public static void addFriend(String email, Activity activity) {
        if(testmode) {
            testSession.addFriend(email, activity);
            return;
        }
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
        if(testmode) {
            testSession.setUsers(userList);
            return;
        }
        users = userList;
    }

    /**
     * this method is called when garbage collector destroys the object, doing this to ensure
     * that data is saved.
     */
    @Override
    public void finalize() {
        if(testmode) {
            return;
        }
        writeUserToDB(user);
    }
}
