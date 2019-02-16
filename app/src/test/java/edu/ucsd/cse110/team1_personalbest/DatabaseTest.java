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
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;

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
        db = new Database(appContext);
    }

    @After
    public void cleanup() {
        File temp = new File(FILENAME);
        if ( temp.exists() ) {
            temp.delete();
        }
    }

    @Test
    public void testWriteAndReadObject() {
        StepDataObject obj = new StepDataObject(0, 0,0, "2/16/2019");
        db.putDataObject(obj);
        IDataObject result = db.readDataObject("2/16/2019");
        assertEquals(obj.getDailyStepCount(), result.getDailyStepCount());
        assertEquals(obj.getDailyIntentionalStepCount(), result.getDailyIntentionalStepCount());
        assertEquals(obj.getDailyStepGoal(), result.getDailyStepGoal());
        assertEquals(obj.getDate(), result.getDate());
    }

}
