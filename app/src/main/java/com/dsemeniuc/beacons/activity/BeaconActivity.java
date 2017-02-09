package com.dsemeniuc.beacons.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dsemeniuc.beacons.R;
import org.altbeacon.beacon.BeaconConsumer;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    private final static String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private final static String BEACON_ID = "11c7f1f8-3058-b733-cb03-110ea9c11254";
    private final static String UUID = "myBeacons";
    private TextView mBeaconTV;
    private BeaconSearch searchBeaconService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        searchBeaconService = new BeaconSearch(this, IBEACON_LAYOUT, BEACON_ID, UUID);
        searchBeaconService.start();
    }

    @Override
    public void onBeaconServiceConnect() {
        searchBeaconService.onBeaconServiceConnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchBeaconService.stop();
        searchBeaconService = null;
    }

    public void onBeaconDetected(String info) {
        final String str = info;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBeaconTV = (TextView)findViewById(R.id.beacon);
                mBeaconTV.setText(str);
            }
        });
    }
}
