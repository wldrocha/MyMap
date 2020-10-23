package com.example.mymap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //max and min zoom
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(16);

        // Add a marker in Sydney and move the camera
        LatLng tequesCity = new LatLng(10.345643,-67.075105);
        // add .draggable(true) move marker
        mMap.addMarker(new MarkerOptions().position(tequesCity).title("Marker in my home"));

        //move cam
        //Zoom  map
        // rotate east
        // rotate 3D
        CameraPosition camera = new CameraPosition.Builder()
                .target(tequesCity)
                .zoom(18)           // maximo de zoom 21
                .bearing(90)        // 360
                .tilt(30)           // 90
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(tequesCity));
    }
}