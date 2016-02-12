package com.tim.smartparking;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MyLocationActivity extends Activity implements LocationListener 
{
    private static final String TAG=MyLocationActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.test);

        if (Build.VERSION.SDK_INT >= 21) {

            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Set paddingTop of toolbar to height of status bar.
            // Fixes statusbar covers toolbar issue
        }

        LocationManager lm =
              (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void onLocationChanged(Location location) 
    {
       if (location != null) 
       {
          Log.d(TAG, "Широта="+location.getLatitude());
          Log.d(TAG, "Долгота="+location.getLongitude());
       }
    }

    public void onProviderDisabled(String provider) 
    {
    }

    public void onProviderEnabled(String provider) 
    {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
    }
}
