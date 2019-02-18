package edu.ucsd.cse110.team1_personalbest.Fitness.Adapters;

import android.app.Activity;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.TimeUnit;


import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;


/**
 * An implementation of {@link FitnessService} for the google fit api.
 */
public class GoogleFitAdapter implements FitnessService,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // permissions for gfit api
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this)
            & 0xFFFF;
    private static final String TAG = "[GoogleFitAdapter]";

    private Activity activity;

    private GoogleSignInAccount googleAccount;
    private GoogleApiClient mClient;
    private  List<FitnessObserver> observers;

    private int initialNumSteps;

    private static final String START_TIME = "START_TIME";

    /**
     * Constructs an instance of the adapter for the given activity
     */
    public GoogleFitAdapter(final Activity activity, int initialNumSteps) {
        this.activity = activity;
        this.observers = new ArrayList<>();
        this.initialNumSteps = initialNumSteps;
    }

    public void setInitialNumSteps(int initialNumSteps) {
        this.initialNumSteps = initialNumSteps;
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

    @Override
    public void startListening() {

        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;

        this.mClient = new GoogleApiClient.Builder(this.activity)
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mClient.connect();


        updateStepCount();
        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_DISTANCE_DELTA)
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Fitness.SensorsApi.add(mClient,
                                    new SensorRequest.Builder()
                                            .setDataSource(dataSource)
                                            .setDataType(dataSource.getDataType())
                                            .setSamplingRate(5, TimeUnit.SECONDS)
                                            .build(),
                                    new OnDataPointListener() {
                                        @Override
                                        public void onDataPoint(DataPoint dataPoint) {
                                            String msg = "onDataPoint: ";
                                            for (Field field : dataPoint.getDataType().getFields()) {
                                                Value value = dataPoint.getValue(field);
                                                switch (field.getName().toLowerCase()) {
                                                    case "distance":
                                                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
                                                        long startTime = pref.getLong(START_TIME, 0);
                                                        long timeElapsed = 0;
                                                        if (startTime != 0)
                                                        {
                                                            timeElapsed = new Date().getTime() - pref.getLong(START_TIME, 0);
                                                        } else {
                                                            SharedPreferences.Editor editor = pref.edit();
                                                            editor.putLong(START_TIME, new Date().getTime());
                                                            editor.apply();
                                                        }
                                                        notifyObservers(null, null, timeElapsed, value.asFloat());
                                                        updateStepCount();
                                                        break;
                                                }

                                            }

                                        }
                                    })
                                    .setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            if (status.isSuccess()) {
                                                Log.i(TAG, "client connected.");
                                            } else {
                                                Log.e(TAG, "Client Failed to connect");
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    @Override
    public void stopListening() {
        if (this.mClient != null && this.mClient.isConnected())
            this.mClient.disconnect();
    }

    @Override
    public void removeObservers() {
        Log.d(TAG, "Clearing observers");
        if (observers != null) observers.clear();
    }

    @Override
    public void registerObserver(FitnessObserver observer) {
        Log.d(TAG, "Registering observer");
        if (observers != null) observers.add(observer);
    }

    /**
     * Start recording steps.
     */
    private void startRecordingSteps() {
        Log.i(TAG, "Subscribing to steps");
        this.subscribeToDataType(DataType.TYPE_STEP_COUNT_DELTA);
    }

    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    private void updateStepCount() {
        if (this.googleAccount == null) GoogleSignIn.getLastSignedInAccount(this.activity);
        if (this.googleAccount == null) return;
        Fitness.getHistoryClient(activity, googleAccount)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(final DataSet dataSet) {
                                Log.d(TAG, "Data request result: " + dataSet.toString());
                                if(!dataSet.isEmpty()) {
                                    int value = dataSet.getDataPoints().get(0).getValue(
                                            Field.FIELD_STEPS).asInt();
                                    notifyObservers(value, value-initialNumSteps, null, null);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {
                                Log.e(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

    private void subscribeToDataType(final DataType dt) {
        if(googleAccount == null) return;
        Fitness.getRecordingClient(this.activity, this.googleAccount)
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
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "service connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "client connection lost");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Conection failed attempting resolution...");
        try {
            connectionResult.startResolutionForResult(activity, 1);
            this.activity.finish();
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Resolution attempt failed", e);
            e.printStackTrace();
        }
    }

    private void notifyObservers(Integer numSteps, Integer deltaSteps, Long timeElapsed, Float distance) {
        if(observers != null) {
            Log.d(TAG, "notifiying observers");
            for (FitnessObserver observer : observers) {
                observer.update(numSteps, deltaSteps, timeElapsed, distance);
            }
        }
    }

    public static void putSessionStartTime(final Activity activity) {
        Log.d(TAG, "session started");
        /* save start time */
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        final SharedPreferences.Editor editor = pref.edit();
        editor.putLong(START_TIME, new Date().getTime());
        editor.commit();
    }
}
