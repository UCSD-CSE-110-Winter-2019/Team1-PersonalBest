package edu.ucsd.cse110.team1_personalbest.Activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

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


import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

@RunWith(RobolectricTestRunner.class)
public class CountStepActivityTest {
    private CountStepActivity activity;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<CountStepActivity> cont;

    @Before
    public void setUp() throws Exception {
        UserSession.testmode = true;
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


        Intent intent = new Intent(RuntimeEnvironment.application, CountStepActivity.class);
        cont = Robolectric.buildActivity(CountStepActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);
    }

    @Test
    public void loginFailed() {
        Mockito.when(this.loginService.isLoggedIn()).thenReturn(false);
        cont.create();
        Mockito.verify(this.loginService).isLoggedIn();
        Mockito.verify(this.service, Mockito.never()).setup();
        Assert.assertEquals("No google account found", ShadowToast.getTextOfLatestToast());
        Assert.assertTrue(this.activity.isFinishing());
    }

    @Test
    public void loginSuccess() {
        Mockito.when(this.loginService.isLoggedIn()).thenReturn(true);
        ArgumentCaptor<FitnessObserver> obsCaptor = ArgumentCaptor.forClass(FitnessObserver.class);
        cont.create();
        Mockito.verify(this.service).registerObserver(obsCaptor.capture());
        Mockito.verify(this.service).setup();
        Mockito.verify(this.service).startListening();
        Assert.assertNotNull(obsCaptor.getValue());
    }
}

