package com.example.bilalsattar.csumbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
    private Button login;
    private Button signup;
    private EditText username, password;
    Context appCxt;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        appCxt = getApplicationContext();
        login = (Button) findViewById(R.id.loginButton);
        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.login_password);


    }

    public void OnLogin(View view){
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        String type = "login";
        LoginProcessor loginProcessor = new LoginProcessor(appCxt);
        loginProcessor.execute(type, username, password);
    }

}
