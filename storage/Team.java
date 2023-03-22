package edu.ucsd.cse110.team19.walkwalkrevolution.storage;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team{
    private String team_name;
    private ArrayList<String> emails;
    private Map<String,String> user_roles;
    private String curr_walk;
    private String curr_walk_status;
    private String curr_walk_date;
    private String curr_walk_time;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference team_ref = db.collection("Teams");
    private Map<String,Object> team_map;

    public Team(String email) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        this.emails = new ArrayList<String>();
        this.emails.add(email);
        this.team_name = "team "+email;

        this.team_map = new HashMap<>();
        team_map.put("team_name",team_name);
        team_map.put("emails",emails);
        team_ref.document(team_name).set(team_map);
    }

    public Team() {}     // empty construction needed for Firestore

    public Team(String email,String lol){
        System.out.println("entered construction");
        Query team_query = team_ref.whereArrayContains("emails",email);
        team_query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            System.out.println("team found");
                            System.out.println(document.get("team_name"));
                        }
                    }
                } else{
                    //This Toast will be displayed only when you'll have an error while getting documents.
                    System.out.println("query not found");
                }
            }
        });
        System.out.println("meh");
        System.out.println(team_query.toString());
        //List<DocumentSnapshot> team_doc = team_query.getDocuments();
        //System.out.println(team_doc.size());
        //this.team_map = team_doc.get(0).getData();
        //System.out.println(team_map.get("team_name"));
    }

}
