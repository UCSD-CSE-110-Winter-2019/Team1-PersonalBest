package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
import java.io.*;
import org.json.*;

public class Database {

    /*private FirebaseFirestore db;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }

    public Database(FirebaseFirestore store) {
        db = store;
    }

    public void push(String range, Map map) {

        db.collection("calendar")
                .document(range)
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

    public void get(String range) {
        db.collection("calendar")
                .document(range)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if ( task.isSuccessful() ) {

                            Log.d(TAG, "Got document info");
                        }
                    }
                });

    }*/

    public Database() {}

    public void write(String fileName, JSONObject obj) {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.write(obj.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject read(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            int i = 0;
            String line = "";
            while ( (i = r.read()) != -1 ) {
                line += (char) i;
                System.out.print((char) i);
            }
            return new JSONObject(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
