package com.example.dylan.avatarfitness.Objects;

import java.util.Date;

/**
 * Created by Dylan on 2/19/2015.
 */
public interface iWorkout {
    void LogWorkout();
    String getDescription();
    float getDuration();
    Date getDate();
}
