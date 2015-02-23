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

    public Run(){
        mDuration = 0;
    }

    public Run(float duration, Date date, ArrayList<LatLng> list ){
        mDuration = duration;
        mDate = date;
        mRoute = list;
    }

    @Override
    public void LogWorkout() {

    }
}
