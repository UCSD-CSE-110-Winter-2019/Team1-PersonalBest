package edu.ucsd.cse110.team1_personalbest.Fitness.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.Steps;


/**
 * An implementation of {@link FitnessService} for the google fit api.
 */
public class GoogleFitAdapter implements FitnessService {

    // permissions for gfit api
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this)
            & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private Activity activity;
    private String currentSessionName;

    private Session session;
    private Long sessionStartTime;

    private GoogleSignInAccount googleAccount;
    private OnDataPointListener mListener;
    /**
     * Constructs an instance of the adapter for the given activity
     */
    public GoogleFitAdapter(final Activity activity) {
        this.activity = activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void setup() {

        // check permissions again
        this.googleAccount = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) {
            Toast.makeText(this.activity,"No Google Account Signed In", Toast.LENGTH_LONG)
                    .show();
        }

        final FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(this.googleAccount,
                fitnessOptions)) {
            // if no permissions
            Toast.makeText(this.activity,
                    "This app requires permissions to run please grant permissions",
                    Toast.LENGTH_LONG).show();
            this.activity.finish();
        } else {
            this.startRecordingSteps();
        }


    }

    /**
     * Start recording steps.
     */
    private void startRecordingSteps() {
        this.subscribeToDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE);
    }

    /**
     * Start a walk/run session
     */
    public void startListening() {

        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;
        if (this.session != null) {
            Toast.makeText(this.activity, "Activity already in progress", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // subscribe to data
        this.subscribeToDataType(DataType.AGGREGATE_STEP_COUNT_DELTA);
        this.subscribeToDataType(DataType.TYPE_DISTANCE_DELTA);
        this.subscribeToDataType(DataType.TYPE_ACTIVITY_SEGMENT);
        this.subscribeToDataType(DataType.TYPE_SPEED);

        this.sessionStartTime = new Date().getTime();
        this.currentSessionName = "RUN_SESSION: " + sessionStartTime;
        // start the session
        this.session = new Session.Builder()
                .setName(currentSessionName)
                .setDescription("Run on: " + new Date().getTime())
                .setIdentifier(currentSessionName)
                .setStartTime(this.sessionStartTime, TimeUnit.MILLISECONDS)
                .build();

        Task<Void> response = Fitness.getSessionsClient(this.activity, googleAccount)
                .startSession(this.session)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    /**
     * Stop a walk/run session
     */
    public void stopListening() {
        this.session = null;
        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;
        Fitness.getSessionsClient(this.activity, googleAccount)
                .stopSession(this.session.getIdentifier());
    }

    public void updateSessionData(final SessionData sessionData) {
        sessionData.setShouldUpdate(false);
        if (this.session == null) return;
        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;
        if (this.sessionStartTime == null) this.sessionStartTime = new Date().getTime();

        SessionReadRequest readRequest = new SessionReadRequest.Builder()
                .setTimeInterval(this.sessionStartTime, new Date().getTime(), TimeUnit.MILLISECONDS)
                .read(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .read(DataType.TYPE_SPEED)
                .read(DataType.TYPE_DISTANCE_DELTA)
                .read(DataType.TYPE_ACTIVITY_SEGMENT)
                .setSessionName(currentSessionName)
                .build();

        Fitness.getSessionsClient(this.activity, this.googleAccount)
                .readSession(readRequest)
                .addOnSuccessListener(new OnSuccessListener<SessionReadResponse>() {
                    @Override
                    public void onSuccess(SessionReadResponse sessionReadResponse) {
                        List<DataSet> data = sessionReadResponse.getDataSet(sessionReadResponse.getSessions().get(0));
                        for (DataSet datum : data) {
                            if (datum.isEmpty()) continue;
                            DataPoint d = datum.getDataPoints().get(0);
                            if (d.getValue(Field.FIELD_STEPS) != null)
                                sessionData.setSteps(d.getValue(
                                        Field.FIELD_STEPS).asInt());
                            sessionData.setDistance(d.getValue(
                                    Field.FIELD_DISTANCE).asFloat());
                            sessionData.setDuration(Long.parseLong(d
                                    .getValue(Field.FIELD_DURATION).asString()));
                            sessionData.setSpeed(d.getValue(
                                    Field.FIELD_SPEED).asFloat());

                            }
                        sessionData.setShouldUpdate(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Failed to read data from session request",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    @Override
    public void updateStepCount(final Steps steps) {
        steps.setShouldUpdate(false);
        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;
        Fitness.getHistoryClient(activity, googleAccount)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(final DataSet dataSet) {
                                Log.d(TAG, dataSet.toString());
                                if(!dataSet.isEmpty()) {
                                    steps.setSteps(dataSet.getDataPoints().get(0).getValue(
                                            Field.FIELD_STEPS).asInt());
                                }
                                steps.setShouldUpdate(true);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {
                                // TODO add error handling
                                Toast.makeText(activity, "FAIL", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

    private Task<Void> subscribeToDataType(final DataType dt) {
        if(googleAccount == null) return null;
        return Fitness.getRecordingClient(this.activity, this.googleAccount)
                .subscribe(dt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO add error handling
                        Toast.makeText(activity, "FAIL on datatype " + dt.getName() + e, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }
}
