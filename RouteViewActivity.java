package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RouteViewActivity extends Activity {
    public static final String TEST_MODE = "DEFAULT";
    private String test_mode; // Determines if we use mocked version for testing or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        test_mode = getIntent().getStringExtra(TEST_MODE);
        if (test_mode == null) {
            test_mode = "DEFAULT";
        }

        TextView routeTitleView = findViewById(R.id.routeView_title);
        TextView routeStartingPoint = findViewById(R.id.routeView_startingPoint);
        TextView routeDifficulty = findViewById(R.id.routeView_difficulty);
        TextView routeShape = findViewById(R.id.routeView_shape);
        TextView routeSlope = findViewById(R.id.routeView_slope);
        TextView routePathType = findViewById(R.id.routeView_pathType);
        TextView routeSurface = findViewById(R.id.routeView_surface);
        TextView routeNotes = findViewById(R.id.routeView_notes);

        Intent intent = getIntent();
        routeTitleView.setText(intent.getStringExtra("title"));
        routeStartingPoint.setText(intent.getStringExtra("startingPoint"));
        routeDifficulty.setText(intent.getStringExtra("difficulty"));
        routeShape.setText(intent.getStringExtra("shape"));
        routeSlope.setText(intent.getStringExtra("slope"));
        routePathType.setText(intent.getStringExtra("pathType"));
        routeSurface.setText(intent.getStringExtra("surface"));
        routeNotes.setText(intent.getStringExtra("notes"));

        Button proposeTeamWalkButton = findViewById(R.id.propose_walk);
        if(test_mode.equals("TEST")){
            proposeTeamWalkButton.setOnClickListener((View v) -> mockLaunchProposedWalkActivity());
        } else {
            proposeTeamWalkButton.setOnClickListener((View v) -> launchProposeTeamWalkActivity());
        }
    }

    private void launchProposeTeamWalkActivity() {
        Intent intent = new Intent(this, ProposeTeamWalkActivity.class);
        intent.putExtra("title", getIntent().getStringExtra("title"));
        startActivity(intent);
    }

    private void mockLaunchProposedWalkActivity(){
        Intent intent = new Intent(this, ProposeTeamWalkActivity.class);
        intent.putExtra(RouteViewActivity.TEST_MODE, test_mode);
        startActivity(intent);
    }
}
