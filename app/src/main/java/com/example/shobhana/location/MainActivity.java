package com.example.shobhana.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleApiClient gClient = null;
    Location LastLocation,CurrentLocation;
    LocationRequest mLocationRequest;
    TextView longitude, latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude=(TextView)findViewById(R.id.latitude);
        longitude=(TextView)findViewById(R.id.longitude);

        if (gClient == null) {
            gClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                gClient);
        if (LastLocation != null) {
            latitude.setText(String.valueOf(LastLocation.getLatitude()));
            longitude.setText(String.valueOf(LastLocation.getLongitude()));
        }


        Location location = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(gClient, mLocationRequest, this);

        }
        else {
            handleNewLocation(location);
        };


    }

    private void handleNewLocation(Location location) {

        Log.d("location", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        System.out.println("new latitude= "+currentLatitude+"  new longitude= "+currentLongitude);
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }





    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        gClient.connect();
        super.onStart();
    }

    protected void onStop() {
        gClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (gClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(gClient, this);
            gClient.disconnect();
        }
    }

}