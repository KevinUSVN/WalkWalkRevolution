package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Observable;

import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team19.walkwalkrevolution.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.team19.walkwalkrevolution.team.TeamViewActivity;

public class MainActivity extends AppCompatActivity implements IStepActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TEST_SERVICE = "TEST_SERVICE";
    public static final String SHARED_PREF_FILE = "userprefs";
    public static final String SHARED_PREF_TIME = "lastWalkTime";
    public static final String SHARED_PREF_HEIGHT = "userHeight";
    public static final String SHARED_PREF_STEPS = "lastStepCount";

    // Default Values
    private static final int STEP_INIT = 0;
    private String fitnessServiceKey = "GOOGLE_FIT";
    private String DEFAULT_LAST_TIME = "0 seconds";
    private static final float DEFAULT_USER_HEIGHT = 5.6f; // In ft
    private static final float DEFAULT_STEP_RATIO = 0.4f;
    private static final float FT_MI_RATI0 = 5280;


    private SharedPreferences sp;
    private FitnessService fitnessService;

    //UI elements
    private TextView stepsHome;
    private TextView userMiles;

    // Debug Var
    private static final String TAG = MainActivity.class.getSimpleName();

    private long dailyStepCount;
    private float dailyMileCount;
    private float userHeight;
    private float stepsToFt;

    public static final String USER = "DEFAULT";
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsHome = findViewById(R.id.stepsHome);
        userMiles = findViewById(R.id.milesHome);
        userMiles.setGravity(Gravity.CENTER_HORIZONTAL);

        Button startButton = findViewById(R.id.starButton);
        startButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, WalkScreenActivity.class);
            intent.putExtra("steps", dailyStepCount);
            startActivity(intent);
        });
        Button routeListBtn = findViewById(R.id.routeListBtn);
        routeListBtn.setOnClickListener((View v) ->
                startActivity(new Intent(this,
                        edu.ucsd.cse110.team19.walkwalkrevolution.route.RouteListActivity.class)));
        Button editUserBtn = findViewById(R.id.userBtn);
        editUserBtn.setOnClickListener((View v) ->
                startActivity(new Intent(this, HeightActivity.class)));
        Button teamButton = findViewById(R.id.teamButton);
        teamButton.setOnClickListener((View v) ->
                startActivity(new Intent(this, TeamViewActivity.class)));
        Button teamWalkButton = findViewById(R.id.teamWalkButton);
        teamWalkButton.setOnClickListener((View v) ->
                startActivity(new Intent(this, ViewTeamWalkActivity.class)));

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener((View v) -> signOut());

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Get previous walk/run data from shared preferences file
        SharedPreferences userPrefs = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);

        // Get the previous walk/run step count
        long lastStepCount = userPrefs.getLong(SHARED_PREF_STEPS, dailyStepCount);
        userHeight = userPrefs.getFloat(SHARED_PREF_HEIGHT,  DEFAULT_USER_HEIGHT);
        stepsToFt = userHeight * DEFAULT_STEP_RATIO;
        TextView lastSteps = findViewById(R.id.last_step_text);
        lastSteps.setText(Long.toString(lastStepCount));

        // Get the previous walk/run time
        String lastWalkTime = userPrefs.getString(SHARED_PREF_TIME, DEFAULT_LAST_TIME);
        TextView lastTime = findViewById(R.id.last_time_text);
        lastTime.setText(lastWalkTime);

        // Get the previous walk/run miles
        TextView lastMiles = findViewById(R.id.last_mile_text);
        float lastMilesCount = Conversion.stepsToMiles(lastStepCount, stepsToFt);
        String formattedLastMiles = Conversion.formatMiles(lastMilesCount);
        lastMiles.setText(formattedLastMiles);

        /*
            Set up Google Fit Adapter
         */
        fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY); //Obtain key from test service

        if (fitnessServiceKey == null) { // Use default (GoogleFitAdapter) key
            fitnessServiceKey = FITNESS_SERVICE_KEY;
        }

        //Use GoogleFitAdapter or Mock Fitness Service
        if (!fitnessServiceKey.equals(TEST_SERVICE)) {
            Log.d(TAG, "GoogleFitAdapter Used");
            FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(IStepActivity activity) {
                    return new GoogleFitAdapter(activity);
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

        // Update Button2
        Button buttonUpdate = findViewById(R.id.updateHomeButton);
        buttonUpdate.setOnClickListener((View v) -> fitnessService.updateStepCount());
    }

    /*
        Gets notified from GoogleFitAdapter updates
     */
    public void update(Observable o, Object arg) {
        runOnUiThread(() -> fitnessService.updateStepCount());
    }

    public void setStepCount(long stepCount) {
        dailyStepCount = stepCount;
        dailyMileCount = Conversion.stepsToMiles(dailyStepCount, stepsToFt);
        stepsHome.setText(String.valueOf(dailyStepCount));
        userMiles.setText(Conversion.formatMiles(dailyMileCount));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "Signed Out",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    //  Used for testing
    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

}
