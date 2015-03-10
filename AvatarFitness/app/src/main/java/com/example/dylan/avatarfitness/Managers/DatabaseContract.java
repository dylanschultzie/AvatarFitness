package com.example.dylan.avatarfitness.Managers;

import android.provider.BaseColumns;

import com.example.dylan.avatarfitness.Objects.Workout;

/**
 * Created by Dylan on 2/22/2015.
 */
public final class DatabaseContract implements BaseColumns {
    public DatabaseContract(){

    }

    private static final String	TEXT_TYPE = " TEXT";
    private static final String	INTEGER_TYPE = " INTEGER";
    private static final String	REAL_TYPE = " REAL";
    private static final String	NUMERIC_TYPE = " NUMERIC";
    private static final String	COMMA_SEP = ", ";

    private static final String	PRIMARY_KEY_CON = " PRIMARY KEY AUTOINCREMENT";
    private static final String FOREIGN_KEY_CON = " FOREIGN KEY";
    private static final String REFERENCE_CON = " REFERENCES ";
    private static final String	NOT_NULL_CON = " NOT NULL";

    public static abstract class Users implements BaseColumns{
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_USER_ID = "UserID";
        public static final String COLUMN_NAME_USER_NAME = "UserName";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_GENDER = "Gender";
    }

    public static abstract class Workouts implements BaseColumns{
        public static final String TABLE_NAME = "Workouts";
        public static final String COLUMN_NAME_WORKOUT_ID = "WorkoutID";
        public static final String COLUMN_NAME_USER_ID_FK = "UserID";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_DURATION = "Duration";
    }

    public static abstract class Runs implements BaseColumns{
        public static final String TABLE_NAME = "Runs";
        public static final String COLUMN_NAME_RUN_ID = "RunID";
        public static final String COLUMN_NAME_WORKOUT_ID_FK = "WorkoutID";
        public static final String COLUMN_NAME_DISTANCE = "Distance";
    }

    public static abstract class Routes implements BaseColumns{
        public static final String TABLE_NAME = "Routes";
        public static final String COLUMN_NAME_ROUTE_ID = "RouteID";
        public static final String COLUMN_NAME_RUN_ID_FK = "RunID";
        public static final String COLUMN_NAME_LATITUDE = "Latitude";
        public static final String COLUMN_NAME_LONGITUDE = "Longitude";
        public static final String COLUMN_NAME_LAT_LONG_ID = "LatLongID";
    }

    public static abstract class Exercises implements BaseColumns{
        public static final String TABLE_NAME = "Exercises";
        public static final String COLUMN_NAME_EXERCISE_ID = "ExerciseID";
        public static final String COLUMN_NAME_WORKOUT_ID_FK = "WorkoutID";
        public static final String COLUMN_NAME_SETS = "Sets";
        public static final String COLUMN_NAME_REPS = "Reps";
        public static final String COLUMN_NAME_WEIGHT = "Weight";
    }

    public static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users.COLUMN_NAME_USER_ID + INTEGER_TYPE + PRIMARY_KEY_CON + COMMA_SEP +
                    Users.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
                    Users.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    Users.COLUMN_NAME_GENDER + INTEGER_TYPE + ")";

    public static final String SQL_CREATE_WORKOUTS_TABLE =
            "CREATE TABLE " + Workouts.TABLE_NAME + " (" +
                    Workouts.COLUMN_NAME_WORKOUT_ID + INTEGER_TYPE + PRIMARY_KEY_CON + COMMA_SEP +
                    Workouts.COLUMN_NAME_USER_ID_FK + INTEGER_TYPE + COMMA_SEP +
                    Workouts.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    Workouts.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Workouts.COLUMN_NAME_DURATION + REAL_TYPE + COMMA_SEP +
                    FOREIGN_KEY_CON + "(" + Workouts.COLUMN_NAME_USER_ID_FK + ")" + REFERENCE_CON + Users.TABLE_NAME + "(" + Users.COLUMN_NAME_USER_ID + ") )";

    public static final String SQL_CREATE_RUNS_TABLE =
            "CREATE TABLE " + Runs.TABLE_NAME + " (" +
                    Runs.COLUMN_NAME_RUN_ID + INTEGER_TYPE + PRIMARY_KEY_CON + COMMA_SEP +
                    Runs.COLUMN_NAME_WORKOUT_ID_FK + INTEGER_TYPE + COMMA_SEP +
                    Runs.COLUMN_NAME_DISTANCE + REAL_TYPE + COMMA_SEP +
                    FOREIGN_KEY_CON + "(" + Runs.COLUMN_NAME_WORKOUT_ID_FK + ")" + REFERENCE_CON + Workouts.TABLE_NAME + "(" + Workouts.COLUMN_NAME_WORKOUT_ID + ") )";

    public static final String SQL_CREATE_ROUTES_TABLE =
            "CREATE TABLE " + Routes.TABLE_NAME + " (" +
                    Routes.COLUMN_NAME_ROUTE_ID + INTEGER_TYPE + PRIMARY_KEY_CON + COMMA_SEP +
                    Routes.COLUMN_NAME_RUN_ID_FK + INTEGER_TYPE + COMMA_SEP +
                    Routes.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    Routes.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    Routes.COLUMN_NAME_LAT_LONG_ID + INTEGER_TYPE + COMMA_SEP +
                    FOREIGN_KEY_CON + "(" + Routes.COLUMN_NAME_RUN_ID_FK + ")" + REFERENCE_CON + Runs.TABLE_NAME + "(" + Runs.COLUMN_NAME_RUN_ID + ") )";

    public static final String SQL_CREATE_EXERCISES_TABLE =
            "CREATE TABLE " + Exercises.TABLE_NAME + " (" +
                    Exercises.COLUMN_NAME_EXERCISE_ID + INTEGER_TYPE + PRIMARY_KEY_CON + COMMA_SEP +
                    Exercises.COLUMN_NAME_WORKOUT_ID_FK + INTEGER_TYPE + COMMA_SEP +
                    Exercises.COLUMN_NAME_REPS + REAL_TYPE + COMMA_SEP +
                    Exercises.COLUMN_NAME_SETS + REAL_TYPE + COMMA_SEP +
                    Exercises.COLUMN_NAME_WEIGHT + REAL_TYPE + COMMA_SEP +
                    FOREIGN_KEY_CON + "(" + Exercises.COLUMN_NAME_WORKOUT_ID_FK + ")" + REFERENCE_CON + Workouts.TABLE_NAME + "(" + Workouts.COLUMN_NAME_WORKOUT_ID + ") )";

    public static final String	SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + Users.TABLE_NAME + ";";

    public static final String	SQL_DELETE_WORKOUTS_TABLE =
            "DROP TABLE IF EXISTS " + Workouts.TABLE_NAME + ";";

    public static final String	SQL_DELETE_ROUTES_TABLE =
            "DROP TABLE IF EXISTS " + Routes.TABLE_NAME + ";";

    public static final String  SQL_DELETE_EXERCISES_TABLE =
            "DROP TABLE IF EXISTS " + Exercises.TABLE_NAME + ";";

    public static final String SQL_DELETE_RUNS_TABLE =
            "DROP TABLE IF EXISTS " + Runs.TABLE_NAME + ";";
}
