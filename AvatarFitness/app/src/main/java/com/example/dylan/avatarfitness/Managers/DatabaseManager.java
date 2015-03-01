package com.example.dylan.avatarfitness.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dylan.avatarfitness.Objects.User;

import java.io.File;

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
        db.execSQL(DatabaseContract.SQL_CREATE_ROUTES_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SQL_DELETE_USERS_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_WORKOUTS_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_ROUTES_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_EXERCISES_TABLE);
        onCreate(db);
    }

    public User GetUser(String username, String password){
        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        Cursor cur = readDB.query(DatabaseContract.Users.TABLE_NAME,
                new String[] {DatabaseContract.Users.COLUMN_NAME_USER_ID},
                DatabaseContract.Users.COLUMN_NAME_USER_NAME + " = '" + username + "' AND " +
                DatabaseContract.Users.COLUMN_NAME_PASSWORD + " = '" + password + "'", null,
                null, null, null);

        User myUser = new User(0, username, password);
        if (cur.moveToFirst()){
                myUser.setUserID(cur.getLong(0));
        }
        return myUser;
    }

    public User GetUserByID(long userID){
//        TODO: delete this line!
        userID = 1;

        SQLiteDatabase readDB = this.getReadableDatabase();
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        Cursor cur = readDB.query(DatabaseContract.Users.TABLE_NAME,
                new String[] {DatabaseContract.Users.COLUMN_NAME_USER_NAME, DatabaseContract.Users.COLUMN_NAME_PASSWORD},
                DatabaseContract.Users.COLUMN_NAME_USER_ID + " = '" + userID + "'", null,
                null, null, null);

        User myUser = new User(userID,"","");
        if (cur.moveToFirst()){
            myUser.setUsername(cur.getString(0));
            myUser.setPassword(cur.getString(1));

            cur = readDB.query(DatabaseContract.Workouts.TABLE_NAME,
                    null, DatabaseContract.Workouts.COLUMN_NAME_USER_ID_FK + " = '" + myUser.getUserID() + "'",
                    null, null, null, null);
        }
        return myUser;
    }

    public User InsertUser (User queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_NAME_USER_NAME, queryValues.getUsername());
        values.put(DatabaseContract.Users.COLUMN_NAME_PASSWORD, queryValues.getPassword());
        queryValues.setUserID(database.insert(DatabaseContract.Users.TABLE_NAME, null, values));
        database.close();
        return queryValues;
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
