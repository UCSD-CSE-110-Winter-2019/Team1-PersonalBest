package edu.ucsd.cse110.team1_personalbest.Firebase;

public interface IDatabaseSubject {

    void register(IDatabaseObserver o);
    void unregister(IDatabaseObserver o);
    void notifyObservers();

}
