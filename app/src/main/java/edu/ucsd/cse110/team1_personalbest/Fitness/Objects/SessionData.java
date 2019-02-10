package edu.ucsd.cse110.team1_personalbest.Fitness.Objects;

public class SessionData {
    private int steps;
    private double distance;
    private long duration;
    private double speed;
    private boolean shouldUpdate;

    public SessionData() {
        //default constructor
        steps = 0;
        speed = 0;
        distance = 0;
        duration = 0;
        shouldUpdate = true;
    }

    public SessionData(int steps, double distance, long duration, double speed, boolean shouldUpdate) {
        this.steps = steps;
        this.distance = distance;
        this.duration = duration;
        this.speed = speed;
        this.shouldUpdate = shouldUpdate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Steps: " + this.steps + "\n Distance: " + this.distance + "\n Speed: " + this.speed
                + "\n Duration: " + this.duration;
    }

    public boolean isShouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
