package edu.ucsd.cse110.team1_personalbest.Fitness.Adapters;

import android.app.Activity;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
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
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.SessionReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.team1_personalbest.Activities.CountStepActivity;
import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.Steps;
import edu.ucsd.cse110.team1_personalbest.R;


/**
 * An implementation of {@link FitnessService} for the google fit api.
 */
public class GoogleFitAdapter implements FitnessService,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // permissions for gfit api
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this)
            & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private Activity activity;

    private GoogleSignInAccount googleAccount;
    private GoogleApiClient mClient;

    private static final String START_TIME = "START_TIME";

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

    @Override
    public void startListening(int initialNumSteps) {

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

        /* save start time */
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        final SharedPreferences.Editor editor = pref.edit();
        final Steps steps = new Steps(0);
        editor.putLong(START_TIME, new Date().getTime());
        editor.putLong(MainActivity.STEP_KEY, initialNumSteps);
        editor.apply();

        updateStepCount(steps);

        final List<String> datasources = new ArrayList<>();
        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_DISTANCE_DELTA)
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                        datasources.clear();
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Device device = dataSource.getDevice();
                            String fields = dataSource.getDataType().getFields().toString();
                            datasources.add(device.getManufacturer() + " " + device.getModel()
                                    + " [" + dataSource.getDataType().getName() + " " + fields + "]");

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
                                                        double cv = Double.parseDouble(((TextView) activity.findViewById(R.id.distance_view)).getText().toString());
                                                        ((TextView) activity.findViewById(R.id.distance_view)).setText(String.format(Locale.ENGLISH,"%.2f", value.asFloat() + cv));
                                                        long timeElapsed = new Date().getTime() - pref.getLong(START_TIME, 0);
                                                        double speed = (value.asFloat() + cv) / (timeElapsed/1000);
                                                        ((TextView) activity.findViewById(R.id.speed_view)).setText(String.format(Locale.ENGLISH,"%.2f", speed));

                                                        int initial_steps = pref.getInt(MainActivity.STEP_KEY, 0);
                                                        if (steps.isShouldUpdate()) {
                                                            updateStepCount(steps);
                                                            ((TextView) activity.findViewById(R.id.exercise_step_view)).setText(Integer.toString(steps.getSteps() - initial_steps));
                                                            ((TextView) activity.findViewById(R.id.step_goal_view)).setText(Integer.toString(steps.getSteps()));
                                                        }
                                                        break;
                                                }

                                            }

                                        }
                                    })
                                    .setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            if (status.isSuccess()) {

                                            } else {
                                                Toast.makeText(activity, "fail", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    @Override
    public void stopListening() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(START_TIME, 0);
        editor.apply();
        if (this.mClient.isConnected())
            this.mClient.disconnect();
    }

    /**
     * Start recording steps.
     */
    private void startRecordingSteps() {
        this.subscribeToDataType(DataType.TYPE_STEP_COUNT_DELTA);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(activity, "suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, "failed " + connectionResult, Toast.LENGTH_LONG).show();
        try {
            connectionResult.startResolutionForResult(activity, 1);
            this.activity.finish();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}
