package edu.ucsd.cse110.team19.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Observable;

import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.GoogleFitAdapter;

public class WalkScreenActivity extends AppCompatActivity implements IStepActivity {
    private static final String TAG = "WalkScreenActivity";
    public static final String SHARED_PREF_FILE = "userprefs";
    public static final String SHARED_PREF_TIME = "lastWalkTime";
    public static final String SHARED_PREF_STEPS = "lastStepCount";
    public static final String SHARED_PREF_HEIGHT = "userHeight";
    private static final float DEFAULT_USER_HEIGHT = 5.6f; // In ft
    private static final float DEFAULT_STEP_RATIO = 0.4f;

    private Chronometer timer;
    private TextView stepWalk;
    private TextView stepMile;
    private float currMile;
    private float stepsToFt;
    private long initStep;
    private long currStep;
    private long startTime, endTime;

    private String fitnessServiceKey;
    private FitnessService fitnessService;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate the UI
        setContentView(R.layout.activity_walk_screen);


        // Get fitness API key from home screen and launch instance which this activity can observe
        fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);

        if (fitnessServiceKey == null) { // Use default (GoogleFitAdapter) key
            fitnessServiceKey = FITNESS_SERVICE_KEY;
        }

        if (!fitnessServiceKey.equals(TEST_SERVICE))
        {
            Log.d(TAG, "GoogleFitAdapter Used");
            FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(IStepActivity mainActivity) {
                    return new GoogleFitAdapter(mainActivity);
                }
            });
            fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
            fitnessService.setup();
            fitnessService.addObserverFit(this);
            fitnessService.initUpdate();
        }
        else {
            fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
            Log.d(TAG, "Mock Fitness Service Test Used");
        }


        // Load in height data
        SharedPreferences userPrefs = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        float height = userPrefs.getFloat(SHARED_PREF_HEIGHT, DEFAULT_USER_HEIGHT);
        stepsToFt = DEFAULT_STEP_RATIO * height;

        // Get daily steps passed from the home screen
        stepWalk = findViewById(R.id.active_steps);
        stepMile = findViewById(R.id.active_miles);

        currStep = 0;
        initStep = getIntent().getLongExtra("steps", 0);
        stepWalk.setText(String.valueOf(currStep));

        currMile = 0.0f;
        stepMile.setText(Conversion.formatMiles(currMile));

        // Set up stopwatch (count-up) timer
        timer = (Chronometer) findViewById(R.id.active_walk_time);
        timer.start(); // Start timer
        startTime = timer.getBase(); // Get the start time

        // Listen for when the "end run" button is pressed
        Button stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWalk();
            }
        });

        // Update Button2
        Button buttonUpdate = findViewById(R.id.updateWalkButton);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
            }
        });

    }

    public void setStepCount(long stepCount) {
        // Offset from step count for the day before run began
        currStep = stepCount - initStep;
        currMile = Conversion.stepsToMiles(currStep, stepsToFt);
        stepWalk.setText(String.valueOf(currStep));
        stepMile.setText(Conversion.formatMiles(currMile));
    }

    /*
        Gets notified from GoogleFitAdapter updates
     */
    public void update(Observable o, Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fitnessService.updateStepCount();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }


    protected void endWalk() {
        // Save time in shared preferences
        SharedPreferences userPrefs = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        userPrefs.edit().putString(SHARED_PREF_TIME, timer.getText().toString())
                        .putLong(SHARED_PREF_STEPS, currStep).apply();
        timer.stop();
        endTime = SystemClock.elapsedRealtime() - startTime;

        // Switch back to Home screen
        finish();
    }
}
