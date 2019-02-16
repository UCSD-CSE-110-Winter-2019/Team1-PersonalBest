package edu.ucsd.cse110.team1_personalbest.Firebase;

public interface Subject {

    void register(Observer o);
    void unregister(Observer o);
    void notifyObservers();

}
