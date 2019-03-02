package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    Database class stores the IDataObject into JSON files for easy read and write access.
 */
public class Database extends AppCompatActivity implements Subject, IDatabase {

    // Not used in this implementation
    private FirebaseFirestore db;
    private ArrayList<Observer> observers;
    private User user;
    private Map<String, User> allUsers;
    // Context needed to get files written to and from by file location
    private Context c;
    private static final String TAG = "[Database]";

    /**
     * Constructor:
     * Database creates the instance of this class for reading and writing
     *
     * @param c the Context needed to know where file locations are
     */
    public Database(Context c) {
        observers = new ArrayList<>();
        this.c = c;
        if ( c.getApplicationContext() != null ) {
            FirebaseApp.initializeApp(c.getApplicationContext());
            db = FirebaseFirestore.getInstance();
        }
    }

    /**
     * @param store the Firestore instance that this database
     */
    public Database(FirebaseFirestore store) {
        db = store;
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(user);
            obs.update(allUsers);
        }
    }

    public void setUsers(Map<String, User> users) {
        db.collection("users")
                .document("users")
                .set(users)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Successfully added friends list");
                })
                .addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    public void getUsers() {
        db.collection("users")
                .document("users")
                .get()
                .addOnSuccessListener(result -> {
                    if ( result == null ) return;
                    allUsers = (Map) result.getData();
                    notifyObservers();
                })
                .addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    public void getUser(String email) {
        db.collection("users")
                .document("users")
                .get()
                .addOnSuccessListener(result -> {
                    if ( result == null ) return;
                    Map<String, Object> userInfo = (Map) result.getData().get(email);
                    User temp = new User();
                    temp.setName(userInfo.get("name").toString());
                    temp.setEmail(userInfo.get("email").toString());
                    temp.setFriends((List<String>) userInfo.get("friends"));
                    user = temp;
                    //Log.d(TAG, String.valueOf(user.getGraphData("03-01-2019")[0][0]));
                    notifyObservers();
                })
                .addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    /**
     * write takes the fileName, JSONObject and context to write to a location in the phone
     *
     * @param fileName the name of the file to write to
     * @param obj      the object to write to file
     * @param c        the Context to get the file locations from
     */
    private void write(String fileName, JSONObject obj, Context c) {
        try {
            // replace fileName special characters with -
            File temp = new File(c.getFilesDir(), fileName.replaceAll("/", "-"));
            PrintWriter pw = new PrintWriter(temp);
            pw.write(obj.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read takes a fileName and context to see which file to read from
     *
     * @param fileName the name of the file to write to
     * @param c        the Context to get the file locations from
     * @return a JSONObject if there is one in this file else null
     */
    private JSONObject read(String fileName, Context c) {
        try {
            // replace fileName special characters with -
            File temp = new File(c.getFilesDir(), fileName.replaceAll("/", "-"));
            if (!temp.exists()) {
                return null;
            }
            FileReader r = new FileReader(temp);
            int i = 0;
            String line = "";
            while ((i = r.read()) != -1) {
                line += (char) i;
            }
            return stringToJSON(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a String in JSON format to a JSONObject
     *
     * @param t the String to convert to JSON format
     * @return a JSONObject
     * @throws JSONException error if it cannot convert the string to JSON
     */
    private static JSONObject stringToJSON(String t) throws JSONException {
        t = t.replaceAll("=", ":");
        JSONObject jObject = new JSONObject(t);

        return jObject;
    }

    /**
     * Deletes a file on the phone if it exists
     *
     * @param fileName the file to delete
     * @param c        the Context to find file location
     */
    public void deleteFile(String fileName, Context c) {
        File temp = new File(c.getFilesDir(), fileName.replaceAll("/", "-"));
        if (temp.delete()) {
            Log.d(TAG, "deleted " + fileName.replaceAll("/", "-"));
        }
    }

    /**
     * putDataObject stores the interface object into the JSON file
     *
     * @param object the object to store
     */
    @Override
    public void putDataObject(IDataObject object) {
        try {
            Log.d(TAG, "Writing to " + object.getDate());
            JSONObject tmp = new JSONObject();
            JSONObject child = new JSONObject();
            child.put("goal", object.getDailyStepGoal());
            child.put("steps", object.getDailyStepCount());
            child.put("intentional_steps", object.getDailyIntentionalStepCount());
            tmp.put(object.getDate(), child);
            write(object.getDate(), tmp, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an IDataObject containing data for the Step Information
     *
     * @param date the date to look up information for
     * @return an IDataObject containing the step data
     */
    @Override
    public IDataObject readDataObject(String date) {
        try {
            Log.d(TAG, "Reading from " + date);
            JSONObject tmp = read(date, c);
            int dailyStepCount = 0;
            if (tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("steps")) {
                dailyStepCount = tmp.getJSONObject(date).getInt("steps");
            }
            int dailyIntentionalStepCount = 0;
            if (tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("intentional_steps")) {
                dailyIntentionalStepCount = tmp.getJSONObject(date).getInt("intentional_steps");
            }
            int dailyStepGoal = 0;
            if (tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("goal")) {
                dailyStepGoal = tmp.getJSONObject(date).getInt("goal");
            }
            return new StepDataObject(dailyStepCount, dailyIntentionalStepCount, dailyStepGoal, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
