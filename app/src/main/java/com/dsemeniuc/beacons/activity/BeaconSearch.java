package com.dsemeniuc.beacons.activity;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.dsemeniuc.interfaces.OnBeaconSearch;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconSearch implements OnBeaconSearch {

    private String beaconLayout;
    private String beaconId;
    private String UUID;
    private BeaconManager beaconManager;
    private Context context;
    private final static int BEACON_RSSI = -90;

    public BeaconSearch(Context context, final String beaconLayout, final String beaconId, final String UUID) {
        this.context = context;
        this.beaconLayout = beaconLayout;
        this.beaconId = beaconId;
        this.UUID = UUID;
    }

    @Override
    public void start() {
        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(beaconLayout));
        beaconManager.bind((BeaconConsumer) this.context);
    }

    @Override
    public void stop() {
        beaconManager.unbind((BeaconConsumer) context);
    }

    public void onBeaconServiceConnect(){
        final Region region = new Region(UUID, Identifier.parse(beaconId), null, null);
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> beacons, Region region) {
                for(org.altbeacon.beacon.Beacon beacon : beacons){
                    Log.d("TAG", " distance: " + beacon.getDistance() + " id: " + beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3());
                    ((BeaconActivity)context).onBeaconDetected(" distance: " + beacon.getDistance() + " id: " + beacon.getId1() + "/ RSSI: " + beacon.getRssi());
                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
