package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
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
public class Database extends AppCompatActivity implements IDatabaseSubject, IDatabase {

    // Not used in this implementation
    private FirebaseFirestore db;
    private ArrayList<IDatabaseObserver> IDatabaseObservers;
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
        IDatabaseObservers = new ArrayList<>();
        this.c = c;
        if (c.getApplicationContext() != null ) {
            FirebaseApp.initializeApp(c.getApplicationContext());
            db = FirebaseFirestore.getInstance();
        }
    }

    // test constructor
    public Database(Context c, Boolean Test) {
        IDatabaseObservers = new ArrayList<>();
        this.c = c;
    }

    /**
     *
     * @param store the Firestore instance that this database
     */
    public Database(FirebaseFirestore store) {
        db = store;
        IDatabaseObservers = new ArrayList<>();
    }

    public FirebaseFirestore getDatabase() {
        return db;
    }

    @Override
    public void register(IDatabaseObserver o) {
        if ( !IDatabaseObservers.contains(o) ) {
            IDatabaseObservers.add(o);
        }
    }

    @Override
    public void unregister(IDatabaseObserver o) {
        IDatabaseObservers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for( IDatabaseObserver obs : IDatabaseObservers) {
            if ( user != null ) {
                obs.update(user);
            }
            if ( allUsers != null ) {
                obs.update(allUsers);
            }

        }
    }

    @Override
    public void setUsers(Map<String, User> users) {
        HashMap<String,User> modifiedMap = new HashMap<>();
        for (String key : users.keySet()) {
            String modifiedKey = key;
            if (key.contains(".")) {
                modifiedKey = key.replace(".", "#");
            }
            modifiedMap.put(modifiedKey, users.get(key));
        }
        db.collection("users")
                .document("users")
                .set(modifiedMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added users");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    public void setUser(Map<String, Object> user) {
        if ( user == null ) return;
        if ( user.containsKey(null) ) return;
        HashMap<String,Object> modifiedMap = new HashMap<>();
        for (String key : user.keySet()) {
            String modifiedKey = key;
            if (key.contains(".")) {
                modifiedKey = key.replace(".", "#");
            }
            modifiedMap.put(modifiedKey, user.get(key));
        }
        db.collection("users")
                .document("users")
                .update(modifiedMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    public void getUsers() {
        db.collection("users")
                .document("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if ( documentSnapshot == null ) return;
                        HashMap<String,User> map = new HashMap<>();
                        Map<String,Object> doc = documentSnapshot.getData();
                        if (doc == null) return;
                        for (String key : doc.keySet()) {
                            Map userinfo = (Map)doc.get(key);
                            String modifiedKey = key;
                            if (key.contains("#")) {
                                modifiedKey = key.replace("#", ".");
                            } else {
                                continue;
                            }
                            map.put(modifiedKey, makeUser(userinfo));
                        }
                        allUsers = map;
                        notifyObservers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    public void getUser(final String email) {
        final String newEmail = email.replace(".", "#");
        if ( email == null ) return;
        db.collection("users")
                .document("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if ( documentSnapshot == null) return;
                        if (documentSnapshot.getData()  == null) return;;
                        Map userInfo = (Map)documentSnapshot.getData().get(newEmail);
                        if (userInfo == null) return;
                        user = makeUser(userInfo);
                        notifyObservers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private User makeUser(Map userInfo) {
        User temp = new User();
        if ( userInfo.containsKey("name") && userInfo.get("name") != null) {
            temp.setName(userInfo.get("name").toString());
        } else {
            temp.setName("");
        }
        if ( userInfo.containsKey("email") && userInfo.get("email") != null) {
            temp.setEmail(userInfo.get("email").toString());
        } else {
            temp.setEmail("");
        }
        if ( userInfo.containsKey("friends") && userInfo.get("friends") != null) {
            temp.setFriends((List<String>) userInfo.get("friends"));
        } else {
            temp.setFriends(new ArrayList<>());
        }
        if ( userInfo.containsKey("pendingRequests") && userInfo.get("pendingRequests") != null) {
            temp.setRequests((List<String>) userInfo.get("pendingRequests"));
        } else {
            temp.setRequests(new ArrayList<>());
        }
        if (userInfo.containsKey("graphData") && userInfo.get("graphData") != null) {
            temp.setGraphData((Map)userInfo.get("graphData"));
        } else {
            temp.setGraphData(new HashMap<>());
        }
        return temp;
    }

    /**
     * write takes the fileName, JSONObject and context to write to a location in the phone
     *
     * @param fileName the name of the file to write to
     * @param obj the object to write to file
     * @param c the Context to get the file locations from
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
     * @param c the Context to get the file locations from
     * @return a JSONObject if there is one in this file else null
     */
    private JSONObject read(String fileName, Context c) {
        try {
            // replace fileName special characters with -
            File temp = new File(c.getFilesDir(), fileName.replaceAll("/", "-"));
            if ( !temp.exists() ) {
                return null;
            }
            FileReader r = new FileReader(temp);
            int i = 0;
            String line = "";
            while ( (i = r.read()) != -1 ) {
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

        HashMap<String, Object> map = new HashMap<>();
        t = t.replaceAll("=", ":");
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            // removes all JSON syntax for creating objects
            value = value.replaceAll("\\{", "");
            value = value.replaceAll("\\}", "");
            value = value.replaceAll("\"", "");
            if ( value.contains(" ") ) {
                value = value.replaceAll(" ", "");
            }
            String[] split = value.split(",");
            JSONObject child = new JSONObject();
            for ( String s : split ) {
                child.put(s.split(":")[0], s.split(":")[1]);
            }
            jObject.put(key, child);

        }

        return jObject;
    }

    /**
     * Deletes a file on the phone if it exists
     *
     * @param fileName the file to delete
     * @param c the Context to find file location
     */
    public void deleteFile(String fileName, Context c) {
        File temp = new File(c.getFilesDir(), fileName.replaceAll("/", "-"));
        if ( temp.delete() ) {
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
        } catch(Exception e) {
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
            if ( tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("steps") ) {
                dailyStepCount = tmp.getJSONObject(date).getInt("steps");
            }
            int dailyIntentionalStepCount = 0;
            if ( tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("intentional_steps") ) {
                dailyIntentionalStepCount = tmp.getJSONObject(date).getInt("intentional_steps");
            }
            int dailyStepGoal = 0;
            if ( tmp != null && tmp.getJSONObject(date) != null && tmp.getJSONObject(date).has("goal") ) {
                dailyStepGoal = tmp.getJSONObject(date).getInt("goal");
            }
            return new StepDataObject(dailyStepCount, dailyIntentionalStepCount, dailyStepGoal, date);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}