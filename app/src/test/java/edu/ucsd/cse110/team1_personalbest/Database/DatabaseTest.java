package edu.ucsd.cse110.team1_personalbest.Database;

import android.content.Context;

import org.apache.tools.ant.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest {

    private Database db;
    private String[] FILENAMES = {"02/16/2019", "02/17/2019"};
    private Context appContext = Robolectric.setupActivity(MainActivity.class).getApplicationContext();

    @Before
    public void setup() {
        UserSession.testmode = true;
        db = new Database(appContext, true);
        MainActivity.enable_firestore = false;
    }

    @After
    public void cleanup() {
        for ( String s : FILENAMES) {
            File temp = new File(appContext.getFilesDir(), s);
            if ( temp.exists() ) {
                temp.delete();
            }
        }
    }

    @Test
    public void testWriteAndReadObject() {
        StepDataObject obj = new StepDataObject(5, 5,5, "02/16/2019");
        db.putDataObject(obj);
        IDataObject result = db.readDataObject("02/16/2019");
        assertEquals(obj.getDailyStepCount(), result.getDailyStepCount());
        assertEquals(obj.getDailyIntentionalStepCount(), result.getDailyIntentionalStepCount());
        assertEquals(obj.getDailyStepGoal(), result.getDailyStepGoal());
        assertEquals(obj.getDate(), result.getDate());
    }

    @Test
    public void testMultipleDates() {
        StepDataObject obj = new StepDataObject(2, 2,2, "02/16/2019");
        db.putDataObject(obj);
        StepDataObject obj1 = new StepDataObject(1,1,1, "02/17/2019");
        db.putDataObject(obj1);
        IDataObject result = db.readDataObject("02/16/2019");
        IDataObject result1 = db.readDataObject("02/17/2019");
        assertEquals(obj.getDailyStepCount(), result.getDailyStepCount());
        assertEquals(obj.getDailyIntentionalStepCount(), result.getDailyIntentionalStepCount());
        assertEquals(obj.getDailyStepGoal(), result.getDailyStepGoal());
        assertEquals(obj.getDate(), result.getDate());
        assertEquals(obj1.getDailyStepCount(), result1.getDailyStepCount());
        assertEquals(obj1.getDailyIntentionalStepCount(), result1.getDailyIntentionalStepCount());
        assertEquals(obj1.getDailyStepGoal(), result1.getDailyStepGoal());
        assertEquals(obj1.getDate(), result1.getDate());
    }

    @Test
    public void testUpdateOneValue() {
        StepDataObject obj = new StepDataObject(5,0,0, "02/16/2019");
        db.putDataObject(obj);
        IDataObject result = db.readDataObject("02/16/2019");
        result.setDailyStepGoal(100);
        db.putDataObject(result);
        result = db.readDataObject("02/16/2019");
        assertEquals(result.getDailyStepGoal(), 100);
        assertEquals(result.getDailyStepCount(), 5);
    }

    @Test
    public void testWrongDate() {
        IDataObject result = db.readDataObject("WRONG_DATE");
        assertEquals(result.getDailyStepCount(), 0);
        assertEquals(result.getDailyIntentionalStepCount(), 0);
        assertEquals(result.getDailyStepGoal(), 0);
    }

}
