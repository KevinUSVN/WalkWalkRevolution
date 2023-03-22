package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProposeTeamWalkActivity extends Activity {
    public static final String TEST_MODE = "DEFAULT";
    private String test_mode; // Determines if we use mocked version for testing or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_team_walk);

        test_mode = getIntent().getStringExtra(TEST_MODE);
        if (test_mode == null) {
            test_mode = "DEFAULT";
        }

        Intent intent = getIntent();
        String route_title = intent.getStringExtra("title");

        EditText dateTime = findViewById(R.id.proposeTeamWalk_dateTime);

        Button proposeButton = findViewById(R.id.teamWalk_propose);

        if(test_mode.equals("TEST")){
            proposeButton.setOnClickListener((View v) -> {finish();});
        } else {
            proposeButton.setOnClickListener((View v) -> {
                CollectionReference notifList = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(getUserEmail())
                        .collection("Notifications");

                notifList.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> notif = new HashMap<>();
                        notif.put("status", "proposes");
                        notifList.document("TeamWalk").set(notif);
                    }
                });

                finish();
            });
        }

        Button cancelButton = findViewById(R.id.teamWalk_cancel);
        cancelButton.setOnClickListener((View v) -> {
            finish();
        });
    }

    private String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}
