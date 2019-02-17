package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.internal.LocalPermissionGranter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.CustomGoalActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetNewGoalActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class CustomGoalActivityTest {
    private CustomGoalActivity activity;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<CustomGoalActivity> cont;

    private StepDataObject day1;
    private StepDataObject day2;

    @Before
    public void setUp() throws Exception {
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

        Intent intent = new Intent(RuntimeEnvironment.application, CustomGoalActivity.class);
        cont = Robolectric.buildActivity(CustomGoalActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);


    }

    public void setupDB() {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal1.getTime();
        String currDate = format.format(date1);
        day1 = new StepDataObject(1000, 0, 5100, currDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -1);
        Date date2 = cal2.getTime();
        String preDate = format.format(date2);
        day2 = new StepDataObject(1000, 0, 5000, preDate);
        activity.setDataBase(day1, day2);
    }

    @Test
    public void TestGetCustomGoal(){
        cont.create();
        EditText goal = activity.findViewById(R.id.customGoal);
        goal.setText("6010");

        int cusGoal = activity.getCustomGoal();
        assertThat(cusGoal, equalTo(6010));
    }


    @Test
    public void TestSaveSuggestedGoal(){
        cont.create();
        this.setupDB();
        /*
        activity.saveCustomGoal(6010);
        assertThat(day1.getDailyStepCount(), equalTo(6010));

        activity.saveCustomGoal(2010);
        assertThat(day1.getDailyStepCount(), equalTo(2010));

        activity.saveCustomGoal(10002);
        assertThat(day1.getDailyStepCount(), equalTo(10002));

        activity.saveCustomGoal(3);
        assertThat(day1.getDailyStepCount(), equalTo(3));
        */
    }

}