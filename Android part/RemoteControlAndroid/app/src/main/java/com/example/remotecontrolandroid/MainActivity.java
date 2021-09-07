package com.example.remotecontrolandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendText(View v)
    {
        Random rand = new Random();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String usernameSP = sharedPreferences.getString("username", "Select your username");
        WebView webView = findViewById(R.id.webView);
        String url = "http://rc.8u.cz/sendKeyStroke.php";
        String type = "type=1";
        String username = "username=" + usernameSP;
        String messageKey = "messageKey=" + rand.nextInt(10001);
        String keyStroke = "keyStroke=" + v.getTag().toString();
        String params = type +  "&" + username + "&" + messageKey + "&" + keyStroke;
        if(usernameSP.equals("Select your username"))
        {
            Toast.makeText(this, "Error: No username set! Please select your username in the settings.", Toast.LENGTH_LONG).show();
        }
        else
        {
            webView.postUrl(url,params.getBytes());
        }
    }

    public void onSettingsButton(View v)
    {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}