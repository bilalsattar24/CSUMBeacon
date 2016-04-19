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
    private String url;//Used to create image// = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";
    @Override protected void onCreate(Bundle savedInstanceState){
        Log.d("test", "entered displaycard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_card);
        //access url imageview ID from bundled data
        url = savedInstanceState.getString("url");
        //int id = savedInstanceState.getInt("viewId");

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        DownloadImageTask task = new DownloadImageTask(getApplicationContext(),imageView);
        task.execute(url);

    }



    public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        Context context;
        ImageView bmImageView;
        AlertDialog alertDialog;


        public DownloadImageTask(Context cxt, ImageView bmImageView){
            context = cxt;
            this.bmImageView = bmImageView;
        }


        protected Bitmap doInBackground(String... params) {
            url = params[0];
            //String url = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";
            Bitmap bmp = null;
            //ImageView image = (ImageView) findViewById(R.id.imageView2);
            try{
                URL image = new URL(url);
                try{
                    bmp = BitmapFactory.decodeStream(image.openConnection().getInputStream());

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
            bmImageView.setImageBitmap(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
