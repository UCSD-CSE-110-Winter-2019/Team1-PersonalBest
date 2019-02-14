package edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces;


public interface FitnessObserver {
    public void update(Integer numSteps, Integer deltaSteps, Long elapsedTime, Float deltaDistance);
}
