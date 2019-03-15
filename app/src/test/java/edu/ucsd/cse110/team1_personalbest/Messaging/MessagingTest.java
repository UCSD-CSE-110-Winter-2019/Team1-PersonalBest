package edu.ucsd.cse110.team1_personalbest.Messaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.Query;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Activities.MessageActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.IUser;
import edu.ucsd.cse110.team1_personalbest.Firebase.IUserSession;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.R;

@RunWith(RobolectricTestRunner.class)
public class MessagingTest {
    private IMessagingService service = Mockito.mock(IMessagingService.class);
    private ActivityController<MessageActivity> cont;
    private MessageActivity activity;
    private ArgumentCaptor<String> stringCaptor;
    private IUserSession sess = Mockito.mock(IUserSession.class);
    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(RuntimeEnvironment.application, MessageActivity.class);
        intent.putExtra("name", "testuser");
        cont = Robolectric.buildActivity(MessageActivity.class, intent);
        MessagingServiceFactory.setTestService(service);
        MessagingServiceFactory.toggleTestMode();
        UserSession.testmode = true;
        UserSession.testSession = sess;
        User u = new User("email", "email");
        Mockito.when(sess.getCurrentUser()).thenReturn(u);
        activity = cont.get();
    }

    @Test
    public void testMessageOrder() {
        ArgumentCaptor<Query.Direction> dirCaptor = ArgumentCaptor.forClass(Query.Direction.class);
        cont.create();
        Mockito.verify(this.service).init(Mockito.any(),dirCaptor.capture(), Mockito.any());
        Assert.assertEquals(Query.Direction.ASCENDING,dirCaptor.getValue());
    }

    @Test
    public void testSendMessage() {
        ArgumentCaptor<Query.Direction> dirCaptor = ArgumentCaptor.forClass(Query.Direction.class);
        cont.create();
        Button btnSendMessage = (Button) activity.findViewById(R.id.buttonSendMessage);
        btnSendMessage.callOnClick();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.service).sendMessage(Mockito.anyMap(), Mockito.any(),captor.capture());
        Assert.assertEquals("email~testuser", captor.getValue());
    }

}
