package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.internal.LocalPermissionGranter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.CustomGoalActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.IUserSession;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetNewGoalActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class SetNewGoalActivityTest {
    private SetNewGoalActivity activity;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<SetNewGoalActivity> cont;

    private long currSteps;
    private StepDataObject day1;
    private StepDataObject day2;
    private String currDate;
    private StepDataObject today;
    private Context appContext = Robolectric.setupActivity(MainActivity.class).getApplicationContext();
    private IUserSession sess = Mockito.mock(IUserSession.class);
    @Before
    public void setUp() throws Exception {
        UserSession.testmode = true;
        UserSession.testSession = sess;
        User u = new User();
        MainActivity.enable_firestore = false;
        service = Mockito.mock(FitnessService.class);
        loginService = Mockito.mock(LoginService.class);
        LocalPermissionGranter granter = new LocalPermissionGranter();
        granter.addPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET);

        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return service;
            }
        });

        LoginServiceFactory.put(TEST_SERVICE, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return loginService;
            }
        });


        Intent intent = new Intent(RuntimeEnvironment.application, SetNewGoalActivity.class);
        cont = Robolectric.buildActivity(SetNewGoalActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);


        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal1.getTime();
        currDate = format.format(date1);
        u.setStepGoal(currDate, 500);
        Mockito.when(sess.getCurrentUser()).thenReturn(u);
    }

    @Test
    public void TestGetSuggestedGoal(){
        cont.create();
        Mockito.verify(sess).getCurrentUser();
        int cusGoal = activity.getSuggestedGoal();
        assertThat(cusGoal, equalTo(1000));
    }


    @After
    public void cleanup() {
        File temp = new File(appContext.getFilesDir(), currDate);
        if ( temp.exists() )
            temp.delete();
    }
}