package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.app.Activity;

import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;

public class UserSession {
    private static User user;
    private static IDatabase database;
    private static IDatabaseObserver observer;

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
    }

    public static void updateUserObject() {
        database.getUser(user.getEmail());
    }

    public static User getUser() {
        return user;
    }

    protected static void setUser(final User newUser) {
        user = newUser;
    }

    public void writeUserToDB() {
        // add logic here
    }

    /**
     * this method is called when garbage collector destroys the object, doing this to ensure
     * that data is saved.
     */
    @Override
    public void finalize() {
        this.writeUserToDB();
    }
}
