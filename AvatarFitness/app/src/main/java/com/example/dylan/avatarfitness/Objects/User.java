package com.example.dylan.avatarfitness.Objects;

import java.util.ArrayList;

/**
 * Created by Dylan on 2/16/2015.
 */
public class User {
    private long mUserID;
    private String mUsername;
    private String mPassword;
    private Avatar mAvatar;

    private ArrayList<iWorkout> mWorkouts;

    public User(){
        mWorkouts = new ArrayList<>();
    }
    public User( long userID, String username, String password ){
        mUserID = userID;
        mUsername = username;
        mPassword = password;
        mWorkouts = new ArrayList<>();
    }
    public ArrayList<iWorkout> getWorkouts() {
        return mWorkouts;
    }
    public void AddWorkout( iWorkout workout ){
        mWorkouts.add(workout);
    }
    public long getUserID(){
        return mUserID;
    }
    public void setUserID( long userID ){
        mUserID = userID;
    }
    public String getUsername(){
        return mUsername;
    }
    public void setUsername(String username){
        mUsername = username;
    }
    public String getPassword(){
        return mPassword;
    }
    public void setPassword(String password){
        mPassword = password;
    }
}
