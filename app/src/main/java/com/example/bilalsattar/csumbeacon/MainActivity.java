package com.example.bilalsattar.csumbeacon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends Activity {

        private static final Map<String, List<String>> PLACES_BY_BEACONS;

        // TODO: replace "<major>:<minor>" strings to match your own beacons.
        static {
            Map<String, List<String>> placesByBeacons = new HashMap<>();
            placesByBeacons.put("22504:48827", new ArrayList<String>() {{
                add("Heavenly Sandwiches");
                // read as: "Heavenly Sandwiches" is closest
                // to the beacon with major 22504 and minor 48827
                add("Green & Green Salads");
                // "Green & Green Salads" is the next closest
                add("Mini Panini");
                // "Mini Panini" is the furthest away
            }});
            placesByBeacons.put("648:12", new ArrayList<String>() {{
                add("Mini Panini");
                add("Green & Green Salads");
                add("Heavenly Sandwiches");
            }});
            PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
        }

        private List<String> placesNearBeacon(Beacon beacon) {
            String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
            if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
                return PLACES_BY_BEACONS.get(beaconKey);
            }
            return Collections.emptyList();
        }

        private BeaconManager beaconManager;
        private Region region;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //Intent i = new Intent(this, login.activity);
            //startavitivty(i);
            //go to login activity immediately after
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            beaconManager = new BeaconManager(this);
            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                    if (!list.isEmpty()) {
                        Beacon nearestBeacon = list.get(0);
                        List<String> places = placesNearBeacon(nearestBeacon);
                        // TODO: update the UI here

                        Toast toast = Toast.makeText(getApplicationContext(), "in range", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d("Airport", "Nearest places: " + places);
                    }
                }
            });
            region = new Region("ranged region", UUID.fromString("8492e75f-4fd6-469d-b132-043fe94921d8"), 7182, 4554);
        }

        @Override
        protected void onResume() {
            super.onResume();

            SystemRequirementsChecker.checkWithDefaultDialogs(this);

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });
        }

        @Override
        protected void onPause() {
            beaconManager.stopRanging(region);

            super.onPause();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
