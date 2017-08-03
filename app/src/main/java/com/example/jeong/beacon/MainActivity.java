package com.example.jeong.beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;

import android.app.Activity;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> List;
    ArrayAdapter<String> adapter;
    ListView lv;
    private BeaconManager beaconManager;
    private Region region;
    private TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        List = new ArrayList<String>();
        List.add("연결된 비콘");

        tvId=(TextView)findViewById(R.id.tvId);
        beaconManager=new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    for(int i=0; i<list.size(); i++) {
                        Beacon beacon = list.get(i);
                        Log.d("Airport", "Nearest places: " + beacon.getMacAddress());
                        tvId.setText(beacon.getMacAddress() + "");

                            List.add(tvId.getText().toString());
                            adapter.notifyDataSetChanged();

                    }
                }
            }
        }); region = new Region("ranged region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null, null);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, List);
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady(){
                beaconManager.startRanging(region);
            }
        });
    }
    @Override
    protected void onPause(){
        beaconManager.stopRanging(region);
        super.onPause();
    }
}
