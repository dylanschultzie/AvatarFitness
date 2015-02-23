package com.example.dylan.avatarfitness.Fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.dylan.avatarfitness.R;


public class MapFragment extends Fragment implements LocationListener {
    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mBestProvider;
    private Location mLocation;
    private SupportMapFragment mSupportMapFragment;
    private TextView locationTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_map, container, false);
        locationTv = (TextView) thisView.findViewById(R.id.latlongLocation);

        mContext = mListener.GetContext();

        SupportMapFragment mSupportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));

        mMap = mSupportMapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        mCriteria = new Criteria();
        mBestProvider = mLocationManager.getBestProvider(mCriteria, true);
        mLocation = mLocationManager.getLastKnownLocation(mBestProvider);

        if (mLocation != null) {
            onLocationChanged(mLocation);
        }
        mLocationManager.requestLocationUpdates(mBestProvider, 2000, 0, this);

        // Inflate the layout for this fragment
        return thisView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
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
        public Context GetContext();
    }
}
