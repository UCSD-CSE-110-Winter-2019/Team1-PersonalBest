package edu.ucsd.cse110.team1_personalbest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;

public class DatabaseUnitTest {

    Database db;

    @Before
    public void setup() {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

    }

    @Test
    public void testPush() {
        if ( db == null ) {
            return;
        }
        db.push();
    }

    @Test
    public void testGet() {

    }
}
