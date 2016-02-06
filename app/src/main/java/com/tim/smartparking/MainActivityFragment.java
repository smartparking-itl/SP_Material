package com.tim.smartparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    static Spinner Spinner; // Спиннер
    public static int act = 0; // Какой активити открыть1
    private static Geocoder g; // Превращает координаты в название города
    public static String myTown; // В эту строку будет записан город, например,
    // "Уруссу"
    // public static IBeaconProtocol ibp;
    private static LocationManager lm;
    private static Location currLoc;
    private static findTown ft;
    private static final String LOG_TAG = "SP";
    private static LocationListener locationlistener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            currLoc = location;
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Log.d(LOG_TAG, "Provider status: " + status);
        }

    };

    public MainActivityFragment() {
    }

    public void findAndStartActivity() {
        if (isOnline() && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Snackbar.make(getView(), "Мы пытаемся найти вас...", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            try {
                startGPS();
            } catch (Exception e) {

            }
            ft = new findTown();
            if (ft.getStatus() == AsyncTask.Status.RUNNING) {
                ft.cancel(true);
            }
            ft.execute(null, null);
        } else if (!isOnline()) {
            Snackbar.make(getView(), "Нет интернет-соединения", Snackbar.LENGTH_LONG).setAction("Настройки", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }).show();
        } else {
            Snackbar.make(getView(), "Необходимо включить местоположение по сетям", Snackbar.LENGTH_LONG).setAction("Настройки", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        g = new Geocoder(getContext());

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndStartActivity();
            }
        });

        Spinner = (Spinner) getView().findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                switch (arg2) {
                    case 0:
                        ((Spinner) getView().findViewById(R.id.spinner1)).setVisibility(View.INVISIBLE);
                        ((Button) getView().findViewById(R.id.button1)).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        ((Spinner) getView().findViewById(R.id.spinner1)).setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1,
                                getActivity().getResources().getStringArray(R.array.ChelniParks)));
                        ((Spinner) getView().findViewById(R.id.spinner1)).performClick();
                        ((Spinner) getView().findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);
                        ((Button) getView().findViewById(R.id.button1)).setVisibility(View.VISIBLE);//Chelny
                        break;
                    case 2:
                        ((Spinner) getView().findViewById(R.id.spinner1)).setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1,
                                getActivity().getResources().getStringArray(R.array.KazanParks)));
                        ((Spinner) getView().findViewById(R.id.spinner1)).performClick();
                        ((Spinner) getView().findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);
                        ((Button) getView().findViewById(R.id.button1)).setVisibility(View.VISIBLE);//Kazan
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ((Spinner) getActivity().findViewById(R.id.spinner1)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) getView().findViewById(R.id.spinner)).getSelectedItemPosition() == 1) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            Intent i = new Intent();
                            i.setClass(getActivity(), Kruiz.class);
                            startActivity(i);
                            break;
                        case 2:
                            Intent ir = new Intent();
                            ir.setClass(getActivity(), Garag500.class);
                            startActivity(ir);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), Kolco.class));
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), Chistopolskaya.class));
                            break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            Log.v(LOG_TAG, "Internet works!");
            return true;
        } else {
            Log.v(LOG_TAG, "NO INTERNET");
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    public static void stopGPS() {
        try {
            lm.removeUpdates(locationlistener);
            Log.d(LOG_TAG, "Removed location updates");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void startGPS() {
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000,
                    locationlistener);
            Log.d(LOG_TAG, "Registered for location updates");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        Spinner.setSelection(0);
        try {
            startGPS();
        } catch (Exception e) {
        }
        // scanBeacons();
    }

    public void onPause() {
        super.onPause();
        try {
            stopGPS();
        } catch (Exception e) {
        }
        try {
            ft.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * if(ibp.isScanning()) { ibp.stopScan(); }
		 */
    }

    public static String findMyTown(String s) {
        int a, b;
        a = s.indexOf(",1:\"") + 4;
        b = s.indexOf("\"", a);
        return s.substring(a, b);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9) {
            if (resultCode == Activity.RESULT_OK) {
                // scanBeacons();
            }
        }
    }


    public class findTown extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "onPreExecute");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            boolean break_c = true;
            while (break_c) {
                try { // Здесь определяется город и записывается в строку
                    myTown = findMyTown(g.getFromLocation(
                            currLoc.getLatitude(), currLoc.getLongitude(), 1)
                            .toString());
                    if (myTown.equals("Казань")) {
                        act = 2;
                    } else if (myTown.equals("Набережные Челны")) {
                        act = 1;
                    }
                    break_c = false;
                    Log.d(LOG_TAG, Integer.toString(act));
                    Log.d(LOG_TAG, myTown);
                } catch (IOException e) {
                    if (isCancelled()) {
                        Log.d(LOG_TAG, "NOT FOUND!");
                        return null;
                    }
                } catch (NullPointerException e1) {
                    if (isCancelled()) {
                        cancel(true);
                        return null;
                    }
                }
            }
            Log.d(LOG_TAG, "Finished Async!");
            return myTown;
        }

        @Override
        protected void onPostExecute(String result) {
            if (act == 2) {
                startActivity(new Intent(getActivity(), KazanParks.class));
            } else if (act == 1) {
                startActivity(new Intent(getActivity(), ChelniParks.class));
            }
        }
    }

    public void onFind(View v) {
        if (((Spinner) getView().findViewById(R.id.spinner)).getSelectedItemPosition() == 2) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), GooglePlaceActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), BeaconMap.class);
            startActivity(intent);
        }
    }
}
