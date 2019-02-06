package edu.ucsd.cse110.team1_personalbest.Steps.Interfaces;

/**
 * An interface for all Step-counting services.
 */
public interface StepService {
    /**
     * returns the integer request code.
     * @return int
     */
    int getRequestCode();

    /**
     * initializes connection to service
     */
    void setup();

    /**
     * syncs step count with service.
     */
    void updateStepCount();
}
