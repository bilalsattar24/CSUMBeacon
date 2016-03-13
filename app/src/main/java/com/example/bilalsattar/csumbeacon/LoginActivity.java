package com.example.bilalsattar.csumbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends Activity {
    private Button login;
    private Button signup;
    Context appCxt;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        appCxt = getApplicationContext();
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //change to main activity for now.
                Intent mainActivity = new Intent(appCxt, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

}
