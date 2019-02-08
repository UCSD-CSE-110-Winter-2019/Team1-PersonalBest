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

public class Database {

    FirebaseFirestore db;

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

    }
}
