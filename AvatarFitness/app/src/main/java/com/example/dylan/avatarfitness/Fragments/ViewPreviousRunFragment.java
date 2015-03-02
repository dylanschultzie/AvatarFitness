package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.dylan.avatarfitness.Objects.Run;
import com.example.dylan.avatarfitness.Objects.iWorkout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.dylan.avatarfitness.R;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;


public class ViewPreviousRunFragment extends Fragment implements LocationListener {
    private OnFragmentInteractionListener mListener;
    private Run mRun = null;

    private Context mContext;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mBestProvider;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_view_previous_run, container, false);
        TextView mDistanceTravelledTextView = (TextView) thisView.findViewById(R.id.PreviousDistanceTextView);

        mRun = (Run)mListener.GetSingularRun();

        //Setting up Google Map
        mContext = mListener.GetContext();
        SupportMapFragment mSupportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.PreviousMap));
        mMap = mSupportMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mCriteria = new Criteria();
        mBestProvider = mLocationManager.getBestProvider(mCriteria, true);
        mLocation = mLocationManager.getLastKnownLocation(mBestProvider);
        mDistanceTravelledTextView.setText("Distance:" + mRun.getDistance() + " miles");
        PlotPolylines();

        return thisView;
    }

    public void SetLocation(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void PlotPolylines(){
        int mListLength = 0;
        SetLocation(mRun.getRoute().get(0));
        mMap.addMarker(new MarkerOptions().position(mRun.getRoute().get(mListLength++)).title("Start"));
        while( mRun.getRoute().size() > mListLength){
            mMap.addPolyline(new PolylineOptions()
                    .add(mRun.getRoute().get(mListLength - 1), mRun.getRoute().get(mListLength))
                    .width(10)
                    .color(Color.BLUE));
            mListLength++;
        }
        mMap.addMarker(new MarkerOptions().position(mRun.getRoute().get(mListLength - 1)).title("End"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
        public Context GetContext();
        public ArrayList<Run> GetRuns();
        public iWorkout GetSingularRun();
    }
}
