package com.example.dylan.avatarfitness.Objects;

import java.util.Date;

/**
 * Created by Dylan on 2/19/2015.
 */
public class Workout implements iWorkout {
    protected float mDuration;
    protected String mDescription;
    protected int mReps;
    protected int mSets;
    protected Date mDate;

    public Workout(){
        mDuration = 0;
        mReps = 0;
        mSets = 0;
        mDescription = "";
    }

    public Workout( int sets, int reps, String description, float duration, Date date ){
        mDescription = description;
        mDuration = duration;
        mReps = reps;
        mSets = sets;
        mDate = date;
    }

    @Override
    public void LogWorkout() {

    }
}
