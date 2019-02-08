package edu.ucsd.cse110.team1_personalbest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;

public class DatabaseUnitTest {

    Database db;

    @Before
    public void setup() {
        FirebaseFirestore mock = Mockito.mock(FirebaseFirestore.class);
        db = new Database(mock);
    }

    @Test
    public void testPush() {
        Map<String, Integer> map = new HashMap<>();
        map.put("06", 100);
        db.push("02", map);
    }

    @Test
    public void testGet() {

    }
}
