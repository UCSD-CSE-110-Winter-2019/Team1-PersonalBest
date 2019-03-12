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
    public void setFriends(List<String> friends) {
        this.friends = friends;
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
    public void removeRequest(User user) {
        friendRequests.remove(user.getEmail());
    }

    @Override
    public void setRequests(List<String> requests) {
        this.friendRequests = requests;
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
    public void setGraphData(String date, Map<String, Integer> daySteps) {
        graphData.put(date, daySteps);
    }

    @Override
    public Map<String, Integer> getGraphData(String date) {
        return graphData.get(date);
    }

}
