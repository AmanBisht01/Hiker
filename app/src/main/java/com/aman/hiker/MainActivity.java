package com.aman.hiker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void Update(Location location) {
        Log.i("LOCATONINFO", location.toString());
        TextView lat = findViewById(R.id.LatTextView);
        TextView log = findViewById(R.id.logTextView);
        TextView Acc = findViewById(R.id.AccTextView);
        TextView alt = findViewById(R.id.altTextView);


        lat.setText("Latitute " + location.getLatitude());
        log.setText("Longitute: " + location.getLongitude());
        Acc.setText("Accuracy: " + location.getAccuracy());
        alt.setText("Altitute: " + location.getAltitude());

//        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            String address = "could not found";
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.i("PlaceInfo", addresses.get(0).toString());
//            Log.i("PlaceInfo", addresses.get(0).toString());
            address = "Address: ";
            if (addresses.get(0).getSubThoroughfare() != null) {
                address += addresses.get(0).getSubThoroughfare() + "\n";
            }
            if (addresses.get(0).getThoroughfare() != null) {
                address += addresses.get(0).getThoroughfare() + "\n";
            }
            if (addresses.get(0).getLocality() != null) {
                address += addresses.get(0).getLocality() + "\n";
            }
            if (addresses.get(0).getPostalCode() != null) {
                address += addresses.get(0).getPostalCode() + "\n";
            }
            if (addresses.get(0).getCountryName() != null) {
                address += addresses.get(0).getCountryName();
            }

            //
            TextView addressText = findViewById(R.id.AddTextView);
            addressText.setText(address);


        } catch (IOException e) {
            Log.i("AMAN", location.toString());
            e.printStackTrace();
        }


    }

    public void startListening() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = this::Update;


        if (Build.VERSION.SDK_INT < 23) {

            startListening();

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Update(location);
                }
//                    startListening();

            }
        }

    }
}