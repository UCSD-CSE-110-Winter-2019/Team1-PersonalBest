package edu.ucsd.cse110.team1_personalbest.Firebase;

public class StepDataObject implements IDataObject {
    private int dailyStepCount;
    private int dailyIntentionalStepCount;
    private int dailyStepGoal;
    private String date;

    public StepDataObject(int dailyStepCount, int dailyIntentionalStepCount, int dailyStepGoal, String date) {
        this.dailyStepCount = dailyStepCount;
        this.dailyIntentionalStepCount = dailyIntentionalStepCount;
        this.dailyStepGoal = dailyStepGoal;
        this.date = date;
    }

    public int getDailyStepGoal() {
        return dailyStepGoal;
    }

    public void setDailyStepGoal(int dailyStepGoal) {
        this.dailyStepGoal = dailyStepGoal;
    }

    public int getDailyIntentionalStepCount() {
        return dailyIntentionalStepCount;
    }

    public void setDailyIntentionalStepCount(int dailyIntentionalStepCount) {
        this.dailyIntentionalStepCount = dailyIntentionalStepCount;
    }

    public int getDailyStepCount() {
        return dailyStepCount;
    }

    public void setDailyStepCount(int dailyStepCount) {
        this.dailyStepCount = dailyStepCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
