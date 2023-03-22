package edu.ucsd.cse110.team19.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.ucsd.cse110.team19.walkwalkrevolution.backend.RouteViewModel;

public class ProposedWalkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk);
        Intent intent = getIntent();
        System.out.println(intent.getStringExtra("route"));
        System.out.println(intent.getStringExtra("email"));
        TextView noWalkText = findViewById(R.id.proposedWalk_noWalkText);
        TextView date = findViewById(R.id.proposedWalk_date);
        EditText editDate = findViewById(R.id.proposedWalk_editDate);
        TextView scheduledDate = findViewById(R.id.proposedWalk_scheduledDate);
        TextView time = findViewById(R.id.proposedWalk_time);
        EditText editTime = findViewById(R.id.proposedWalk_editTime);
        TextView scheduledTime = findViewById(R.id.proposedWalk_scheduledTime);
        Button proposeButton = findViewById(R.id.proposedWalk_propose);
        Button scheduleButton = findViewById(R.id.proposedWalk_schedule);
        Button withdrawButton = findViewById(R.id.proposedWalk_withdraw);
        Button declineButton = findViewById(R.id.proposedWalk_decline);
        Button acceptButton = findViewById(R.id.proposedWalk_accept);

        if (intent.getStringExtra("route").equals("none")){
            noWalkText.setVisibility(View.VISIBLE);
            date.setVisibility(View.GONE);
            editDate.setVisibility(View.GONE);
            scheduledDate.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            editTime.setVisibility(View.GONE);
            scheduledTime.setVisibility(View.GONE);
            proposeButton.setVisibility(View.GONE);
            scheduleButton.setVisibility(View.GONE);
            withdrawButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
        }
        else{
            if (intent.getStringExtra("email").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                //set up owner ui
                System.out.println("curr user is owner");
                noWalkText.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                editDate.setVisibility(View.VISIBLE);
                scheduledDate.setVisibility(View.GONE);
                time.setVisibility(View.VISIBLE);
                editTime.setVisibility(View.VISIBLE);
                scheduledTime.setVisibility(View.GONE);
                proposeButton.setVisibility(View.VISIBLE);
                scheduleButton.setVisibility(View.VISIBLE);
                withdrawButton.setVisibility(View.VISIBLE);
                declineButton.setVisibility(View.GONE);
                acceptButton.setVisibility(View.GONE);
            }
            else{
                //set up not owner ui
                System.out.println("curr user is not owner");
                noWalkText.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                editDate.setVisibility(View.GONE);
                scheduledDate.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                editTime.setVisibility(View.GONE);
                scheduledTime.setVisibility(View.VISIBLE);
                proposeButton.setVisibility(View.GONE);
                scheduleButton.setVisibility(View.GONE);
                withdrawButton.setVisibility(View.GONE);
                declineButton.setVisibility(View.VISIBLE);
                acceptButton.setVisibility(View.VISIBLE);
            }
        }

    }


}
