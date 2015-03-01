package com.example.dylan.avatarfitness.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dylan on 2/22/2015.
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

    public Date getDate(){
        return mDate;
    }
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
    public void LogWorkout() {

    }
}
