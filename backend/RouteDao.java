package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM route_table")
    LiveData<List<Route>> getRoutes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Route route);

    @Update
    void update(Route route);

    @Query("UPDATE route_table SET steps = :steps, miles = :miles WHERE name = :name")
    void updateMetrics(String name, int steps, float miles);

    @Query("DELETE FROM route_table WHERE name = :name")
    void delete(String name);

    @Query("DELETE FROM route_table")
    void deleteAll();
}
