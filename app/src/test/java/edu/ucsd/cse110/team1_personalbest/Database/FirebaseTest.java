package edu.ucsd.cse110.team1_personalbest.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.TestIDatabaseObserver;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;

import static org.mockito.ArgumentMatchers.any;

@RunWith(RobolectricTestRunner.class)
public class FirebaseTest {

    private Database db;
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCol;
    private DocumentReference mockRef;
    private Task<Void> mockTask;
    private Task<DocumentSnapshot> mockTaskGet;
    private MainActivity activity;
    private Map<String, User> map = new HashMap<>();
    private TestIDatabaseObserver obs;

    @Before
    public void setup() {
        UserSession.testmode = true;
        activity = Robolectric.setupActivity(MainActivity.class);
        mockFirestore = Mockito.mock(FirebaseFirestore.class);
        db = new Database(mockFirestore);
        mockCol = Mockito.mock(CollectionReference.class);
        mockRef = Mockito.mock(DocumentReference.class);
        mockTask = Mockito.mock(Task.class);
        mockTaskGet = Mockito.mock(Task.class);
        MainActivity.enable_firestore = false;

        Mockito.when(mockFirestore.collection("users")).thenReturn(mockCol);
        Mockito.when(mockCol.document("users")).thenReturn(mockRef);
        Mockito.when(mockRef.set(map)).thenReturn(mockTask);
        Mockito.when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        Mockito.when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);
        Mockito.when(mockRef.get()).thenReturn(mockTaskGet);
        Mockito.when(mockTaskGet.addOnSuccessListener(any())).thenReturn(mockTaskGet);
        Mockito.when(mockTaskGet.addOnFailureListener(any())).thenReturn(mockTaskGet);

        obs = new TestIDatabaseObserver();
        db.register(obs);
    }

    @After
    public void complete() {

    }

    @Test
    public void testPush() {
        db.setUsers(map);
        Mockito.verify(mockFirestore.collection("users").document("users")).set(map);
    }

    @Test
    public void testGet() {
        db.getUsers();
        Mockito.verify(mockFirestore.collection("users").document("users")).get();
    }

    // example of how to use Firestore
    private void checkFriends() {
        TestIDatabaseObserver obs = new TestIDatabaseObserver();
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
        evan.setDailySteps("03-01-2019", 1);
        evan.setIntentionalSteps("03-01-2019", 2);

        evan.addFriend(bob);
        bob.addFriend(evan);
        users.put(bob.getEmail(), bob);
        users.put(joe.getEmail(), joe);
        users.put(evan.getEmail(), evan);
        db.setUsers(users);
        db.getUser("test2@gmail.com");
        db.getUsers();

        // single user push
        Map<String, Object> user = new HashMap<>();
        User test = new User();
        test.setName("test");
        test.setEmail("foo@ucsd.edu");
        user.put(test.getEmail(), test);
        db.setUser(user);
    }
}