package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Route.class}, version = 1, exportSchema = false)
abstract public class RouteRoomDatabase extends RoomDatabase {

    abstract public RouteDao routeDao();

    private static volatile RouteRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RouteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RouteRoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.v("backend", "building Room database");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RouteRoomDatabase.class, "route_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
