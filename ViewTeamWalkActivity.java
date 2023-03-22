package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class ViewTeamWalkActivity extends Activity {

    private static final String TAG = ViewTeamWalkActivity.class.getSimpleName();
    public static final String TEST_MODE = "DEFAULT";
    public static boolean buttonIsDisplayed = false;
    private String test_mode; // Determines if we use mocked version for testing or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team_walk);

        test_mode = getIntent().getStringExtra(TEST_MODE);
        if (test_mode == null) {
            test_mode = "DEFAULT";
        }

        Intent intent = getIntent();

        String viewer_email;
        String owner_email;
        String route_title;

        if(test_mode.equals("TEST")){
            viewer_email = "mock_email";
            owner_email = "owner_mock_email";
            route_title = "mock_route";
        } else { //add legit code here
            viewer_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            owner_email = "mymekeff@gmail.com";
            route_title = "temp rout ename hardcodeed";
        }

        TextView noWalkText = findViewById(R.id.teamWalk_noWalkText);
        TextView dateTime = findViewById(R.id.viewTeamWalk_dateTime);
        Button yesButton = findViewById(R.id.teamWalk_yes);
        Button noButton = findViewById(R.id.teamWalk_no);
        Button googleMapsButton = findViewById(R.id.googleMapsButton);

        if (route_title == null || route_title.equals("none")) {
            dateTime.setVisibility(View.GONE);
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            noWalkText.setVisibility(View.VISIBLE);
        } else {
            dateTime.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            noWalkText.setVisibility(View.GONE);
            buttonIsDisplayed = true;
        }

        if (owner_email != null && owner_email.equals(viewer_email)) {
            yesButton.setText(R.string.schedule);
            noButton.setText(R.string.withdraw);
        } else {
            yesButton.setText(R.string.accept);
            noButton.setText(R.string.decline);
        }

        yesButton.setOnClickListener((View v) -> {
            System.out.println("yup");
            sendTeamNotification("Accept");
            finish();
        });
        noButton.setOnClickListener((View v) -> {
            System.out.println("nope");
            sendTeamNotification("Decline");
            finish();
        });

        String latitude = String.valueOf(49.08);
        String longitude = String.valueOf(122.45);
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        googleMapsButton.setOnClickListener(view -> {
            startActivity(mapIntent);
        });
        if(!test_mode.equals("TEST")) {
            subscribeToNotificationsTopic();
        }
    }

    private void sendTeamNotification(String notificationType) {
        CollectionReference notifList = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Notifications");

        if (notificationType.equals("Accept")) {
            notifList.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Map<String, Object> notif = new HashMap<>();
                    notif.put("status", "accepts");
                    notifList.document("TeamWalk").set(notif);
                }
            });
        }
        else if (notificationType.equals("Decline")) {
            notifList.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Map<String, Object> notif = new HashMap<>();
                    notif.put("status", "declines");
                    notifList.document("TeamWalk").set(notif);
                }
            });
        }
    }

    private String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    private void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("TeamWalk")
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to TeamWalk notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to TeamWalk notifications failed";
                            }
                            Log.d(TAG, msg);
                            //Toast.makeText(ViewTeamWalkActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
