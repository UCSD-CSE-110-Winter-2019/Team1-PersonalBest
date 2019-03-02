package edu.ucsd.cse110.team1_personalbest.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.TestObserver;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;

@RunWith(RobolectricTestRunner.class)
public class FirebaseTest {

    private Database db;
    private MainActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
        db = new Database(activity.getApplicationContext());
    }

    @After
    public void complete() {

    }

    @Test
    public void checkFriends() {

        TestObserver obs = new TestObserver();
        db.register(obs);

        Map<String, User> users = new HashMap<>();
        User bob = new User();
        bob.setName("Bob");
        bob.setEmail("test@gmail.com");

        User joe = new User();
        joe.setName("Joe");
        joe.setEmail("test1@gmail.com");

        User evan = new User();
        evan.setName("Evan");
        evan.setEmail("test2@gmail.com");
        Map<String, Integer> steps = new HashMap<>();
        steps.put("daily_steps", 1);
        steps.put("intentional_steps", 2);
        evan.setGraphData("03-01-2019", steps);

        evan.addFriend(bob);
        bob.addFriend(evan);
        users.put(bob.getEmail(), bob);
        users.put(joe.getEmail(), joe);
        users.put(evan.getEmail(), evan);
        db.setUsers(users);
        db.getUser("test2@gmail.com");
        db.getUsers();
    }

}
