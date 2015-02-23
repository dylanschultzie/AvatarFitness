package com.example.dylan.avatarfitness.Objects;

import java.util.ArrayList;

/**
 * Created by Dylan on 2/16/2015.
 */
public class User {
    private Avatar mAvatar;
    private ArrayList<iStatistic> mStatistic;
    private ArrayList<iWorkout> mWorkouts;

    public User(){
        mStatistic = new ArrayList<>();
        mWorkouts = new ArrayList<>();
    }
    public void AddWorkout( iWorkout workout ){
        mWorkouts.add(workout);
    }
}
