package com.example.dylan.avatarfitness.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**@author Dylan Schultz
 * Date Created: 2/22/2015
 * Date Last Revised: 7/14/2015
 *
 *Purpose:
 *      Cardio workouts are logged through this. Such a terrible choice in name.
 *
 */
public class Run implements iWorkout {
    protected float mDuration;
    protected Date mDate;
    private ArrayList<LatLng> mRoute = new ArrayList<>();
    private float mDistance;

    public Run(){
        mDuration = 0;
    }

    public Run(float duration, Date date, ArrayList<LatLng> list, float distance ){
        mDuration = duration;
        mDistance = distance;
        mDate = date;
        mRoute = list;
    }

    @Override
    public Date getDate(){
        return mDate;
    }
    @Override
    public float getDuration(){
        return mDuration;
    }
    public ArrayList<LatLng> getRoute(){
        return mRoute;
    }
    public float getDistance(){
        return mDistance;
    }

    @Override
    public String getDescription() {
        return "Run";
    }
}
