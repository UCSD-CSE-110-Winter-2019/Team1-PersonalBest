package edu.ucsd.cse110.team1_personalbest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DatabaseUnitTest {

    @Before
    public void setup() {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
    }

    @Test
    public void testPush() {
    }

    @Test
    public void testGet() {

    }
}
