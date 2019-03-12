package edu.ucsd.cse110.team1_personalbest.Firebase;

public interface IDatabase extends IDatabaseSubject {
    void putDataObject(IDataObject object);
    IDataObject readDataObject(String date);
    void getUser(String email);
}
