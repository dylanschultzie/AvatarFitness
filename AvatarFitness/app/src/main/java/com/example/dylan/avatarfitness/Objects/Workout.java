package com.example.dylan.avatarfitness.Objects;

/**
 * Created by Dylan on 2/19/2015.
 */
public class Workout implements iWorkout {
    protected float mDuration;
    protected int mIntensity;
    protected String mDescription;
    protected String mName;

    public Workout(){
        mDuration = 0;
        mIntensity = 0;
        mDescription = "";
        mName = "";
    }

    public Workout( String name, String description, float duration ){
        mDescription = description;
        mDuration = duration;
        mName = name;
    }

    public Workout( String name, String description, float duration, int intensity ){
        mDescription = description;
        mIntensity = intensity;
        mDuration = duration;
        mName = name;
    }

    @Override
    public void LogWorkout() {

    }
}
