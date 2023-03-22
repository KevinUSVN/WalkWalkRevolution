package edu.ucsd.cse110.team19.walkwalkrevolution.route;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.ucsd.cse110.team19.walkwalkrevolution.R;
import edu.ucsd.cse110.team19.walkwalkrevolution.RouteAddActivity;

public class RouteListActivity extends AppCompatActivity {

    private RadioButton myRoutesButton;
    private RadioButton teamRoutesButton;
    private RoutesListFirestoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        this.myRoutesButton = findViewById(R.id.routeList_myRoutes);
        this.teamRoutesButton = findViewById(R.id.routeList_teamRoutes);
        myRoutesButton.setOnClickListener((View v) -> switchToMyRoutes());
        teamRoutesButton.setOnClickListener((View v) -> switchToTeamRoutes());

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener((View v) ->
                startActivity(new Intent(this, RouteAddActivity.class)));
        buildMyRoutesRecycler();
    }

    public void switchToMyRoutes(){
        myRoutesButton.setBackgroundResource(R.color.colorAccent);
        myRoutesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        teamRoutesButton.setBackgroundResource(R.color.colorWhite);
        teamRoutesButton.setTextColor(getResources().getColor(R.color.textGrey));
        buildMyRoutesRecycler();
    }

    public void switchToTeamRoutes(){
        myRoutesButton.setBackgroundResource(R.color.colorWhite);
        myRoutesButton.setTextColor(getResources().getColor(R.color.textGrey));
        teamRoutesButton.setBackgroundResource(R.color.colorAccent);
        teamRoutesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        buildMyTeamRoutesRecycler();
    }

    public void buildMyRoutesRecycler(){
        System.out.println("entered build recycler method");
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Routes")
                .orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Route> options = new FirestoreRecyclerOptions.Builder<Route>()
                .setQuery(query, Route.class)
                .build();
        adapter = new RoutesListFirestoreAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void buildMyTeamRoutesRecycler(){
        System.out.println("entered build recycler method for teams");

        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(getUserEmail())
                .collection("Team Routes")
                .orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Route> options = new FirestoreRecyclerOptions.Builder<Route>()
                .setQuery(query, Route.class)
                .build();

        adapter = new RoutesListFirestoreAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

}
