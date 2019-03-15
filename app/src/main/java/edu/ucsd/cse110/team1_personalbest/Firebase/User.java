package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements IUser {

    private String name;
    private String email;
    private List<String> friends;
    private List<String> friendRequests;
    Map<String, Map<String, Integer>> graphData;
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
    public boolean hasFriends() {
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
    }

    @Override
    public void setStepGoal(String date, Integer steps) {
        if (steps == null) return;
        if (steps == 0) return;
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        today.put(stepGoalKey, steps);
    }

    @Override
    public void setDailySteps(String date, Integer steps) {
        if (steps == null) return;
        if (steps == 0) return;
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        today.put(dailyStepKey, steps);
    }

    @Override
    public int getStepGoal(String date) {
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        Integer steps = today.get(stepGoalKey);
        return steps == null ? 0 : steps;
    }

    @Override
    public int getIntentionalSteps(String date) {
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        Integer steps = today.get(intentionalKey);
        return steps == null ? 0 : steps;
    }

    @Override
    public int getDailySteps(String date) {
        Map<String,Integer> today = graphData.get(date);
        if (today == null) today = new HashMap<>();
        Integer steps = today.get(dailyStepKey);
        return steps == null ? 0 : steps;
    }


    @Override
    public Map<String, Integer> getGraphData(String date) {
        if (graphData.get(date) == null) return new HashMap<>();
        return graphData.get(date);
    }

}
