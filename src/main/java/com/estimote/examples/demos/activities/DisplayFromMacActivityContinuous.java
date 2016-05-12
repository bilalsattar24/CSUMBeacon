package com.estimote.examples.demos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.examples.demos.R;
import com.estimote.examples.demos.adapters.EddystonesListAdapter;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.eddystone.Eddystone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * Created by Markus on 4/18/2016.
 */
public class DisplayFromMacActivityContinuous extends BaseActivity{
    private String mac;

    private static final String TAG = ListEddystoneActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_EDDYSTONE = "extrasEddystone";

    String passedMac;

    private BeaconManager beaconManager;
    private EddystonesListAdapter adapter;

    @Override protected int getLayoutResId() {
        return R.layout.main;
    }

    @Override protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_from_mac);

        mac = getIntent().getExtras().getString("mac"); //mac = "[F1:6C:09:05:AC:8E]"; //mac = savedInstanceState.getString("mac");
        ImageView imageView = (ImageView) findViewById(R.id.macView);
        //Log.d("Mac displayed",mac);
        DisplayCardTask task = new DisplayCardTask(getApplicationContext(),imageView);
        task.execute(mac);

        passedMac = mac;
        adapter = new EddystonesListAdapter(this);
        beaconManager = new BeaconManager(this);

        startScanning();
    }

    @Override protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }


    @Override protected void onStop() {
        beaconManager.disconnect();
        super.onStop();
    }

    @Override protected void onResume() {
        super.onResume();

        startScanning();
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

                if (eddystones.get(0).macAddress.toString() != null && !(eddystones.get(0).macAddress.toString().equals(passedMac))) {

                    System.out.println("Test123 TRUE" + eddystones.get(0).macAddress);
                    System.out.println("Test123" + eddystones.get(0).macAddress.toString());

                    Context context = getApplicationContext();

                    System.out.println("distance " + eddystones.get(0).rssi);

                    CharSequence text = "Beacon Found! Going to info Card!";
                    int duration = Toast.LENGTH_LONG;

                    //Toast toast = Toast.makeText(context, text, duration);
                    //toast.show();


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

    public void goToActivity(String mac){
        Intent i = new Intent(this, DisplayFromMacActivityContinuous.class);
        Bundle extraInfo = new Bundle();
        extraInfo.putString("mac", mac);
        i.putExtras(extraInfo);
        startActivity(i);
    }




    //Bitmap Goodies
    public class DisplayCardTask extends AsyncTask<String,Void,Bitmap> {

        private Context cxt;
        private String mac;
        private ImageView imageView;
        private String dbUrl;
        private String cardUrl;

        public DisplayCardTask(Context cxt, ImageView imageView)
        {
            this.cxt = cxt;
            //this.mac = mac;
            this.imageView = imageView;
            dbUrl = "http://54.201.92.64/getCard.php";
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            mac = params[0]; //first parameter will be mac address
            Bitmap bmp = null;

            //make api call to server
            //parse JSON
            //create Bitmap from URL
            //Set ImageView to Newly created Bitmap

            try {
                URL url = new URL(dbUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mac","UTF-8") +"="+ URLEncoder.encode(mac,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result = "",line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();
                urlConnection.disconnect();

                try{
                    JSONObject data = new JSONObject(result);
                    cardUrl = data.getString("url");
                    Log.d("test", "card URL continuous: " + cardUrl);

                    try{
                        URL image = new URL(cardUrl);
                        try{
                            bmp = BitmapFactory.decodeStream(image.openConnection().getInputStream());

                        }catch(IOException io){
                        }

                    }catch(MalformedURLException e){
                    }

                }catch(JSONException e){

                }
                //result will have JSON data (url for card to display)

                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bmp;
        }

        protected void onPostExecute(Bitmap result) {imageView.setImageBitmap(result);}
    }
}


