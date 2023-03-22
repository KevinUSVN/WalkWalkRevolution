package edu.ucsd.cse110.team19.walkwalkrevolution.fitness;

import android.app.Activity;

import java.util.Observer;

import edu.ucsd.cse110.team19.walkwalkrevolution.MainActivity;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void addObserverFit(Observer mainActivity);
    void initUpdate();
}
