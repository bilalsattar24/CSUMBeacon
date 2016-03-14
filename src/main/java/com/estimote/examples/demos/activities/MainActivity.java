package com.estimote.examples.demos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.estimote.examples.demos.R;

/**
 * Shows all available demos.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainactivity);

    //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //toolbar.setTitle(getTitle());

    View tourButton = findViewById(R.id.tour);
    tourButton.setOnClickListener(this);
    tourButton.bringToFront();


  }

  private void startListBeaconsActivity(String extra) {
    Intent intent = new Intent(MainActivity.this, ListBeaconsActivity.class);
    intent.putExtra(ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY, extra);
    startActivity(intent);
  }


  public void onClick(View v) {
    if (v.getId() == R.id.tour) {
      Intent I = new Intent(getApplicationContext(), ListEddystoneActivity.class);
      startActivity(I);
    }


  }
}
