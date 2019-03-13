package edu.ucsd.cse110.team1_personalbest.Login.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;

/**
 * Class for handling google {@link LoginService}.
 */
public class GoogleLogInService implements LoginService {

    // permissions for gfit api
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private final Activity activity;

    public GoogleLogInService(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean login() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this.activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        this.activity.startActivityForResult(signInIntent, 50);

        Log.d("Test", "Signing in");
        final FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this.activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this.activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this.activity),
                    fitnessOptions);
        }

        // check if an account has been signed into
        return GoogleSignIn.getLastSignedInAccount(this.activity) != null;
    }

    @Override
    public boolean isLoggedIn() {
        return GoogleSignIn.getLastSignedInAccount(this.activity) != null;
    }

    public static String getLastLoggedInAccount(final Activity activity) {
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(activity);
        if (gsa!=null) {
            return gsa.getEmail();
        }
        return "";
    }
}
