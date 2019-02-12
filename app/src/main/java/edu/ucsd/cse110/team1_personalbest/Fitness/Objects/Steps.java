package edu.ucsd.cse110.team1_personalbest.Fitness.Objects;

public class Steps {
    private int steps;
    private boolean shouldUpdate;

    public Steps(int steps) {
        this.steps = steps;
        this.shouldUpdate = false;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean isShouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
