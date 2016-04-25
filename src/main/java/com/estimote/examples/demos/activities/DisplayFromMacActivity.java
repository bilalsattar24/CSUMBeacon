package com.estimote.examples.demos.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.estimote.examples.demos.R;

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

/**
 * Created by Markus on 4/18/2016.
 */
public class DisplayFromMacActivity extends AppCompatActivity{
    private String mac;

    @Override protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_from_mac);

        mac = getIntent().getExtras().getString("mac"); //mac = "[F1:6C:09:05:AC:8E]"; //mac = savedInstanceState.getString("mac");
        ImageView imageView = (ImageView) findViewById(R.id.macView);
        //Log.d("Mac displayed",mac);
        DisplayCardTask task = new DisplayCardTask(getApplicationContext(),imageView);
        task.execute(mac);
    }


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
                    Log.d("test", "card URL: " + cardUrl);

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


