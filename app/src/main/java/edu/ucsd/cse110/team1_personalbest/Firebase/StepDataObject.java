package edu.ucsd.cse110.team1_personalbest.Firebase;

public class StepDataObject implements IDataObject {
    private int dailyStepCount;
    private int preDailyStepCount;
    private int dailyStepGoal;
    private String date;

    public StepDataObject(int dailyStepCount, int preDailyStepCount, int dailyStepGoal, String date) {
        this.dailyStepCount = dailyStepCount;
        this.preDailyStepCount = preDailyStepCount;
        this.dailyStepGoal = dailyStepGoal;
        this.date = date;
    }

    public int getDailyStepGoal() {
        return dailyStepGoal;
    }

    public void setDailyStepGoal(int dailyStepGoal) {
        this.dailyStepGoal = dailyStepGoal;
    }

    public int getPreDailyStepCount() {
        return preDailyStepCount;
    }

    public void setPreDailyStepCount(int preDailyStepCount) {
        this.preDailyStepCount = preDailyStepCount;
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
