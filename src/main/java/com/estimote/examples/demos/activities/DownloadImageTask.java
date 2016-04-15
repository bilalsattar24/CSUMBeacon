package com.estimote.examples.demos.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import com.estimote.examples.demos.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Markus on 4/14/2016.
 */
public class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
    Context context;
    AlertDialog alertDialog;

    public DownloadImageTask(Context cxt){
        context = cxt;
    }


    protected Bitmap doInBackground(String... params) {

        String url = "http://vignette4.wikia.nocookie.net/modernfamily/images/d/d2/Kobe_bryant_con_competencia.jpg/revision/latest?cb=20140107103249";
        Bitmap bmp = null;
        try{
            URL kobe = new URL(url);
            try{
                bmp = BitmapFactory.decodeStream(


                        kobe.openConnection().
                                getInputStream());

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


    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
