package edu.ucsd.cse110.team1_personalbest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.intercepting.SingleActivityFactory;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity_CountStepUITest;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity_SetGoalUITest.TEST_SERVICE;

public class MainActivityTestRule extends ActivityTestRule<MainActivity> {
    public MainActivityTestRule(Class activityClass) {
        super(activityClass);
    }

    public MainActivityTestRule(Class activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode);
    }

    public MainActivityTestRule(Class activityClass, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }

    public MainActivityTestRule(SingleActivityFactory activityFactory, boolean initialTouchMode, boolean launchActivity) {
        super(activityFactory, initialTouchMode, launchActivity);
    }

    public MainActivityTestRule(Class activityClass, @NonNull String targetPackage, int launchFlags, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, targetPackage, launchFlags, initialTouchMode, launchActivity);
    }

    @Override
    public void beforeActivityLaunched() {
        MainActivity activity = getActivity();
        LoginServiceFactory.put(TEST_SERVICE, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return new TestLoginService();
            }
        });

        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new TestFitnessService();
            }
        });
        activity.setKeys(TEST_SERVICE, TEST_SERVICE);
    }

    private class TestLoginService implements LoginService {

        @Override
        public boolean login() {
            return true;
        }

        @Override
        public boolean isLoggedIn() {
            return true;
        }
    }

    private class TestFitnessService implements FitnessService {

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {

        }

        @Override
        public void startListening() {

        }

        @Override
        public void stopListening() {

        }

        @Override
        public void removeObservers() {

        }

        @Override
        public void registerObserver(FitnessObserver observer) {

        }
    }

}
