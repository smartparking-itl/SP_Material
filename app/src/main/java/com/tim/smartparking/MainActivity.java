package com.tim.smartparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static int currFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Fragment f1 = new MainActivityFragment();
        if (currFragment == 1 || currFragment == 0) {
            FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
            ftr.replace(R.id.fragment, f1);
            ftr.commit();
            currFragment = 1;
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder(this).withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withBadge("2")
                                .withName("Города"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("О приложении")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 3) {
                            FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
                            Fragment f1 = new Fragment2();
                            ftr.replace(R.id.fragment, f1);
                            ftr.commit();
                            currFragment = 2;
                        }
                        return false;
                    }
                })
                .build();


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
                "Main Page",
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

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("Frag", currFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        currFragment = state.getInt("Frag");
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

}