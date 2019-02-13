package edu.ucsd.cse110.team1_personalbest.Login.Interfaces;

import android.app.Activity;

/**
 * Interface for log in services.
 */
public interface LoginService {

    /**
     * prompts user to log in
     * @return
     */
    public boolean login();
    public boolean isLoggedIn();
}
