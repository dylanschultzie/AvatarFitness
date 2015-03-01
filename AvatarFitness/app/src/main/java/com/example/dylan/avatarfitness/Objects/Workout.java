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

    protected int mWeight;

    public Workout(){
        mDuration = 0;
        mReps = 0;
        mSets = 0;
        mDescription = "";
    }

    public Workout( int sets, int reps, String description, float duration, Date date, int weight ){
        mDescription = description;
        mDuration = duration;
        mReps = reps;
        mSets = sets;
        mDate = date;
        mWeight = weight;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    public float getDuration() {
        return mDuration;
    }

    public void setDuration(float mDuration) {
        this.mDuration = mDuration;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getReps() {
        return mReps;
    }

    public void setReps(int mReps) {
        this.mReps = mReps;
    }

    public int getSets() {
        return mSets;
    }

    public void setSets(int mSets) {
        this.mSets = mSets;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public void LogWorkout() {

    }
}
