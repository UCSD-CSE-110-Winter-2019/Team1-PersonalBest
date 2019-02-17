package edu.ucsd.cse110.team1_personalbest.Firebase;

public interface IDataObject {

    int getDailyStepGoal();

    void setDailyStepGoal(int dailyStepGoal);

    int getDailyIntentionalStepCount();

    void setDailyIntentionalStepCount(int dailyIntentionalStepCount);

    int getDailyStepCount();

    void setDailyStepCount(int dailyStepCount);

    String getDate();

    void setDate(String date);

}
