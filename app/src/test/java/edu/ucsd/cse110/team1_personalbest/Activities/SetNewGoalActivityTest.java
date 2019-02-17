package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
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
    private SetNewGoalActivity activity1;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<SetNewGoalActivity> cont;

    private long currSteps;
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

        Intent intent = new Intent(RuntimeEnvironment.application, SetNewGoalActivity.class);
        cont = Robolectric.buildActivity(SetNewGoalActivity.class, intent);
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
    public void TestGetSuggestedGoal(){
        cont.create();
        this.setupDB();
        int getGoal = activity.getSuggestedGoal();
        int suggestedGoal = day1.getDailyStepGoal() + 500;

        TextView suggestedGoalView = activity.findViewById(R.id.newSuggestedGoal);
        assertThat(suggestedGoal, equalTo(getGoal));
        assertThat(String.valueOf(suggestedGoal), equalTo(suggestedGoalView.getText().toString()));
    }

    @Test
    public void TestSaveSuggestedGoal(){
        cont.create();
        this.setupDB();

        int getGoal = activity.getSuggestedGoal();
        day1.setDailyStepGoal(getGoal);
        activity.saveSuggestedGoal(getGoal);

        assertThat(day1.getDailyStepGoal(), equalTo(5600));
    }
}