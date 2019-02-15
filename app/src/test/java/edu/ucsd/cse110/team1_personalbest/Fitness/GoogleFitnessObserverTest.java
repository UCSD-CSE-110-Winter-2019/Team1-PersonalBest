package edu.ucsd.cse110.team1_personalbest.Fitness;

import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleFitnessObserverTest {
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

    @Captor
    ArgumentCaptor<String> stepCaptor;
    @Captor
    ArgumentCaptor<String> deltaStepCaptor;
    @Captor
    ArgumentCaptor<String> speedCaptor;
    @Captor
    ArgumentCaptor<String> distanceCaptor;

    Integer st;
    Integer dst;
    Float dist;
    Long elapsed;


    @Before
    public void setup() {
        st = 1;
        dst = 0;
        elapsed = 1000L;
        dist = 2.0F;
        this.observer = new GoogleFitnessObserver(steps, deltaSteps, speed, distance, null);
        when(this.distance.getText()).thenReturn("0");
    }

    @Test
    public void testAllInputs() {
        this.observer.update(st, dst, elapsed, dist);
        verify(this.steps).setText(stepCaptor.capture());
        verify(this.deltaSteps).setText(deltaStepCaptor.capture());
        verify(this.distance).setText(distanceCaptor.capture());
        verify(this.speed).setText(speedCaptor.capture());

        Assert.assertEquals("1", stepCaptor.getValue());
        Assert.assertEquals("0", deltaStepCaptor.getValue());
        Assert.assertEquals("2.00", speedCaptor.getValue());
        Assert.assertEquals("2.00", distanceCaptor.getValue());
    }

    @Test
    public void testNull() {
        this.observer.update(null, null, null, null);
        verify(this.steps, never()).setText(stepCaptor.capture());
        verify(this.deltaSteps, never()).setText(deltaStepCaptor.capture());
        verify(this.distance, never()).setText(distanceCaptor.capture());
        verify(this.speed, never()).setText(speedCaptor.capture());
    }

    @Test
    public void testZeroTime() {
        this.observer.update(st, dst, 0L, dist);
        verify(this.steps).setText(stepCaptor.capture());
        verify(this.deltaSteps).setText(deltaStepCaptor.capture());
        verify(this.distance).setText(distanceCaptor.capture());
        verify(this.speed).setText(speedCaptor.capture());

        Assert.assertEquals("1", stepCaptor.getValue());
        Assert.assertEquals("0", deltaStepCaptor.getValue());
        Assert.assertEquals("0.00", speedCaptor.getValue());
        Assert.assertEquals("2.00", distanceCaptor.getValue());
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

        Assert.assertEquals("2.00", speedCaptor.getValue());
        Assert.assertEquals("2.00", distanceCaptor.getValue());
    }
}
