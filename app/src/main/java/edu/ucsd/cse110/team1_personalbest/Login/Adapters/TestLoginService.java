package edu.ucsd.cse110.team1_personalbest.Login.Adapters;

import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

public class TestLoginService implements LoginService {

    @Override
    public boolean login() {
        return true;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

}