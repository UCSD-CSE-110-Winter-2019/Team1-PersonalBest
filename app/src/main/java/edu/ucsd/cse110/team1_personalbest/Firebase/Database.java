package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

/*
    Database class stores the IDataObject into JSON files for easy read and write access.
 */
public class Database extends AppCompatActivity implements Subject, IDatabase {

    // Not used in this implementation
    private FirebaseFirestore db;
    private ArrayList<Observer> observers;
    private Map<String, Object> data;
    // Context needed to get files written to and from by file location
    private Context c;

    /**
     * Constructor:
     * Database creates the instance of this class for reading and writing
     *
     * @param c the Context needed to know where file locations are
     */
    public Database(Context c) {
        //FirebaseApp.initializeApp(this);
        //db = FirebaseFirestore.getInstance();
        observers = new ArrayList<>();
        this.c = c;
    }

    /**
     *
     * @param store the Firestore instance that this database
     */
    @Deprecated
    public Database(FirebaseFirestore store) {
        db = store;
        observers = new ArrayList<>();
    }

    @Deprecated
    @Override
    public void register(Observer o) {
        if ( !observers.contains(o) ) {
            observers.add(o);
        }
    }

    @Deprecated
    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Deprecated
    @Override
    public void notifyObservers() {
        for( Observer obs : observers ) {
            obs.update(data);
        }
    }


    /**
     * Push to a document reference and insert a Map
     */
    @Deprecated
    public void push(String document, Map map) {
        db.collection("calendar")
                .document(document)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added info");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding info", e);
                    }
                });
    }

    /**
     * Notifies observers with the Map data
     * @param document the document location to get the object
     */
    @Deprecated
    public void get(String document) {
        db.collection("calendar")
                .document(document)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if ( task.isSuccessful() ) {
                            data = task.getResult().getData();
                            notifyObservers();
                            Log.d(TAG, "Got document info");
                        }
                    }
                });
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
     * @throws JSONException
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
        File temp = new File(c.getFilesDir(), fileName);
        if ( temp.exists() ) {
            temp.delete();
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
