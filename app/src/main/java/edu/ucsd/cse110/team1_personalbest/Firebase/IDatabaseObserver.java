package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.Map;

public interface IDatabaseObserver {
    void update(User user);
    void update(Map<String, User> allUsers);
}

