package edu.ucsd.cse110.team19.walkwalkrevolution.route;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Route {
    private String name;
    private String startPoint;
    private String shape;
    private String slope;
    private String pathType;
    private String surface;
    private String difficulty;
    private Boolean favorite;
    private String notes;
    private int steps;
    private double miles;
    private String initials;

    public Route() {} //empty constructor for firebase?

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
