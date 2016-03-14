package com.estimote.examples.demos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.estimote.examples.demos.R;

/**
 * Created by michaelsarmiento on 3/14/16.
 */
public class InfoCard extends Activity implements View.OnClickListener

    {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.infocard);

            Bundle passedExtras = getIntent().getExtras();

            String mac = passedExtras.getString("mac");

            TextView main = (TextView) findViewById(R.id.textView1);

            main.append(" " + mac);



    }

    public void onClick(View v) {

    }





}
