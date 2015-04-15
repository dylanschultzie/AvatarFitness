package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dylan.avatarfitness.Objects.Workout;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.example.dylan.avatarfitness.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> mRouteListAdapter;

    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        try {
            final Spinner exerciseTypeSpinner = (Spinner) view.findViewById(R.id.ExerciseTypeSpinner);
            final Spinner statTypeSpinner = (Spinner) view.findViewById(R.id.StatTypeSpinner);
            final TextView statTypeTextView = (TextView) view.findViewById(R.id.StatTypeTextView);
            Button refreshButton = (Button) view.findViewById(R.id.RefreshButtonStatistics);
            final GraphView graph = (GraphView) view.findViewById(R.id.graph);

            ArrayList<String> list = CleanExerciseList(mListener.getExerciseTypeList());

            //spinner for exercise type
            ArrayAdapter<String> exerciseTypeAdapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_spinner_item, list);
            exerciseTypeSpinner.setAdapter(exerciseTypeAdapter);

            //spinner for stat type
            ArrayAdapter<String> statTypeAdapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statType));
            statTypeSpinner.setAdapter(statTypeAdapter);

            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<iWorkout> workoutList = mListener.GetExerciseDate(exerciseTypeSpinner.getSelectedItem().toString());
                    String statType = statTypeSpinner.getSelectedItem().toString();
                    int stat = mListener.GetStatByStringAndQualifier(exerciseTypeSpinner.getSelectedItem().toString(),
                            statTypeSpinner.getSelectedItem().toString());

                    statTypeTextView.setText(statType + ": " + stat);

                    DataPoint[] dataPoints = new DataPoint[workoutList.size()];
                    Workout temp = null;
                    int i = 0;
                    for (iWorkout workout : workoutList) {
                        temp = (Workout) workout;
                        dataPoints[i++] = new DataPoint(temp.getDate(), temp.getWeight());
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    graph.addSeries(series);

                    // set date label formatter
                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                    graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space

                    // set manual x bounds to have nice steps
                    graph.getViewport().setMinX(dataPoints[0].getX());
                    graph.getViewport().setMaxX(dataPoints[workoutList.size() - 1].getX());
                    graph.getViewport().setXAxisBoundsManual(true);
                }
            });
        }
        catch(Exception e){}

        return view;
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
        public ArrayList<iWorkout> GetExerciseDate( String exerciseType );
        public ArrayList<String> getExerciseTypeList();
        public int GetStatByStringAndQualifier( String exerciseType, String maxMin );
    }

}
