package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.team19.walkwalkrevolution.team.Member;

public class RouteRepository {
    private RouteDao mRouteDao;
    private LiveData<List<Route>> mAllRoutes;
    String current_user_email;
    CollectionReference currentUserRouteList;

    RouteRepository(Application application) {
        RouteRoomDatabase db = RouteRoomDatabase.getDatabase(application);
        mRouteDao = db.routeDao();
        mAllRoutes = mRouteDao.getRoutes();

        current_user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        currentUserRouteList = FirebaseFirestore.getInstance() // Current User
                .collection("Users")
                .document(current_user_email)
                .collection("Routes");
    }

    LiveData<List<Route>> getAllRoutes() {
        return mAllRoutes;
    }

    void insert(Route route) {
        Log.v("backend", "attempting to insert new Route into database");
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> mRouteDao.insert(route));
        // if a registered user is found, upload route data to Firestore
        FirebaseFirestore.getInstance().collection("Users").document(current_user_email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserRouteList.document().set(route);
                    }
                });
    }

    void update(Route route) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> mRouteDao.update(route));
    }

    void updateMetrics(String name, int steps, float miles) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> mRouteDao.updateMetrics(name, steps, miles));
    }

    void delete(String name) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> mRouteDao.delete(name));
    }

    void deleteAll() {
        Log.v("backend", "deleting all Routes from database");
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> mRouteDao.deleteAll());
        FirebaseFirestore.getInstance().collection("Users").document(current_user_email).collection("Routes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            ds.getReference().delete();
                        }
                    }
                });
    }
}
