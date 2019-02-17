package edu.ucsd.cse110.team1_personalbest.Activities;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity_SetGoalUITest.TEST_SERVICE;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivity_MainUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.INTERNET");

    @Before
    public void setup() {
        monitorCurrentActivity();
    }

    @Test
    public void mainActivity_MainUITest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.step_goal), withText("Step Goal:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Step Goal:")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.step_goal_view), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("0")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.current_step), withText("Daily Step:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Daily Step:")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.current_step_view), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView4.check(matches(withText("0")));

        ViewInteraction button = onView(
                allOf(withId(R.id.buttonStartWalk),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.buttonHistory),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.setGoalMain),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonStartWalk), withText("Start Walk/Run Now"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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

    private void monitorCurrentActivity() {
        mActivityTestRule.getActivity().getApplication()
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
                        if (activity.getClass() == MainActivity.class) {
                            ((MainActivity)activity).setKeys(TEST_SERVICE, TEST_SERVICE);
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
                        }
                    }

                    @Override
                    public void onActivityStarted(final Activity activity) { }

                    @Override
                    public void onActivityResumed(final Activity activity) {
                    }

                    @Override
                    public void onActivityPaused(final Activity activity) { }

                    @Override
                    public void onActivityStopped(final Activity activity) { }

                    @Override
                    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) { }

                    @Override
                    public void onActivityDestroyed(final Activity activity) { }
                });
    }
}
