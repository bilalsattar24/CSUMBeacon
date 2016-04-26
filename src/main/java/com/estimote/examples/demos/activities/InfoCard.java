package com.estimote.examples.demos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.examples.demos.R;
import com.estimote.examples.demos.adapters.EddystonesListAdapter;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.eddystone.Eddystone;
import java.util.Collections;
import java.util.List;

/**
 * Displays list of found eddystones sorted by RSSI.
 * Starts new activity with selected eddystone if activity was provided.
 *
 * @author wiktor.gworek@estimote.com (Wiktor Gworek)
 */
public class InfoCard extends BaseActivity {

    private static final String TAG = ListEddystoneActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_EDDYSTONE = "extrasEddystone";

    String passedMac;

    private BeaconManager beaconManager;
    private EddystonesListAdapter adapter;

    @Override protected int getLayoutResId() {
        return R.layout.main;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.infocard);

        Bundle passedExtras = getIntent().getExtras();


        String mac = passedExtras.getString("mac");

        passedMac = mac;

        TextView main = (TextView) findViewById(R.id.textView1);

        main.append(" " + mac);

        // Configure device list.
        adapter = new EddystonesListAdapter(this);

        beaconManager = new BeaconManager(this);

        ImageView img = (ImageView) findViewById(R.id.contextcard);

        if(passedMac.equals("[FE:55:68:D6:08:36]")){
            //img.setImageResource(R.drawable.bit);
            //main.append(" PURP");
            Bundle bundle = new Bundle();
            Intent i = new Intent(getApplicationContext(),DisplayFromMacActivityContinuous.class);
            bundle.putString("mac","[FE:55:68:D6:08:36]");
            i.putExtras(bundle);
            startActivity(i);

        }
        else if(passedMac.equals("[F1:6C:09:05:AC:8E]")){
            //img.setImageResource(R.drawable.dc);
            //main.append(" BABY BLUE");
            Bundle bundle = new Bundle();
            Intent i = new Intent(getApplicationContext(),DisplayFromMacActivityContinuous.class);
            bundle.putString("mac","[F1:6C:09:05:AC:8E]");
            i.putExtras(bundle);
            startActivity(i);

        }
        else if(passedMac.equals("[F7:E2:16:76:24:59]")){
            //img.setImageResource(R.drawable.sports);
            //main.append(" MINT");
            Bundle bundle = new Bundle();
            Intent i = new Intent(getApplicationContext(),DisplayFromMacActivityContinuous.class);
            bundle.putString("mac","[F7:E2:16:76:24:59]");
            i.putExtras(bundle);
            startActivity(i);
        }
        else{//all others here

            System.out.println("Test123 Failed to change image");
        }


    }

    @Override protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override protected void onResume() {
        super.onResume();

        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    @Override protected void onStop() {
        beaconManager.disconnect();
        super.onStop();
    }

    private void startScanning() {
        toolbar.setSubtitle("Scanning...");
        adapter.replaceWith(Collections.<Eddystone>emptyList());

        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> eddystones) {
                toolbar.setSubtitle("Found beacons with Eddystoned protocol: " + eddystones.size());
                adapter.replaceWith(eddystones);
                System.out.println("Test123 " + eddystones.get(0).macAddress);

                if (eddystones.get(0).macAddress.toString()!= null && !(eddystones.get(0).macAddress.toString().equals(passedMac))){

                    System.out.println("Test123 TRUE" + eddystones.get(0).macAddress);
                    System.out.println("Test123" + eddystones.get(0).macAddress.toString());

                    Context context = getApplicationContext();

                    System.out.println("distance " + eddystones.get(0).rssi);

                    CharSequence text = "Beacon Found! Going to info Card!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                    goToActivity(eddystones.get(0).macAddress.toString());

                } else {
                    System.out.println("Test123 FALSE" + eddystones.get(0).macAddress);
                    System.out.println("Test123" + eddystones.get(0).macAddress.toString());
                }
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startEddystoneScanning();
            }
        });
    }

    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY) != null) {
                    try {
                        Class<?> clazz = Class.forName(getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY));
                        Intent intent = new Intent(InfoCard.this, clazz);
                        intent.putExtra(EXTRAS_EDDYSTONE, adapter.getItem(position));
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Finding class by name failed", e);
                    }
                }
            }
        };
    }

    public void goToActivity(String mac){
        Intent i = new Intent(this, InfoCard.class);
        //Pass info to login
        Bundle extraInfo = new Bundle();
        //How many hour total
        extraInfo.putString("mac", mac);
        i.putExtras(extraInfo);
        startActivity(i);
    }
}
