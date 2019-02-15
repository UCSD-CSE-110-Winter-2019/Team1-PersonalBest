package edu.ucsd.cse110.team1_personalbest.Fitness.Factories;

import android.app.Activity;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;

/**
 * Factory for producing instances of {@link FitnessService}.
 */
public class FitnessServiceFactory {
    private static final String TAG = "[FitnessServiceFactory]";

    // Map of known step service blueprints
    private static final Map<String, BluePrint> blueprints = new HashMap<>();

    /**
     * adds a new step service to the map.
     * @param key: string key to reference this service by
     * @param bluePrint: a blueprint of this service
     */
    public static void put(final String key, final BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    /**
     * instantiates a new step service from the given key to the given activity.
     * @param key: the key to find the step service with
     * @param activity: activity to connect to
     * @return the step service
     */
    public static FitnessService create(final String key, final Activity activity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        if (blueprints.get(key) == null) return null;
        return blueprints.get(key).create(activity);
    }

    /**
     * BluePrint interface for step services
     */
    public interface BluePrint {
        FitnessService create(final Activity activity);
    }
}
