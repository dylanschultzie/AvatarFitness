package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.User;
import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.example.dylan.avatarfitness.R;

import java.io.File;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private User mUser;

    private TextView mLatestExerciseTypeTextView;
    private TextView mLatestDurationTextView;
    private TextView mFeatTextView;
    private TextView mSpeedDirectionTextView;
    private TextView mGreetingsTextView;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        Button mViewWorkouts = (Button) view.findViewById(R.id.WorkoutButton);

        mLatestExerciseTypeTextView = (TextView) view.findViewById(R.id.LatestExerciseTypeTextView);
        mLatestDurationTextView = (TextView) view.findViewById(R.id.LatestDurationTextView);
        mFeatTextView = (TextView) view.findViewById(R.id.FeatTextView);
        mSpeedDirectionTextView = (TextView) view.findViewById(R.id.SpeedDirectionTextView);
        mGreetingsTextView = (TextView) view.findViewById(R.id.GreetingTextView);

        mUser = mListener.GetUser();

        mGreetingsTextView.setText("Hello, " + mUser.getUsername());

        if( mUser.getWorkouts().size() > 0)
            PostLatestWorkout();

        ImageView imageView = (ImageView) view.findViewById(R.id.AvatarImageHomePage);
        switch((int)mUser.getGender()){
            case 0:
                imageView.setImageResource(R.drawable.stan_marsh);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wendy_happy);
                break;
        }
        mViewWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SwapFragmentToViewWorkouts();
            }
        });

        return view;
    }

    public void PostLatestWorkout(){
        iWorkout workout = mUser.getWorkouts().get(mUser.getWorkouts().size()-1);
        if( workout.getDescription().equals("Run")){
            Run run = (Run)workout;
            mLatestExerciseTypeTextView.setText(run.getDescription());
            mLatestDurationTextView.setText("Duration: " + String.valueOf(run.getDuration()) + " seconds");
            mFeatTextView.setText("Distance: " + String.valueOf(run.getDistance()));
            mSpeedDirectionTextView.setText(String.valueOf(run.getDistance() / run.getDuration()));
        }
        else{
            Workout wOut = (Workout) workout;
            mLatestExerciseTypeTextView.setText(wOut.getDescription());
            mLatestDurationTextView.setText("Duration: " + String.valueOf(wOut.getDuration()) + " seconds");
            mFeatTextView.setText("Weight: " + String.valueOf(wOut.getWeight()) + " lbs");
            mSpeedDirectionTextView.setText("Reps: " + wOut.getReps() + ", Sets:" + wOut.getSets());
        }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void SwapFragmentToViewWorkouts();
        public User GetUser();
    }

}
