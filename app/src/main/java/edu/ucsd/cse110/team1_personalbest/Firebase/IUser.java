package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.List;
import java.util.Map;

public interface IUser {

    void setName(String name);

    void setEmail(String email);

    String getName();

    String getEmail();

    List<String> getFriends();

    List<String> getPendingRequests();

    boolean hasNoFriends();

    void removeRequest(User user);

    void sendRequest(User user);

    void addFriend(User user);

    void setIntentionalSteps(String date, Integer steps);
    void setStepGoal(String date, Integer steps);
    void setDailySteps(String date, Integer steps);
    int getStepGoal(String date);
    int getIntentionalSteps(String date);
    int getDailySteps(String date);

    void setFriends(List<String> list);
    void setRequests(List<String> list);

}
