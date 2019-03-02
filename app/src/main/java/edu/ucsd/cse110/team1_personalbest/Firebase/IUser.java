package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.List;
import java.util.Map;

public interface IUser {

    void setName(String name);

    void setEmail(String email);

    String getName();

    String getEmail();

    List<String> getFriends();

    void addFriend(User user);

    void setFriends(List<String> friends);

    void setGraphData(String date, Map<String, Integer> steps);

    Map<String, Integer> getGraphData(String date);

}