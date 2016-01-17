package com.tim.smartparking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleksei on 17.01.16.
 */
public class Fragment2 extends Fragment {

    public Fragment2() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kolco_map1, container, false);
    }
}
