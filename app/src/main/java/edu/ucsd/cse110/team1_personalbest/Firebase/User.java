package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements IUser {

    private String name;
    private String email;
    private List<String> friends;
    private List<String> friendRequests;
    private Map<String, Map<String, Integer>> graphData;
    public static final String dailyStepKey = "daily_steps";
    public static final String intentionalKey = "intentional_steps";
    public static final String stepGoalKey = "step_goal";

    public User() {
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        graphData = new HashMap<>();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getFriends() {
        return friends;
    }

    @Override
    public List<String> getPendingRequests() {
        return friendRequests;
    }

    @Override
    public boolean hasNoFriends() {
        return friends.isEmpty();
    }

    @Override
    public void removeRequest(User user) {
        friendRequests.remove(user.getEmail());
    }

    @Override
    public void sendRequest(User user) {
        if ( !friendRequests.contains(user.getEmail()) ) {
            friendRequests.add(user.getEmail());
        }
    }

    @Override
    public void addFriend(User user) {
        if ( !friends.contains(user.getEmail())) {
            friends.add(user.getEmail());
        }
    }

    @Override
    public void setIntentionalSteps(String date, Integer steps) {
        if (steps == null) return;
        if (steps == 0) return;
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        today.put(intentionalKey, steps);
        graphData.put(date, today);
    }

    @Override
    public void setStepGoal(String date, Integer steps) {
        if (steps == null) return;
        if (steps == 0) return;
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        today.put(stepGoalKey, steps);
        graphData.put(date, today);
    }

    @Override
    public void setDailySteps(String date, Integer steps) {
        if (steps == null) return;
        if (steps == 0) return;
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        today.put(dailyStepKey, steps);
        graphData.put(date, today);
    }

    @Override
    public int getStepGoal(String date) {
        Map today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        if (today.get(stepGoalKey) == null) return 500;
        Log.d("test", today.get(stepGoalKey).toString());
        if (today.get(stepGoalKey) instanceof Long) {
            return ((Long) today.get(stepGoalKey)).intValue();
        }
        else return (int)today.get(stepGoalKey);
    }

    @Override
    public int getIntentionalSteps(String date) {
        Map today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        if (today.get(intentionalKey) == null) return 0;
        if (today.get(intentionalKey) instanceof Long) {
            return ((Long) today.get(intentionalKey)).intValue();
        }
        else return (int)today.get(intentionalKey);
    }

    @Override
    public int getDailySteps(String date) {
        Map today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        if (today.get(dailyStepKey) == null) return 0;
        if (today.get(dailyStepKey) instanceof Long) {
            return ((Long) today.get(dailyStepKey)).intValue();
        }
        else return (int)today.get(dailyStepKey);
    }

    @Override
    public void setFriends(List<String> list) {
        this.friends = list;
    }

    @Override
    public void setRequests(List<String> list) {
        this.friendRequests = list;
    }

    public Map getGraphData() {
        return graphData;
    }

    public void setGraphData(Map<String,Map<String,Integer>> map) {
        graphData = map;
    }

}
