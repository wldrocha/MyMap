package com.example.mymap.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymap.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private View rootView;
    private GoogleMap gmap;
    private MapView mapView;
    private List<Address> addresses;
    private Geocoder geocoder;
    private MarkerOptions marker;
    private FloatingActionButton fab;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = rootView.findViewById(R.id.fragmentMap);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void checkGPSIsEnabled(){
        Toast.makeText(getContext(), "Check GPS", Toast.LENGTH_SHORT).show();
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if(gpsSignal == 0){
                //off gps
                showInfoAlert();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showInfoAlert(){
        new AlertDialog.Builder(getContext())
                .setTitle("GPS signal")
                .setMessage("You don't have a GPS signal enabled . Would you like enable the GPS signal now?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL",null)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        LatLng place = new LatLng(10.345643,-67.075105);

        marker = new MarkerOptions();
        marker.position(place);
        marker.title("Marker in my home");
        marker.draggable(true);
        marker.snippet("Caja para modificar datos");
        marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on));


        gmap.addMarker(marker);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
        gmap.animateCamera(zoom);


        gmap.setOnMarkerDragListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();

        marker.setSnippet(state+' '+city);

        marker.showInfoWindow();

        /*Toast.makeText(getContext(),"address "+ address +"\n" +
                        "city "+ city +"\n" +
                        "state "+ state +"\n" +
                        "country "+ country +"\n" +
                        "country "+ country +"\n" +
                        "postalCode "+ postalCode +"\n"
                , Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onClick(View v) {
        this.checkGPSIsEnabled();
    }
}