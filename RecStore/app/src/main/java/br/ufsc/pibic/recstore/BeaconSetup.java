package br.ufsc.pibic.recstore;

/**
 * Created by trdp on 1/31/17.
 */

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

/**
 * Configurações para interação com beacons
 */
public class BeaconSetup {


    public static void configBeacon(BeaconManager beaconManager) {

        beaconManager.setBackgroundMode(true);
        beaconManager.setBackgroundScanPeriod(1000l);
        beaconManager.setBackgroundBetweenScanPeriod(1l);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

    }

}
