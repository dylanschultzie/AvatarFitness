package com.example.dylan.avatarfitness.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.User;
import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dylan on 2/22/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Fitness_db_Android.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager( Context context ){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();

//        File dbFile = context.getDatabasePath(DATABASE_NAME);
//        dbFile.delete();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");	//allows for foreign keys in tables
        db.execSQL(DatabaseContract.SQL_CREATE_USERS_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_WORKOUTS_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_RUNS_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_ROUTES_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SQL_DELETE_ROUTES_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_RUNS_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_EXERCISES_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_WORKOUTS_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }

    public ArrayList<String> GetExerciseTypeList(){
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        ArrayList<String> list = new ArrayList<>();

        Cursor cur = readDB.query(DatabaseContract.Workouts.TABLE_NAME,
                new String[] {DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION},
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
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        Cursor cur = null;

//        if(password.equals("")){
//            cur = readDB.query(DatabaseContract.Users.TABLE_NAME,
//                    new String[] {DatabaseContract.Users.COLUMN_NAME_USER_ID,
//                            DatabaseContract.Users.COLUMN_NAME_GENDER},
//                    DatabaseContract.Users.COLUMN_NAME_USER_NAME + " = '" + username + "'", null,
//                    null, null, null);
//        }
//        else{
            cur = readDB.query(DatabaseContract.Users.TABLE_NAME,
                    new String[] {DatabaseContract.Users.COLUMN_NAME_USER_ID,
                            DatabaseContract.Users.COLUMN_NAME_GENDER},
                    DatabaseContract.Users.COLUMN_NAME_USER_NAME + " = '" + username + "' AND " +
                            DatabaseContract.Users.COLUMN_NAME_PASSWORD + " = '" + password + "'", null,
                    null, null, null);
//        }

        User myUser = new User(0, username, password);
        if (cur.moveToFirst()){
                myUser.setUserID(cur.getLong(0));
                myUser.setGender(cur.getLong(1));
        }
        return myUser;
    }

    public User GetUserByID(long userID){
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        Cursor cur = readDB.query(DatabaseContract.Users.TABLE_NAME,
                new String[] {DatabaseContract.Users.COLUMN_NAME_USER_NAME,
                              DatabaseContract.Users.COLUMN_NAME_PASSWORD,
                              DatabaseContract.Users.COLUMN_NAME_GENDER},
                DatabaseContract.Users.COLUMN_NAME_USER_ID + " = '" + userID + "'", null,
                null, null, null);

        User user = new User(userID,"","");
        if (cur.moveToFirst()){
            user.setUsername(cur.getString(0));
            user.setPassword(cur.getString(1));
            user.setGender(cur.getLong(2));

            cur = readDB.query(DatabaseContract.Workouts.TABLE_NAME,
                    new String[] {DatabaseContract.Workouts.COLUMN_NAME_WORKOUT_ID,
                            DatabaseContract.Workouts.COLUMN_NAME_DURATION,
                            DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION,
                            DatabaseContract.Workouts.COLUMN_NAME_DATE},
                    DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = '" + user.getUserID() + "'",null,null,null,null);

            if(cur.moveToFirst()){
                do {
                    long wID = cur.getLong(0);
                    float duration = cur.getFloat(1);
                    String description = cur.getString(2);
                    Date date = null;
                    date = new Date(cur.getString(3));

                    if(description.equals("Run")){
                        Cursor c = readDB.query(DatabaseContract.Runs.TABLE_NAME,
                                new String[] {DatabaseContract.Runs.COLUMN_NAME_RUN_ID,
                                        DatabaseContract.Runs.COLUMN_NAME_DISTANCE},
                                DatabaseContract.Runs.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
                                null, null, null, null);

                        if(c.moveToFirst()){

                            int rID = c.getInt(0);
                            float distance = c.getFloat(1);
                            do{
                                Cursor cc = readDB.query(DatabaseContract.Routes.TABLE_NAME,
                                        new String[] {DatabaseContract.Routes.COLUMN_NAME_LATITUDE,
                                                DatabaseContract.Routes.COLUMN_NAME_LONGITUDE},
                                        DatabaseContract.Routes.COLUMN_NAME_RUN_ID_FK + " = '" + rID + "'", null, null, null,
                                        DatabaseContract.Routes.COLUMN_NAME_LAT_LONG_ID + " ASC");

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
                        Cursor c = readDB.query(DatabaseContract.Exercises.TABLE_NAME,
                                new String[] {DatabaseContract.Exercises.COLUMN_NAME_WEIGHT,
                                        DatabaseContract.Exercises.COLUMN_NAME_SETS,
                                        DatabaseContract.Exercises.COLUMN_NAME_REPS},
                                DatabaseContract.Exercises.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
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

    public int GetStatByStringAndQualifier( User user, String exerciseType, String maxMin ){
        int stat = 0;
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        final String mQuery = "SELECT " + maxMin + "(" + DatabaseContract.Exercises.COLUMN_NAME_WEIGHT + ") " +
                "FROM " + DatabaseContract.Exercises.TABLE_NAME +
                " JOIN " + DatabaseContract.Workouts.TABLE_NAME + " ON " + DatabaseContract.Exercises.TABLE_NAME + "." + DatabaseContract.Exercises.COLUMN_NAME_WORKOUT_ID_FK +  " = " + DatabaseContract.Workouts.TABLE_NAME + "." + DatabaseContract.Workouts.COLUMN_NAME_WORKOUT_ID +
                " JOIN " + DatabaseContract.Users.TABLE_NAME + " ON " + DatabaseContract.Workouts.TABLE_NAME + "." + DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = " + DatabaseContract.Users.TABLE_NAME + "." + DatabaseContract.Users.COLUMN_NAME_USER_ID +
                " WHERE " + DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION + " = '" + exerciseType + "' " +
                "AND " + DatabaseContract.Users.TABLE_NAME + "." + DatabaseContract.Users.COLUMN_NAME_USER_ID + " = ?";


        Cursor cur = readDB.rawQuery(mQuery, new String[]{String.valueOf(user.getUserID())});

        if(cur.moveToNext()){
            stat = cur.getInt(0);
        }
        return stat;
    }

    public void InsertRunByUserID( User user, Run run ){
        SQLiteDatabase readDB = this.getReadableDatabase();
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
        long workoutID = writeDB.insert(DatabaseContract.Workouts.TABLE_NAME,null,values);

        values.clear();
        values.put("WorkoutID", workoutID);
        values.put("Distance", run.getDistance());
        long runID = writeDB.insert(DatabaseContract.Runs.TABLE_NAME,null,values);

        //store each exercise and route
        ArrayList<LatLng> route = run.getRoute();
        for(LatLng point : route)
        {
            values.clear();
            values.put("RunID", runID);
            values.put("Latitude", point.latitude);
            values.put("Longitude", point.longitude);
            values.put("LatLongID", routePoint++);
            writeDB.insert(DatabaseContract.Routes.TABLE_NAME,null,values);
        }
    }

    public void InsertExerciseByUserID( User user, Workout workout ){
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        //create new workout
        values.clear();
        values.put("UserID", user.getUserID());
        values.put("Description", workout.getDescription());
        values.put("Date", workout.getDate().toString());
        values.put("Duration", workout.getDuration());
        long workoutID = writeDB.insert(DatabaseContract.Workouts.TABLE_NAME,null,values);

        values.clear();
        values.put("WorkoutID", workoutID);
        values.put("Sets", workout.getSets());
        values.put("Reps", workout.getReps());
        values.put("Weight", workout.getWeight());
        long exerciseID = writeDB.insert(DatabaseContract.Exercises.TABLE_NAME,null,values);
    }

    public User InsertUser (User queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_NAME_USER_NAME, queryValues.getUsername());
        values.put(DatabaseContract.Users.COLUMN_NAME_PASSWORD, queryValues.getPassword());
        values.put(DatabaseContract.Users.COLUMN_NAME_GENDER, queryValues.getGender());
        queryValues.setUserID(database.insert(DatabaseContract.Users.TABLE_NAME, null, values));
        database.close();
        return queryValues;
    }

    public User PopulateUser(User user){
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        int routePoint = 0;

        Cursor cur = readDB.query(DatabaseContract.Workouts.TABLE_NAME,
                new String[] {DatabaseContract.Workouts.COLUMN_NAME_WORKOUT_ID,
                        DatabaseContract.Workouts.COLUMN_NAME_DURATION,
                        DatabaseContract.Workouts.COLUMN_NAME_DESCRIPTION,
                        DatabaseContract.Workouts.COLUMN_NAME_DATE},
                DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = '" + user.getUserID() + "'",null,null,null,null);

        if(cur.moveToFirst()){
            do {
                long wID = cur.getLong(0);
                float duration = cur.getFloat(1);
                String description = cur.getString(2);
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    date = format.parse(cur.getString(3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(description.equals("Run")){
                    Cursor c = readDB.query(DatabaseContract.Runs.TABLE_NAME,
                            new String[] {DatabaseContract.Runs.COLUMN_NAME_RUN_ID,
                                    DatabaseContract.Runs.COLUMN_NAME_DISTANCE},
                            DatabaseContract.Runs.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
                            null, null, null, null);

                    if(c.moveToFirst()){

                        int rID = c.getInt(0);
                        float distance = c.getFloat(0);
                        do{
                            Cursor cc = readDB.query(DatabaseContract.Routes.TABLE_NAME,
                                    new String[] {DatabaseContract.Routes.COLUMN_NAME_LAT_LONG_ID,
                                            DatabaseContract.Routes.COLUMN_NAME_LATITUDE,
                                            DatabaseContract.Routes.COLUMN_NAME_LONGITUDE},
                                    DatabaseContract.Routes.COLUMN_NAME_RUN_ID_FK + " = '" + rID + "'", null, null, null,
                                    DatabaseContract.Routes.COLUMN_NAME_LAT_LONG_ID + " ASC");

                            if(cc.moveToFirst()){
                                ArrayList<LatLng> list = new ArrayList<>();
                                do {
                                    list.add(new LatLng(cur.getFloat(0), cur.getFloat(1)));
                                }while(cc.moveToNext());

                                user.AddWorkout( new Run(duration, date, list, distance ));
                            }
                        }while(c.moveToNext());
                    }
                }
                else{
                    Cursor c = readDB.query(DatabaseContract.Exercises.TABLE_NAME,
                            new String[] {DatabaseContract.Exercises.COLUMN_NAME_WEIGHT,
                                    DatabaseContract.Exercises.COLUMN_NAME_SETS,
                                    DatabaseContract.Exercises.COLUMN_NAME_REPS},
                            DatabaseContract.Exercises.COLUMN_NAME_WORKOUT_ID_FK + " = '" + wID + "'",
                            null, null, null, null);

                    int weight = c.getInt(0);
                    int sets = c.getInt(1);
                    int reps = c.getInt(2);
                    user.AddWorkout( new Workout(sets, reps, description, duration, date, weight));
                }
            }while(cur.moveToNext());
        }
        return user;
    }

//    public int UpdateUserPassword (User queryValues){
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseContract.Users.COLUMN_NAME_USER_NAME, queryValues.getUsername());
//        values.put(DatabaseContract.Users.COLUMN_NAME_PASSWORD, queryValues.getPassword());
//        queryValues.setUserID(database.insert(DatabaseContract.Users.TABLE_NAME, null, values));
//        database.close();
//        return database.update(database.insert(DatabaseContract.Users.TABLE_NAME, null, values),
//                "UserId = ?", new String[] {String.valueOf(queryValues.getUserID())});
//    }
}
