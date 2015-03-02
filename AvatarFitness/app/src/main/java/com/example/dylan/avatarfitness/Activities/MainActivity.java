package com.example.dylan.avatarfitness.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.example.dylan.avatarfitness.Fragments.AccountCreationFragment;
import com.example.dylan.avatarfitness.Fragments.HomeFragment;
import com.example.dylan.avatarfitness.Fragments.MapFragment;
import com.example.dylan.avatarfitness.Fragments.NavigationDrawerFragment;
import com.example.dylan.avatarfitness.Fragments.StatisticsFragment;
import com.example.dylan.avatarfitness.Fragments.ViewWorkoutsFragment;
import com.example.dylan.avatarfitness.Fragments.WorkoutFragment;
import com.example.dylan.avatarfitness.Managers.DatabaseManager;
import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.User;
import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.example.dylan.avatarfitness.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomeFragment.OnFragmentInteractionListener,
        WorkoutFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        StatisticsFragment.OnFragmentInteractionListener,
        AccountCreationFragment.OnFragmentInteractionListener,
        ViewWorkoutsFragment.OnFragmentInteractionListener{

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private User mUser;
    private DatabaseManager db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        long userID = 0;
        if (extras != null) {
            userID = extras.getLong("userID");
        }
        db = new DatabaseManager(this);
        mUser = db.GetUserByID(userID);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position){
            default:
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new WorkoutFragment();
                break;
            case 2:
                fragment = new StatisticsFragment();
                break;
            case 3:
                fragment = new MapFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.Home);
                break;
            case 2:
                mTitle = getString(R.string.Workout);
                break;
            case 3:
                mTitle = getString(R.string.Statistics);
                break;
            case 4:
                mTitle = getString(R.string.Map);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public Context GetContext(){
        return this.getBaseContext();
    }

    public void SaveWorkout( iWorkout workout, int workoutType ){

        mUser.AddWorkout( workout );
        switch(workoutType){
            case 0:
                db.InsertRunByUserID(mUser, (Run)workout);
                break;

            case 1:
                db.InsertExerciseByUserID(mUser, (Workout)workout);
                break;
        }
    }

    public ArrayList<String> getExerciseTypeList(){
        return db.GetExerciseTypeList();
    }

    public void SwapFragmentToViewWorkouts(){
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new ViewWorkoutsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public ArrayList<String> GetExerciseDate(){
        ArrayList<String> list = new ArrayList<>();
        for( iWorkout workout: mUser.getWorkouts()){
            list.add(workout.getDescription() + ", " + workout.getDate().toString());
        }
        return list;
    }

    public iWorkout GetWorkoutByExerciseDate( String exercise, String date){
        iWorkout returnedWorkout = null;
        for( iWorkout workout : mUser.getWorkouts()){
            if( workout.getDate().toString().equals(date) && workout.getDescription().equals(exercise) ){
                returnedWorkout = workout;
            }
        }
        return returnedWorkout;
    }



    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
}
