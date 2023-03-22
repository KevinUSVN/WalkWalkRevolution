package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.team19.walkwalkrevolution.backend.Route;
import edu.ucsd.cse110.team19.walkwalkrevolution.backend.RouteViewModel;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RouteAddActivity extends AppCompatActivity {

    private RouteViewModel mRouteAddModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_add);

        mRouteAddModel = new ViewModelProvider(this).get(RouteViewModel.class);

        Button saveButton = findViewById(R.id.routeAdd_saveRoute);
        saveButton.setOnClickListener((View view) -> saveRoute());

        Button cancelButton = findViewById(R.id.routeAdd_cancelSaveRoute);
        cancelButton.setOnClickListener((View view) -> exitAddActivity());
    }

    private void saveRoute() {
        EditText titleInput = findViewById(R.id.routeAdd_titleInput);
        String title = titleInput.getText().toString();
        if (title.matches("")) {
            Toast.makeText(this, "Can't save route without title", Toast.LENGTH_SHORT).show();
        } else {
            EditText startPointInput = findViewById(R.id.routeAdd_startingPointInput);
            String startPoint = startPointInput.getText().toString();
            if (startPoint.equals("")){
                startPoint = null;
            }

            String difficulty = getInputButtonText(findViewById(R.id.routeAdd_difficultyInput));
            String shape = getInputButtonText(findViewById(R.id.routeAdd_shapeInput));
            String slope = getInputButtonText(findViewById(R.id.routeAdd_slopeInput));
            String pathType = getInputButtonText(findViewById(R.id.routeAdd_pathTypeInput));
            String surface = getInputButtonText(findViewById(R.id.routeAdd_surfaceInput));

            EditText notesInput = findViewById(R.id.routeAdd_notesInput);
            String notes = notesInput.getText().toString();
            if (notes.equals("")) {
                notes = null;
            }

            mRouteAddModel.insert(new Route(title, startPoint, shape,
                    slope, pathType, surface, difficulty, false,
                    notes, 0, 0, getInitials()));
            exitAddActivity();
        }
    }

    private String getInputButtonText(RadioGroup group) {
        RadioButton button = findViewById(group.getCheckedRadioButtonId());
        return (button != null) ? button.getText().toString() : null;
    }

    private void exitAddActivity() {
        finish();
    }

    private String getInitials() { return generateInitials(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()); }


    // Helper method to extract initials from name
    private String generateInitials( String name ){
        StringBuilder result = new StringBuilder();
        if(name.length() == 0 ) return null;

        String[] words = name.split(" ");
        for( String word : words ){
            result.append(Character.toUpperCase(word.charAt(0)));
        }
        return result.toString();
    }
}
