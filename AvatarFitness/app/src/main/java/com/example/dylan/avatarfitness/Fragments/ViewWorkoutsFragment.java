package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.example.dylan.avatarfitness.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewWorkoutsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> mRouteListAdapter;

    // TODO: Rename and change types and number of parameters
    public static ViewWorkoutsFragment newInstance(String param1, String param2) {
        ViewWorkoutsFragment fragment = new ViewWorkoutsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewWorkoutsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_workouts, container, false);

        try {
            mRouteListAdapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_list_item_1, mListener.GetExerciseDate());
            ListView listView = (ListView) view.findViewById(R.id.WorkoutHistoryListView);
            listView.setAdapter(mRouteListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    iWorkout workoutBase = null;
                    Run run = null;
                    Workout work = null;
                    TextView textView = (TextView) view;
                    String selectedItem = textView.getText().toString();

                    String[] str_array = selectedItem.split(", ");
                    workoutBase = mListener.GetWorkoutByExerciseDate(str_array[0], str_array[1]);
                    if (workoutBase.getDescription().equals("Run")) {
                        run = (Run) workoutBase;
                        mListener.SwapFragmentViewPreviousRun(run);
                    } else {
                        work = (Workout) workoutBase;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(selectedItem)
                                .setMessage("Weight: " + work.getWeight() + "lbs\n" +
                                        "Sets: " + work.getSets() + "\n" +
                                        "Reps: " + work.getReps() + "\n" +
                                        "Time to complete workout: " + work.getDuration() + " seconds")
                                .show();
                        AlertDialog dialog = builder.create();
                    }
                    //use dialog here!
                }
            });
        }
        catch(Exception e){}
        return view;
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
        public ArrayList<String> GetExerciseDate();
        public iWorkout GetWorkoutByExerciseDate( String exercise, String date);
        public void SwapFragmentViewPreviousRun(Run run);
    }

}
