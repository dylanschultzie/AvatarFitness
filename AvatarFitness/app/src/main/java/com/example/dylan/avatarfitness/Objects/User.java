package com.example.dylan.avatarfitness.Objects;

import java.util.List;

/**
 * Created by Dylan on 2/16/2015.
 */
public class User {
    private Avatar mAvatar;
    private List<iStatistic> mStatistic;
    private List<iWorkout> mWorkouts;

    public User(){

    }
    public void AddWorkout( Workout workout ){
        mWorkouts.add(workout);
    }
}
