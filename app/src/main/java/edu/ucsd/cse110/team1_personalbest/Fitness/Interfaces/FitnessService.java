package edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces;

import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.Steps;

/**
 * An interface for all Step-counting services.
 */
public interface FitnessService {
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
    void updateStepCount(final Steps steps);

    /* sessions stuff */
    void startListening();
    void stopListening();
    void updateSessionData(final SessionData sessionData);
}
