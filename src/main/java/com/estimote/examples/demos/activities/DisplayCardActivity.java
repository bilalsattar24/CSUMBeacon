package com.estimote.examples.demos.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.estimote.examples.demos.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by Markus on 4/14/2016.
 */
public class DisplayCardActivity extends AppCompatActivity {
    //public static URL kobe;
    public static String url = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";
    @Override protected void onCreate(Bundle savedInstanceState){
        Log.d("test", "entered displaycard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_card);

       //String url = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";

        //View v = findViewById(R.id.imageView2);

        //display(url,v);
        ImageView image = (ImageView) findViewById(R.id.imageView2);
        DownloadImageTask task = new DownloadImageTask(getApplicationContext(),image);
        task.execute("test");



    }

    public static void display(String url, View v)
    {


    }

    public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        Context context;
        ImageView bmImage;
        AlertDialog alertDialog;

        public DownloadImageTask(Context cxt, ImageView bmImage2){
            context = cxt;
            this.bmImage = bmImage2;
        }


        protected Bitmap doInBackground(String... params) {

            String url = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";
            Bitmap bmp = null;
            ImageView image = (ImageView) findViewById(R.id.imageView2);
            try{
                URL kobe = new URL(url);
                try{
                    bmp = BitmapFactory.decodeStream(kobe.openConnection().getInputStream());

                }catch(IOException io){
                }

            }catch(MalformedURLException e){
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Status");

        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
