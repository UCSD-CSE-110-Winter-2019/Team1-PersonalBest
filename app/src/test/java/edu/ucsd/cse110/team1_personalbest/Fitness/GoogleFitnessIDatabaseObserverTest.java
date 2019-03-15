package edu.ucsd.cse110.team1_personalbest.Fitness;

import android.content.Context;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleFitnessIDatabaseObserverTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private FitnessObserver observer;
    @Mock
    TextView steps;
    @Mock
    TextView deltaSteps;
    @Mock
    TextView distance;
    @Mock
    TextView speed;
    @Mock
    TextView time;

    @Captor
    ArgumentCaptor<String> stepCaptor;
    @Captor
    ArgumentCaptor<String> deltaStepCaptor;
    @Captor
    ArgumentCaptor<String> speedCaptor;
    @Captor
    ArgumentCaptor<String> distanceCaptor;
    @Captor
    ArgumentCaptor<String> timeCaptor;

    @Mock
    Context context;

    Integer st;
    Integer dst;
    Float dist;
    Long elapsed;

    String expectedDistance;
    String expectedSpeed;

    @Before
    public void setup() {
        UserSession.testmode = true;
        MainActivity.enable_firestore = false;
        st = 1;
        dst = 0;
        elapsed = 1000L;
        dist = 2.0F;
        this.observer = new GoogleFitnessObserver(null, steps, deltaSteps, speed, distance, time, context);
        double distance = 2 * 0.000621371;
        double speed = 2 * 0.000621371 * 3600;
        expectedDistance = String.format(Locale.ENGLISH, "%.3f",distance);
        expectedSpeed = String.format(Locale.ENGLISH, "%.3f",speed);
        when(this.distance.getText()).thenReturn("0");
    }

    @Test
    public void testAllInputs() {
        this.observer.update(st, dst, elapsed, dist);
        verify(this.steps).setText(stepCaptor.capture());
        verify(this.deltaSteps).setText(deltaStepCaptor.capture());
        verify(this.distance).setText(distanceCaptor.capture());
        verify(this.speed).setText(speedCaptor.capture());
        verify(this.time).setText(timeCaptor.capture());

        Assert.assertEquals("1", stepCaptor.getValue());
        Assert.assertEquals("0", deltaStepCaptor.getValue());
        Assert.assertEquals(expectedSpeed, speedCaptor.getValue());
        Assert.assertEquals(expectedDistance, distanceCaptor.getValue());
    }

    @Test
    public void testNull() {
        this.observer.update(null, null, null, null);
        verify(this.steps, never()).setText(stepCaptor.capture());
        verify(this.deltaSteps, never()).setText(deltaStepCaptor.capture());
        verify(this.distance, never()).setText(distanceCaptor.capture());
        verify(this.speed, never()).setText(speedCaptor.capture());
        verify(this.time, never()).setText(timeCaptor.capture());
    }

    @Test
    public void testZeroTime() {
        this.observer.update(st, dst, 0L, dist);
        verify(this.steps).setText(stepCaptor.capture());
        verify(this.deltaSteps).setText(deltaStepCaptor.capture());
        verify(this.distance).setText(distanceCaptor.capture());
        verify(this.speed).setText(speedCaptor.capture());
        verify(this.time).setText(timeCaptor.capture());

        Assert.assertEquals("1", stepCaptor.getValue());
        Assert.assertEquals("0", deltaStepCaptor.getValue());
        Assert.assertEquals("0.000", speedCaptor.getValue());
        Assert.assertEquals(expectedDistance, distanceCaptor.getValue());
    }

    @Test
    public void testUpdateSteps() {
        this.observer.update(st, dst, null, null);
        verify(this.steps).setText(stepCaptor.capture());
        verify(this.deltaSteps).setText(deltaStepCaptor.capture());
        verify(this.distance, never()).setText(distanceCaptor.capture());
        verify(this.speed, never()).setText(speedCaptor.capture());

        Assert.assertEquals("1", stepCaptor.getValue());
        Assert.assertEquals("0", deltaStepCaptor.getValue());
    }

    @Test
    public void testUpdateDistance() {
        this.observer.update(null, null, elapsed, dist);
        verify(this.steps, never()).setText(stepCaptor.capture());
        verify(this.deltaSteps, never()).setText(deltaStepCaptor.capture());
        verify(this.distance).setText(distanceCaptor.capture());
        verify(this.speed).setText(speedCaptor.capture());

        Assert.assertEquals(expectedSpeed, speedCaptor.getValue());
        Assert.assertEquals(expectedDistance, distanceCaptor.getValue());
    }
}
