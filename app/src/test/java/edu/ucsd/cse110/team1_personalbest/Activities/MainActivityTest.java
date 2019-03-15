package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import junit.framework.Assert;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.internal.LocalPermissionGranter;
import org.robolectric.shadows.ShadowToast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Firebase.IUserSession;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetNewGoalActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;
    private MainActivity activity1;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<MainActivity> cont;

    private long currSteps;
    private StepDataObject day1;
    private StepDataObject day2;
    private IUserSession userSession = Mockito.mock(IUserSession.class);

    @Before
    public void setUp() throws Exception {
        MainActivity.enable_firestore = false;
        UserSession.testmode = true;
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

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        cont = Robolectric.buildActivity(MainActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);

    }

    @Test
    public void loginFailed() {
        Mockito.when(this.loginService.login()).thenReturn(false);
        cont.create();
        Mockito.verify(this.loginService).login();
        Mockito.verify(this.service, Mockito.never()).setup();
    }

    @Test
    public void loginSuccess() {
        Mockito.when(this.loginService.login()).thenReturn(true);
        ArgumentCaptor<FitnessObserver> obsCaptor = ArgumentCaptor.forClass(FitnessObserver.class);
        cont.create();
        Mockito.verify(this.service).registerObserver(obsCaptor.capture());
        Mockito.verify(this.service).setup();
        Mockito.verify(this.service).startListening();
        Assert.assertNotNull(obsCaptor.getValue());
    }

    @Test
    public void TestNoEncouragementForNoFriendPerson() {
        cont.create();
        currSteps = 1100;
        activity.setCurrSteps(currSteps);
        this.setupDB();

        activity.onResume();
        assertNull(ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void TestEncouragementNotDoubleForNoFriendPerson() {
        cont.create();
        currSteps = 1500;
        activity.setCurrSteps(currSteps);
        this.setupDB();

        activity.onResume();
        System.out.println(ShadowToast.getTextOfLatestToast().toString());
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo( "Good job! You've made great prgroess!"));
    }

    @Test
    public void TestEncouragementNearlyDoubleForNoFriendPerson() {
        cont.create();
        currSteps = 1900;
        activity.setCurrSteps(currSteps);
        this.setupDB();

        activity.onResume();
        System.out.println(ShadowToast.getTextOfLatestToast().toString());
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("You've nearly doubled your steps. Keep up the good work!"));
    }

    @Test
    public void TestEncouragementDoubleForNoFriendPerson() {
        cont.create();
        currSteps = 2100;
        activity.setCurrSteps(currSteps);
        this.setupDB();

        activity.onResume();
        System.out.println(ShadowToast.getTextOfLatestToast().toString());
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Excellent! You've doubled your steps!"));
    }

    @Test
    public void TestEncouragementForHasFriendPerson() {
        cont.create();
        currSteps = 2100;
        activity.setCurrSteps(currSteps);
        this.setupDBForHasFriendPerson();

        activity.onResume();
        assertNull(ShadowToast.getTextOfLatestToast());
    }


    public void setupDBForHasFriendPerson() {
        UserSession.testSession = userSession;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal1.getTime();
        String currDate = format.format(date1);
        User u1 = new User();
        Mockito.when(userSession.getCurrentUser()).thenReturn(u1);
        u1.setStepGoal(currDate,5100);
        u1.setIntentionalSteps(currDate, 0);
        u1.setDailySteps(currDate, 1000);
        day1 = new StepDataObject(1000, 0, 5100, currDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -1);
        Date date2 = cal2.getTime();
        String preDate = format.format(date2);
        u1.setStepGoal(preDate,5000);
        u1.setIntentionalSteps(preDate, 0);
        u1.setDailySteps(preDate, 1000);
        day2 = new StepDataObject(1000, 0, 5000, preDate);
        u1.addFriend(u1);
    }

    public void setupDB() {
        UserSession.testSession = userSession;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal1.getTime();
        String currDate = format.format(date1);
        User u1 = new User();
        Mockito.when(userSession.getCurrentUser()).thenReturn(u1);
        u1.setStepGoal(currDate,5100);
        u1.setIntentionalSteps(currDate, 0);
        u1.setDailySteps(currDate, 1000);
        day1 = new StepDataObject(1000, 0, 5100, currDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -1);
        Date date2 = cal2.getTime();
        String preDate = format.format(date2);
        u1.setStepGoal(preDate,5000);
        u1.setIntentionalSteps(preDate, 0);
        u1.setDailySteps(preDate, 1000);
        day2 = new StepDataObject(1000, 0, 5000, preDate);
    }

    @Test
    public void testSetGoal(){
        this.setupDB();
        cont.create();
        TextView stepGoalView = activity.findViewById(R.id.step_goal_view);
        stepGoalView.setText("5500");
        activity.setGoal();

        assertThat(stepGoalView.getText().toString(), equalTo(String.valueOf(day1.getDailyStepGoal())));
    }

    @Test
    public void testMetGoalNotification(){
        this.setupDB();
        cont.create();
        TextView stepGoalView = activity.findViewById(R.id.step_goal_view);
        stepGoalView.setText("5500");
        currSteps = 6000;
        activity.setCurrSteps(currSteps);

        activity.onResume();

        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("show goal notification"));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notify", false).apply();
        activity.onResume();

        assertTrue(!ShadowToast.getTextOfLatestToast().toString().equals("show goal notification"));
    }

}