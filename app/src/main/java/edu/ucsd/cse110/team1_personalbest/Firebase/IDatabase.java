package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.Map;

public interface IDatabase extends IDatabaseSubject {
    void putDataObject(IDataObject object);
    IDataObject readDataObject(String date);
    void getUser(String email);
    void setUser(Map<String, Object> user);
    void getUsers();
    void setUsers(Map<String, User> users);
}
