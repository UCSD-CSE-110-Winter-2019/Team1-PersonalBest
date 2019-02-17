package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import junit.framework.Assert;

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

import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<MainActivity> cont;

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


        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        cont = Robolectric.buildActivity(MainActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);

        // Set up for testing encouragement
        /*
        currSteps = 1900;
        activity.setCurrSteps(currSteps);*/


        /*
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal1.getTime();
        String currDate = format.format(date1);
        day1 = new StepDataObject(1000, 0, 5000, currDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -1);
        Date date2 = cal2.getTime();
        String preDate = format.format(date2);
        day2 = new StepDataObject(1000, 0, 5000, preDate);


        activity.setDataBase(day1, day2);*/
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
    public void TestEncouragementNearlyDouble() {
        //activity.onResume();
        //System.out.println(ShadowToast.getTextOfLatestToast().toString());
        //ShadowToast.getTextOfLatestToast().toString(), equalTo("You've nearly doubled your steps. Keep up the good work!"));
    }
}
