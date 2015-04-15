package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.example.dylan.avatarfitness.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Chronometer mElapsedChrono;
    DateFormat mDateFormat = DateFormat.getDateTimeInstance();
    private static boolean mRunningChrono;
    private long mElapsedMillis;

    public static WorkoutFragment newInstance() {
        mRunningChrono = false;
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public WorkoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_workout, container, false);
        final AutoCompleteTextView exerciseTypeEditText = (AutoCompleteTextView) thisView.findViewById(R.id.ExerciseTypeAutoText);
        final EditText workoutSets = (EditText) thisView.findViewById(R.id.SetsEditText);
        final EditText workoutReps = (EditText) thisView.findViewById(R.id.RepsEditText);
        final EditText workoutWeight = (EditText) thisView.findViewById(R.id.WeightEditText);
        final Button startTimer = (Button) thisView.findViewById(R.id.StartWorkoutButton);

        try {

            ArrayList<String> list = CleanExerciseList(mListener.getExerciseTypeList());

            //auto complete for exercise type
            ArrayAdapter<String> exerciseTypeAdapter = new ArrayAdapter<String>(thisView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, list);
            exerciseTypeEditText.setAdapter(exerciseTypeAdapter);

            startTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mRunningChrono) {
                        mRunningChrono = true;
                        mElapsedChrono.setBase(SystemClock.elapsedRealtime());
                        mElapsedChrono.start();
                        startTimer.setText("Stop Workout");
                    } else {
                        mRunningChrono = false;
                        mElapsedChrono.stop();
                        mElapsedMillis = SystemClock.elapsedRealtime() - mElapsedChrono.getBase();
                        mElapsedMillis /= 1000;
                        mListener.SaveWorkout(new Workout(Integer.parseInt(workoutSets.getText().toString()),
                                        Integer.parseInt(workoutReps.getText().toString()),
                                        exerciseTypeEditText.getText().toString(),
                                        mElapsedMillis,
                                        new Date(),
                                        Integer.parseInt(workoutWeight.getText().toString())),
                                1);
                        startTimer.setText("Start Workout");
                    }
                }
            });
            mElapsedChrono = (Chronometer) thisView.findViewById(R.id.WorkoutLengthChrono);
        }
        catch(Exception e){}
        return thisView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public ArrayList<String> CleanExerciseList( ArrayList<String> list ){
        ArrayList<String> tempList = new ArrayList<>();
        for(String string : list){
            if(!tempList.contains(string)){
                tempList.add(string);
            }
        }
        return tempList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void SaveWorkout( iWorkout workout, int workoutType );
        public ArrayList<String> getExerciseTypeList();
    }

}
