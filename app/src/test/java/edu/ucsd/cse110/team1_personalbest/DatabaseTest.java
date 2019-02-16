package edu.ucsd.cse110.team1_personalbest;

import android.content.Context;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest {

    private Database db;
    private static String FILENAME = "test.json";
    private static String NOTEXIST = "dummy.json";
    private Context appContext = Robolectric.setupActivity(MainActivity.class).getApplicationContext();

    @Before
    public void setup() {
        db = new Database();
    }

    @After
    public void cleanup() {
        File temp = new File(FILENAME);
        if ( temp.exists() ) {
            temp.delete();
        }
    }

    @Test
    public void testNoFile() {
        try {
            JSONObject obj = db.read(NOTEXIST, appContext);
            assertNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDate() {
        try {
            JSONObject obj = new JSONObject();
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            HashMap<String, Integer> map = new HashMap<>();
            map.put("steps", 120);
            map.put("goal", 6000);
            obj.put(format.format(date), map);
            db.write(FILENAME, obj, appContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleWrite() {
        try {
            JSONObject obj = new JSONObject();
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            HashMap<String, Integer> map = new HashMap<>();
            map.put("steps", 120);
            map.put("goal", 6000);
            obj.put(format.format(date), map);
            db.write(FILENAME, obj, appContext);
            obj = db.read(FILENAME, appContext);
            map.put("steps", 150 + obj.getJSONObject(format.format(date)).getInt("steps"));
            map.put("goal", 6000);
            obj.put(format.format(date), map);
            db.write(FILENAME, obj, appContext);
            obj = db.read(FILENAME, appContext);
            assertEquals(obj.getJSONObject(format.format(date)).getInt("steps"), 270);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleDays() {
        try {
            JSONObject obj = new JSONObject();
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            HashMap<String, Integer> map = new HashMap<>();
            map.put("steps", 120);
            map.put("goal", 6000);
            obj.put(format.format(date), map);
            cal.add(Calendar.DATE, -1);
            date = cal.getTime();
            obj.put(format.format(date), map);
            db.write(FILENAME, obj, appContext);
            obj = db.read(FILENAME, appContext);
            assertEquals(obj.getJSONObject(format.format(date)).getInt("steps"), 120);
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
            assertEquals(obj.getJSONObject(format.format(date)).getInt("steps"), 120);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
