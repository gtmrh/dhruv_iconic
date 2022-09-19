package com.rkvitsolutions.dhruviconic.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

public class MyLocationListener extends Application implements LocationListener {

    public String address;

    public MyLocationListener() {
    }

    @Override
    public void onLocationChanged(Location loc) {
//        editLocation.setText("");
//        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(
                getBaseContext(),
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                    + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                address = addresses.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
            + cityName;
//        editLocation.setText(s);
    }
}
