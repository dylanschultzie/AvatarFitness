package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.R;

import java.text.DateFormat;
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
        // Required empty public constructor
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

        final EditText workoutName = (EditText) thisView.findViewById(R.id.WorkoutName);
        final EditText workoutDescription = (EditText) thisView.findViewById(R.id.WorkoutDescription);
        final Button startTimer = (Button) thisView.findViewById(R.id.StartWorkoutButton);
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( !mRunningChrono ) {
                    mRunningChrono = true;
                    mElapsedChrono.setBase(SystemClock.elapsedRealtime());
                    mElapsedChrono.start();
                    startTimer.setText("Stop Workout");
                }
                else{
                    mRunningChrono = false;
                    mElapsedChrono.stop();
                    mElapsedMillis = SystemClock.elapsedRealtime() - mElapsedChrono.getBase();
                    mListener.SaveWorkout( new Workout( workoutName.getText().toString(),
                            workoutDescription.getText().toString(), mElapsedMillis,
                            new Date()));
                    startTimer.setText("Start Workout");
                }
            }
        });
        mElapsedChrono = (Chronometer) thisView.findViewById(R.id.WorkoutLengthChrono);
        return thisView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        public void SaveWorkout( Workout workout );
    }

}
