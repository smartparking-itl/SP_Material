package com.tim.smartparking;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

public class Garag500 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garag500);
		if (Build.VERSION.SDK_INT >= 21) {

			// Set the status bar to dark-semi-transparentish
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// Set paddingTop of toolbar to height of status bar.
			// Fixes statusbar covers toolbar issue
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.garag500, menu);
		return true;
	}

}
