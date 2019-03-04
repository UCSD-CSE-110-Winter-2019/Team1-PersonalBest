package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements IUser {

    private String name;
    private String email;
    private List<String> friends;
    Map<String, Map<String, Integer>> graphData;

    public User() {
        friends = new ArrayList<>();
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
    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    @Override
    public List<String> getFriends() {
        return friends;
    }

    @Override
    public void addFriend(User user) {
        if ( !friends.contains(user.getEmail())) {
            friends.add(user.getEmail());
        }
    }

    @Override
    public void setGraphData(String date, Map<String, Integer> daySteps) {
        graphData.put(date, daySteps);
    }

    @Override
    public Map<String, Integer> getGraphData(String date) {
        return graphData.get(date);
    }

}
