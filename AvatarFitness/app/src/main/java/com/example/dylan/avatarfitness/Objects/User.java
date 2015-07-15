package com.example.dylan.avatarfitness.Objects;

import java.util.ArrayList;

/**@author Dylan Schultz
 * Date Created: 2/16/2015
 * Date Last Revised: 7/14/2015
 *
 * Purpose:
 *      Maintains User information. mAvatar is a placeholder for future implementation of Avatar
 *      class.
 *
 */
public class User {
    private long mUserID;
    private long mGender;
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
    public long getGender() {return mGender;}
    public void setGender(long mGender) {this.mGender = mGender;}
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
