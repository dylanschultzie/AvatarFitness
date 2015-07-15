package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.content.Context;
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


public class MapFragment extends Fragment implements LocationListener {
    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mBestProvider;
    private Location mLocation;
    private TextView mLocationTextView;
    private TextView mDistanceTravelledTextView;
    private Button mStartButton;
    private Button mStopButton;
    private ArrayList<LatLng> mList = new ArrayList<>();
    private int mListLength;
    private float mOngoingDistanceTravelled;
    private Chronometer mChrono;
    private long mElapsedMillis;
    private boolean mRunActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_map, container, false);

        mLocationTextView = (TextView) thisView.findViewById(R.id.latlongLocation);
        mDistanceTravelledTextView = (TextView) thisView.findViewById(R.id.distanceTextView);
        mStartButton = (Button) thisView.findViewById(R.id.StartMapsButton);
        mStopButton = (Button) thisView.findViewById(R.id.StopMapsButton);
        mListLength = 0;
        mOngoingDistanceTravelled = 0;
        mElapsedMillis = 0;
        mRunActive = false;


        try {
            //Setting up Google Map
            mContext = mListener.GetContext();
            SupportMapFragment mSupportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map));
            mMap = mSupportMapFragment.getMap();
            mMap.setMyLocationEnabled(true);
            mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
            mCriteria = new Criteria();
            mBestProvider = mLocationManager.getBestProvider(mCriteria, true);
            mLocation = mLocationManager.getLastKnownLocation(mBestProvider);
            SetLocation();
        }
        catch( Exception e){}
        mStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mLocation != null && !mRunActive) {
                    StartRun();
                    LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Start"));
                    mList.add(latLng);
                    onLocationChanged(mLocation);
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation != null && mRunActive) {
                    LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    mList.add(latLng);
                    mMap.addPolyline(new PolylineOptions()
                            .add(mList.get(mListLength - 1), mList.get(mListLength))
                            .width(10)
                            .color(Color.BLUE));
                    mElapsedMillis = SystemClock.elapsedRealtime() - mChrono.getBase();
                    mListLength++;
                    mMap.addMarker(new MarkerOptions().position(latLng).title("End"));
                    EndRun();
                    mListener.SaveWorkout(new Run(mElapsedMillis/1000, new Date(), mList,
                            mOngoingDistanceTravelled), 0);
                }
            }
        });

        mChrono = (Chronometer) thisView.findViewById(R.id.RunLengthChrono);
        return thisView;
    }
    public void StartRun(){
        mRunActive = true;
        mLocationManager.requestLocationUpdates(mBestProvider, 1000, 0, this);
        mChrono.setBase(SystemClock.elapsedRealtime());
        mChrono.start();
    }
    public void EndRun(){
        mRunActive = false;
        mChrono.stop();
        mLocationManager.removeUpdates(this);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addCircle(new CircleOptions().center(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mList.add(latLng);
        mListLength++;
        mMap.addPolyline(new PolylineOptions()
                .add(mList.get(mListLength-1),mList.get(mListLength))
                .width(10)
                .color(Color.BLUE));
        float [] distance = new float[1];
        location.distanceBetween(mList.get(mListLength-1).latitude,
                mList.get(mListLength-1).longitude,
                mList.get(mListLength).latitude,
                mList.get(mListLength).longitude, distance);
        mOngoingDistanceTravelled += distance[0] * 0.000621371;
        mLocationTextView.setText("Latitude:" + latLng.latitude + ", Longitude:" + latLng.longitude);
        mDistanceTravelledTextView.setText("Distance:" + String.format("%.2f",mOngoingDistanceTravelled) + " miles");
    }

    public void SetLocation(){
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
        public void SaveWorkout( iWorkout workout, int workoutType );
    }
}
