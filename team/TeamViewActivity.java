package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team19.walkwalkrevolution.MockFirebase.MockFirebaseAuth;
import edu.ucsd.cse110.team19.walkwalkrevolution.MockFirebase.MockFirebaseFirestore;
import edu.ucsd.cse110.team19.walkwalkrevolution.R;
import edu.ucsd.cse110.team19.walkwalkrevolution.storage.Team;

public class TeamViewActivity extends FragmentActivity implements addMemberPopup.PopupListener {

    private TeamListFirestoreAdapter adapter;
    FloatingActionButton addBtn;
    private static final String TAG = "TeamView";

    private String sender_token;

    //Testing Variables
    public static final String TEST_MODE = "DEFAULT";
    private String test_mode; // Determines if we use mocked version for testing or not
    MockFirebaseAuth mockFirebaseAuth;
    MockFirebaseFirestore mockFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_view);

        test_mode = getIntent().getStringExtra(TEST_MODE);
        if (test_mode == null) {
            test_mode = "DEFAULT";
        }

        //Create Mock Objects
        mockFirebaseAuth = new MockFirebaseAuth();
        mockFirebaseFirestore = new MockFirebaseFirestore();

        if (test_mode.equals("TEST")) {
            Log.d(TAG, "MOCKED FIREBASE Used");
            mockAddUserRecordToFirestore();
            mockBuildRecyclerView();
        }
        else {
            addUserRecordToFirestore();
            addTeamToFirestore();
            buildRecyclerView();
        }

        // Respond to add button
        addBtn = findViewById(R.id.addMemberBtn);
        addBtn.setOnClickListener(view -> openPopup());
    }

    private void addUserRecordToFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                sender_token = deviceToken;
                Map<String,Object> userProfile = new HashMap<>();
                userProfile.put("name", name);
                userProfile.put("email", email);
                userProfile.put("uid", uid);
                userProfile.put("token",deviceToken);

                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(email)
                        .set(userProfile);
            }
        });
    }

    private void addTeamToFirestore() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Team team1 = new Team(email);
    }

    private void buildRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("Users").document(getUserEmail()).collection("Teammates").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();

        adapter = new TeamListFirestoreAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.teamView_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    /*
    Mocked Version of addUserRecordToFirestore()
     */
    private void mockAddUserRecordToFirestore() {
        String uid = mockFirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = mockFirebaseAuth.getInstance().getCurrentUser().getEmail();
        String name = mockFirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        // Mock Upload data to cloud

        String deviceToken = mockFirebaseAuth.getInstance().getCurrentUser().getToken();

        Map<String,Object> userProfile = new HashMap<>();
        userProfile.put("name", name);
        userProfile.put("email", email);
        userProfile.put("uid", uid);
        userProfile.put("token", deviceToken);

        mockFirebaseFirestore.getInstance().collection("MOCK_COLLECTION").document("MOCK_DOCUMENT").set(userProfile);
    }

    private void mockBuildRecyclerView() {
        //Pretend UI gets updated
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!test_mode.equals("TEST")) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!test_mode.equals("TEST")) {
            adapter.stopListening();
        }
    }

    public void openPopup() {
        addMemberPopup addMemberPopup = new addMemberPopup();
        addMemberPopup.show(getSupportFragmentManager(), "add teammate popup");
    }

    // Helper method to insert the name and email entered by user to textView
    @Override
    public void applyTexts(String name, String email) {
        String sender_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String sender_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        CollectionReference teamListRef = FirebaseFirestore.getInstance() // Current User
                .collection("Users")
                .document(sender_email)
                .collection("Teammates");


        CollectionReference teamListInvitee = FirebaseFirestore.getInstance() // User to send to
                .collection("Users")
                .document(email)
                .collection("Teammates");

        CollectionReference notificationCollection = FirebaseFirestore.getInstance() // User to send to
                .collection("Users")
                .document(email)
                .collection("Notifications");

        CollectionReference teammateRouteList = FirebaseFirestore.getInstance() // Current User
                .collection("Users")
                .document(sender_email)
                .collection("Teammates");

        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    // if a registered user is found, create with id and token
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String id = task.getResult().getString("uid");
                            String token = task.getResult().getString("token");
                            boolean friendStatus = false;

                            teamListRef.document(email).set(new Member(name, email, id, token, friendStatus));
                            teamListInvitee.document(sender_email).set(new Member(sender_name, sender_email, sender_uid, sender_token));

                            // create notification token and name
                            Map<String, Object> notif = new HashMap<>();
                            notif.put("name", sender_name);
                            notif.put("email", sender_email);
                            notif.put("token", sender_token);
                            notificationCollection.document(sender_email).set(notif);

                        }
                        // otherwise, just create with name and emailgit
                        else{
                            teamListRef.document(email).set(new Member(name, email));
                            teamListInvitee.document(sender_email).set(new Member(sender_name, sender_email));

                            // create notification token and name
                            Map<String, Object> notif = new HashMap<>();
                            notif.put( "name", sender_name);
                            notificationCollection.document(email).set(notif);
                        }
                    }
                });

    }


    private String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public MockFirebaseFirestore getMockFirebaseFirestore() {
        return mockFirebaseFirestore;
    }
}
