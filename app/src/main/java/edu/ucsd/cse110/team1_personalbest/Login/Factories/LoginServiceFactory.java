package edu.ucsd.cse110.team1_personalbest.Login.Factories;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

/**
 * Factory for instantiating a {@link LoginService}
 */
public class LoginServiceFactory {
    private static final String TAG = "[LoginServiceFactory]";

    // Map of known step service blueprints
    private static final Map<String, LoginServiceFactory.BluePrint> blueprints = new HashMap<>();

    /**
     * adds a new login service to the map.
     * @param key: string key to reference this service by
     * @param bluePrint: a blueprint of this service
     */
    public static void put(final String key, final LoginServiceFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    /**
     * instantiates a new login service from the given key to the given activity.
     * @param key: the key to find the login service with
     * @param activity: activity to connect to
     * @return the step service
     */
    public static LoginService create(final String key, final Activity activity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(activity);
    }

    /**
     * BluePrint interface for login services
     */
    public interface BluePrint {
        LoginService create(final Activity activity);
    }
}
