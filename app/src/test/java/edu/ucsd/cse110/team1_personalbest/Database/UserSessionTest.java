package edu.ucsd.cse110.team1_personalbest.Database;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.ucsd.cse110.team1_personalbest.Firebase.IUserSession;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;

public class UserSessionTest {
    IUserSession sess = Mockito.mock(IUserSession.class);
    @Before
    public void setup() {
        UserSession.testmode=true;
        UserSession.testSession = sess;
    }

    @Test
    public void testSetup() {
        UserSession.setup(null);
        Mockito.verify(sess).setup(null);
    }
    @Test
    public void testAddFriend() {
        UserSession.addFriend("email", null);
        Mockito.verify(sess).addFriend("email", null);
    }
    @Test
    public void testGetCurrentUser() {
        User i = new User("name", "email");
        Mockito.when(sess.getCurrentUser()).thenReturn(i);
        UserSession.setCurrentUser(i);
        User u = UserSession.getCurrentUser();
        Assert.assertEquals(u.getName(), "name");
        Assert.assertEquals("email", u.getEmail());
        Mockito.verify(sess).getCurrentUser();
        Mockito.verify(sess).setCurrentUser(i);
    }
    @Test
    public void testGetUser() {
        User i = new User("name", "email");
        Mockito.when(sess.getUser("email")).thenReturn(i);
        User u = UserSession.getUser("email");
        Assert.assertEquals(u.getName(), "name");
        Assert.assertEquals("email", u.getEmail());
        Mockito.verify(sess).getUser("email");
    }
}
