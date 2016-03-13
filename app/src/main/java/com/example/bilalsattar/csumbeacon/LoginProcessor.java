package com.example.bilalsattar.csumbeacon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;

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



public class LoginProcessor extends AsyncTask <String, String, String>{

    Context context;
    AlertDialog alertDialog;
    public LoginProcessor(Context cxt){
        context = cxt;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String username = params[1];
        String password = params[2];
        String login_url = "http://54.201.92.64/login.php"; //http://54.201.92.64/phpmyadmin/
        if(type.equals("login")){
            try {
                URL url = new URL(login_url);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8") +"="+ URLEncoder.encode(username,"UTF-8") + "&"
                        +URLEncoder.encode("password","UTF-8") +"="+ URLEncoder.encode(password,"UTF-8");
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

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("success"))
        {
            //change intent to main activity
            Intent mainActivity = new Intent(context,MainActivity.class);
            context.startActivity(mainActivity);
        } else{
            alertDialog.setMessage(result);
            alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }


}
