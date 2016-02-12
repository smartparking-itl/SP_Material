package com.tim.smartparking;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapView extends FragmentActivity {

	SupportMapFragment smf;
	GoogleMap gm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_places);
		if (Build.VERSION.SDK_INT >= 21) {

			// Set the status bar to dark-semi-transparentish
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// Set paddingTop of toolbar to height of status bar.
			// Fixes statusbar covers toolbar issue
		}

		smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
		gm = smf.getMap();
		if (gm == null) {
			finish();
			return;
		}
		gm.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		init();

		try {
			gm.setMyLocationEnabled(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		gm.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng ll) {
				// TODO Auto-generated method stub

			}

		});
		gm.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng ll) {
				// TODO Auto-generated method stub

			}

		});
		gm.addMarker(new MarkerOptions().position(new LatLng(0, 0)).flat(true)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.title("Test SP marker"));
	}

}
