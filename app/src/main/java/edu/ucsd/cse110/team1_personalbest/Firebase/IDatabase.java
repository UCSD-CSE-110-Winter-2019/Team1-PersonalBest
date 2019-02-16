package edu.ucsd.cse110.team1_personalbest.Firebase;

public interface IDatabase {
    void putDataObject(IDataObject object);
    IDataObject readDataObject(String date);
}
