package edu.ucsd.cse110.team1_personalbest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DatabaseUnitTest {

    private Database db;
    private static String FILENAME = "test.json";
    private static String NOTEXIST = "dummy.json";

    @Before
    public void setup() {
        FirebaseFirestore mock = Mockito.mock(FirebaseFirestore.class);
        db = new Database(mock);
    }

    @After
    public void cleanup() {
        File temp = new File(FILENAME);
        if ( temp.exists() ) {
            temp.delete();
        }
    }

    @Test
    public void testWriteAndRead() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("test", "123");
            db.write(FILENAME, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject obj = db.read(FILENAME);
            assertEquals(obj.toString(), "{\"test\":\"123\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoFile() {
        try {
            JSONObject obj = db.read(NOTEXIST);
            assertNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
