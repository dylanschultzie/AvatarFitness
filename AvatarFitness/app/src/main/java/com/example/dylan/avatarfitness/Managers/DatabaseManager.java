package com.example.dylan.avatarfitness.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.User;
import com.example.dylan.avatarfitness.Objects.Workout;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Dylan Schultz
 * Date Created: 2/22/2015
 * Date Last Edited: 7/14/2015
 * Purpose:
 *      This acts as the third tier (persistence) layer, directly interacting with the database.
 *      Only MainActivity will actually access this class, and if any more functionality need be
 *      added, it must also be added through MainActivity.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Fitness_db_Android.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager( Context context ){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();

        //uncomment these are rebuild project to destroy database. manually deleting would probably
        //be faster, honestly...
//        File dbFile = context.getDatabasePath(DATABASE_NAME);
//        dbFile.delete();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");	//allows for foreign keys in tables
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_CREATE_USERS_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_CREATE_WORKOUTS_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_CREATE_RUNS_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_CREATE_ROUTES_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_DELETE_ROUTES_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_DELETE_RUNS_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_DELETE_EXERCISES_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_DELETE_WORKOUTS_TABLE);
        db.execSQL(com.example.dylan.avatarfitness.Managers.DatabaseContract.SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }

    public ArrayList<String> GetExerciseTypeList(){
        SQLiteDatabase readDB = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        ArrayList<String> list = new ArrayList<>();

        Cursor cur = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME,
                new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION},
                null,null,null,null,null);

        if(cur.moveToFirst()){
            do{
                if(cur.getString(0).equals("Run")){  }
                else{
                    list.add(cur.getString(0));
                }
            }while(cur.moveToNext());
        }
        return list;
    }

    public User GetUser(String username, String password){
        SQLiteDatabase readDB = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        Cursor cur;


        cur = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME,
                new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_ID,
                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_GENDER},
                com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_NAME + " = '" + username + "' AND " +
                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_PASSWORD + " = '" + password + "'", null,
                null, null, null);

        User myUser = new User(0, username, password);
        if (cur.moveToFirst()){
            myUser.setUserID(cur.getLong(0));
            myUser.setGender(cur.getLong(1));
        }
        return myUser;
    }

    /**Takes in a userID and translates it into what user data is stored within the database.
     *
     * @param userID
     * @return User
     */
    public User GetUserByID(long userID){
        SQLiteDatabase readDB = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        Cursor cur = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME,
                new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_NAME,
                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_PASSWORD,
                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_GENDER},
                com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_ID + " = '" + userID + "'", null,
                null, null, null);

        User user = new User(userID,"","");
        if (cur.moveToFirst()){
            user.setUsername(cur.getString(0));
            user.setPassword(cur.getString(1));
            user.setGender(cur.getLong(2));

            cur = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME,
                    new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_WORKOUT_ID,
                            com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_DURATION,
                            com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION,
                            com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_DATE},
                    com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = '" + user.getUserID() + "'",null,null,null,null);

            if(cur.moveToFirst()){
                do {
                    long wID = cur.getLong(0);
                    float duration = cur.getFloat(1);
                    String description = cur.getString(2);
                    Date date = null;
                    date = new Date(cur.getString(3));

                    if(description.equals("Run")){
                        Cursor c = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Runs.TABLE_NAME,
                                new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Runs.COLUMN_NAME_RUN_ID,
                                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Runs.COLUMN_NAME_DISTANCE},
                                com.example.dylan.avatarfitness.Managers.DatabaseContract.Runs.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
                                null, null, null, null);

                        if(c.moveToFirst()){

                            int rID = c.getInt(0);
                            float distance = c.getFloat(1);
                            do{
                                Cursor cc = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.TABLE_NAME,
                                        new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.COLUMN_NAME_LATITUDE,
                                                com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.COLUMN_NAME_LONGITUDE},
                                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.COLUMN_NAME_RUN_ID_FK + " = '" + rID + "'", null, null, null,
                                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.COLUMN_NAME_LAT_LONG_ID + " ASC");

                                if(cc.moveToFirst()){
                                    ArrayList<LatLng> list = new ArrayList<>();
                                    do {
                                        list.add(new LatLng(cc.getFloat(0), cc.getFloat(1)));
                                    }while(cc.moveToNext());

                                    user.AddWorkout( new Run(duration, date, list, distance ));
                                }
                            }while(c.moveToNext());
                        }
                    }
                    else{
                        Cursor c = readDB.query(com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.TABLE_NAME,
                                new String[] {com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_WEIGHT,
                                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_SETS,
                                        com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_REPS},
                                com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
                                null, null, null, null);

                        if( c.moveToFirst()){
                            int weight = c.getInt(0);
                            int sets = c.getInt(1);
                            int reps = c.getInt(2);
                            user.AddWorkout( new Workout(sets, reps, description, duration, date, weight));
                        }
                    }
                }while(cur.moveToNext());
            }
        }
        return user;
    }

    /** This will take in what user, exerciseType, and whether the max or min is needed for a stat.
     * For example: GetStatByStringAndQualifier( user(dylan), "Bench Press", "max") will return my
     *              max recorded bench press.
     *
     * @param user User seeking data to be found from
     * @param exerciseType "Bench Press" (exercise type)
     * @param maxMin "Max" (to return min or max value)
     * @return value of either min or max exercise
     */
    public int GetStatByStringAndQualifier( User user, String exerciseType, String maxMin ){
        int stat = 0;
        SQLiteDatabase readDB = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        final String mQuery = "SELECT " + maxMin + "(" + com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_WEIGHT + ") " +
                "FROM " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.TABLE_NAME +
                " JOIN " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME + " ON " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.TABLE_NAME + "." + com.example.dylan.avatarfitness.Managers.DatabaseContract.Exercises.COLUMN_NAME_WORKOUT_ID_FK +  " = " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME + "." + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_WORKOUT_ID +
                " JOIN " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME + " ON " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME + "." + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME + "." + com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_ID +
                " WHERE " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION + " = '" + exerciseType + "' " +
                "AND " + com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME + "." + com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_ID + " = ?";


        Cursor cur = readDB.rawQuery(mQuery, new String[]{String.valueOf(user.getUserID())});

        if(cur.moveToNext()){
            stat = cur.getInt(0);
        }
        return stat;
    }

    /**This takes in a user and an unfortunately named "run" - in reality run can be hiking,
     * running, cycling, etc. Due to lack of calorie counting, this doesn't really matter.
     *
     * @param user user to log "run" for
     * @param run run class including latlong
     */
    public void InsertRunByUserID( User user, Run run ){
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        int routePoint = 0;

        //create new workout
        values.clear();
        values.put("UserID", user.getUserID());
        values.put("Description", "Run");
        values.put("Date", run.getDate().toString());
        values.put("Duration", run.getDuration());
        long workoutID = writeDB.insert(com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME,null,values);

        values.clear();
        values.put("WorkoutID", workoutID);
        values.put("Distance", run.getDistance());
        long runID = writeDB.insert(com.example.dylan.avatarfitness.Managers.DatabaseContract.Runs.TABLE_NAME,null,values);

        //store each exercise and route
        ArrayList<LatLng> route = run.getRoute();
        for(LatLng point : route)
        {
            values.clear();
            values.put("RunID", runID);
            values.put("Latitude", point.latitude);
            values.put("Longitude", point.longitude);
            values.put("LatLongID", routePoint++);
            writeDB.insert(com.example.dylan.avatarfitness.Managers.DatabaseContract.Routes.TABLE_NAME,null,values);
        }
    }

    /**More aptly named that "run", this takes in an exercise ("Bench press") and stores data for it.
     *
     * @param user
     * @param workout
     */
    public void InsertExerciseByUserID( User user, Workout workout ){
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        //create new workout
        values.clear();
        values.put("UserID", user.getUserID());
        values.put("Description", workout.getDescription());
        values.put("Date", workout.getDate().toString());
        values.put("Duration", workout.getDuration());
        long workoutID = writeDB.insert(com.example.dylan.avatarfitness.Managers.DatabaseContract.Workouts.TABLE_NAME,null,values);

        values.clear();
        values.put("WorkoutID", workoutID);
        values.put("Sets", workout.getSets());
        values.put("Reps", workout.getReps());
        values.put("Weight", workout.getWeight());
    }

    /**Inserts a new user into database.
     *
     * @param queryValues user to be added (previously created)
     * @return the newly created user
     */
    public User InsertUser (User queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_USER_NAME, queryValues.getUsername());
        values.put(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_PASSWORD, queryValues.getPassword());
        values.put(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.COLUMN_NAME_GENDER, queryValues.getGender());
        queryValues.setUserID(database.insert(com.example.dylan.avatarfitness.Managers.DatabaseContract.Users.TABLE_NAME, null, values));
        database.close();
        return queryValues;
    }
}
