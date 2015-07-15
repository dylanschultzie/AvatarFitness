package com.example.dylan.avatarfitness.Objects;

import java.util.Date;

/**@author Dylan Schultz
 * Date Created: 2/19/2015
 * Date Last Revised: 7/14/2015
 *
 * Purpose:
 *      Much like Run class, contains the data within each exercise, such as bench press.
 *
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

    @Override
    public float getDuration() {
        return mDuration;
    }

    public void setDuration(float mDuration) {
        this.mDuration = mDuration;
    }

    @Override
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

    @Override
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

}
