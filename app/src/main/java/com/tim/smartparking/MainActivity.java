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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        g = new Geocoder(MainActivity.this);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline() && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Snackbar.make(view, "Мы пытаемся найти вас...", Snackbar.LENGTH_SHORT)
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
                    Snackbar.make(view, "Нет интернет-соединения", Snackbar.LENGTH_LONG).setAction("Настройки", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Необходимо включить местоположение по сетям", Snackbar.LENGTH_LONG).setAction("Настройки", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).show();
                }
            }
        });
        
		/*
         * // Тут инизиализируем поиск iBeacon ibp =
		 * IBeaconProtocol.getInstance(this); ibp.setListener(new
		 * IBeaconListener() {
		 * 
		 * @Override public void enterRegion(IBeacon ibeacon) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void exitRegion(IBeacon ibeacon) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void beaconFound(IBeacon ibeacon) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void searchState(int state) { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void operationError(int status) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * });
		 */

        Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                switch (arg2) {
                    case 0:
                        ((Spinner) findViewById(R.id.spinner1)).setVisibility(View.INVISIBLE);
                        ((Button) findViewById(R.id.button1)).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        ((Spinner) findViewById(R.id.spinner1)).setAdapter(new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                MainActivity.this.getResources().getStringArray(R.array.ChelniParks)));
                        ((Spinner) findViewById(R.id.spinner1)).performClick();
                        ((Spinner) findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.button1)).setVisibility(View.VISIBLE);//Chelny
                        break;
                    case 2:
                        ((Spinner) findViewById(R.id.spinner1)).setAdapter(new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                MainActivity.this.getResources().getStringArray(R.array.KazanParks)));
                        ((Spinner) findViewById(R.id.spinner1)).performClick();
                        ((Spinner) findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.button1)).setVisibility(View.VISIBLE);//Kazan
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ((Spinner) findViewById(R.id.spinner1)).setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.spinner)).getSelectedItemPosition() == 1) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            Intent i = new Intent();
                            i.setClass(MainActivity.this, Kruiz.class);
                            startActivity(i);
                            break;
                        case 2:
                            Intent ir = new Intent();
                            ir.setClass(MainActivity.this, Garag500.class);
                            startActivity(ir);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            startActivity(new Intent(MainActivity.this, Kolco.class));
                            break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, Chistopolskaya.class));
                            break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // Not used
	/*
	 * private void scanBeacons(){ // Check Bluetooth every time
	 * Log.i(Utils.LOG_TAG,"Scanning");
	 * 
	 * // Filter based on default easiBeacon UUID, remove if not required
	 * //_ibp.setScanUUID(UUID here);
	 * 
	 * if(!IBeaconProtocol.configureBluetoothAdapter(this)){ //Intent
	 * enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * //startActivityForResult(enableBtIntent, 9 ); }else{ if(ibp.isScanning())
	 * ibp.stopScan(); ibp.reset(); ibp.startScan(); } }
	 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), R.string.exit, Toast.LENGTH_SHORT)
                    .show();
        back_pressed = System.currentTimeMillis();
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

    protected void onResume() {
        super.onResume();
        Spinner.setSelection(0);
        try {
            startGPS();
        } catch (Exception e) {
        }
        // scanBeacons();
    }

    protected void onPause() {
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tim.smartparking/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tim.smartparking/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class findTown extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
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
                startActivity(new Intent(MainActivity.this, KazanParks.class));
            } else if (act == 1) {
                startActivity(new Intent(MainActivity.this, ChelniParks.class));
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Configuration.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFind(View v) {
        if (((Spinner) findViewById(R.id.spinner)).getSelectedItemPosition() == 2) {
            Intent intent = new Intent();
            intent.setClass(this, GooglePlaceActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(this, BeaconMap.class);
            startActivity(intent);
        }
    }


}