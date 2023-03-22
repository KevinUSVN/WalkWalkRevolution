package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

@Entity(tableName = "route_table")
public class Route {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "start_point")
    private String startPoint;

    @ColumnInfo(name = "shape")
    private String shape;

    @ColumnInfo(name = "slope")
    private String slope;

    @ColumnInfo(name = "path_type")
    private String pathType;

    @ColumnInfo(name = "surface_type")
    private String surface;

    @ColumnInfo(name = "difficulty")
    private String difficulty;

    @ColumnInfo(name = "favorite")
    private Boolean favorite;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "steps")
    private int steps;

    @ColumnInfo(name = "miles")
    private double miles;

    @ColumnInfo(name = "initials")
    private String initials;

    public Route(@NonNull String name,
                 String startPoint,
                 String shape,
                 String slope,
                 String pathType,
                 String surface,
                 String difficulty,
                 Boolean favorite,
                 String notes,
                 int steps,
                 double miles,
                 String initials) throws IllegalArgumentException {

        String[] valid_shapes = {null, "loop", "out-and-back"};
        String[] valid_slopes = {null, "flat", "hilly"};
        String[] valid_pathTypes = {null, "street", "trail"};
        String[] valid_surfaces = {null, "even", "uneven"};
        String[] valid_difficulties = {null, "easy", "moderate", "difficult"};
        if (Arrays.asList(valid_shapes).contains(shape) &&
            Arrays.asList(valid_slopes).contains(slope) &&
            Arrays.asList(valid_pathTypes).contains(pathType) &&
            Arrays.asList(valid_surfaces).contains(surface) &&
            Arrays.asList(valid_difficulties).contains(difficulty)) {
            this.name = name;
            this.startPoint = startPoint;
            this.shape = shape;
            this.slope = slope;
            this.pathType = pathType;
            this.surface = surface;
            this.difficulty = difficulty;
            this.favorite = favorite;
            this.notes = notes;
            this.steps = steps;
            this.miles = miles;
            this.initials = initials;
            //Log.v("backend", "successfully inserted new Route into database");
        } else {
            throw new IllegalArgumentException("Invalid input for categorical column");
        }
    }

    public @NonNull String getName() { return this.name; }
    public String getStartPoint() { return this.startPoint; }
    public String getShape() { return this.shape; }
    public String getSlope() { return this.slope; }
    public String getPathType() { return this.pathType; }
    public String getSurface() { return this.surface; }
    public String getDifficulty() { return this.difficulty; }
    public Boolean getFavorite() { return this.favorite; }
    public String getNotes() { return this.notes; }
    public int getSteps() { return this.steps; }
    public double getMiles() { return this.miles; }
    public String getInitials() { return this.initials; }

}
