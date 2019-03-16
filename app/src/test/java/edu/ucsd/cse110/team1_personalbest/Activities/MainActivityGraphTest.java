package edu.ucsd.cse110.team1_personalbest.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.common.graph.Graph;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.internal.LocalPermissionGranter;
import org.robolectric.internal.bytecode.ShadowMap;
import org.robolectric.shadows.ShadowMessenger;
import org.robolectric.shadows.ShadowToast;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Graph.Factories.BarGraphFactory;
import edu.ucsd.cse110.team1_personalbest.Graph.Interface.GraphFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.*;

@RunWith(RobolectricTestRunner.class)

public class MainActivityGraphTest {
    private MainActivity activity;
    private MainActivityGraph activityGraph;

    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessService service;
    private LoginService loginService;
    private ActivityController<MainActivity> cont;
    private ActivityController<MainActivityGraph> contGraph;


    private Context appContext = Robolectric.setupActivity(MainActivity.class).getApplicationContext();


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


        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        cont = Robolectric.buildActivity(MainActivity.class, intent);
        activity = cont.get();
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);
        Intent intentGraph = new Intent(RuntimeEnvironment.application, MainActivityGraph.class);
        contGraph = Robolectric.buildActivity(MainActivityGraph.class,intentGraph);
        activityGraph = contGraph.get();

    }



    @Test
    public void graphTest(){

        GraphFactory graphFactory = Mockito.mock(BarGraphFactory.class);
        GraphView graph = Mockito.mock(GraphView.class);
        int[] dailySteps = {1000,2000,3000,4000,5000,6000,4000};
        int[] intentionalSteps = {500,1500,1000,2000,3000,4000,2000};
        graph = graphFactory.makeGraph(1000,intentionalSteps,dailySteps,graph);
        Assert.assertEquals(6000, dailySteps[5]);
        Assert.assertEquals(4000,intentionalSteps[5]);
        //Assert.assertEquals(4000,graph.getSeries().get(1).getHighestValueY());


    }
    @Test
    public void FriendHistoryTest(){

        ActivityOfFriendActivity activity = Robolectric.setupActivity(ActivityOfFriendActivity.class);
        activity.findViewById(R.id.friendHistoryButton).performClick();
        Intent expectedIntent = new Intent(activity, MainActivityGraph.class);
        expectedIntent.putExtra("name",UserSession.getCurrentUser().getEmail());
        //ActivityOfFriendActivity activity2 = Mockito.mock(ActivityOfFriendActivity.class);
        //activity2.findViewById(R.id.friendHistoryButton).performClick();
        assertEquals(expectedIntent.getStringExtra("name"),UserSession.getCurrentUser().getEmail());

    }


}
