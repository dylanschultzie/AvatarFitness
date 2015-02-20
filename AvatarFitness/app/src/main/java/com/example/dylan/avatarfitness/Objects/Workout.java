package com.example.dylan.avatarfitness.Objects;

import java.util.Date;

/**
 * Created by Dylan on 2/19/2015.
 */
public class Workout implements iWorkout {
    protected float mDuration;
    protected int mIntensity;
    protected String mDescription;
    protected String mName;
    protected Date mDate;

    public Workout(){
        mDuration = 0;
        mIntensity = 0;
        mDescription = "";
        mName = "";
    }

    public Workout( String name, String description, float duration, Date date ){
        mDescription = description;
        mDuration = duration;
        mName = name;
        mDate = date;
    }

    public Workout( String name, String description, float duration, Date date, int intensity ){
        mDescription = description;
        mIntensity = intensity;
        mDuration = duration;
        mDate = date;
        mName = name;
    }

    @Override
    public void LogWorkout() {

    }
}
