package com.example.remotecontrolandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String loadedUsername;
    private String firstKey;
    private String secondKey;
    private String thirdKey;
    private TextView firstKeyText;
    private View firstKeyView;
    private TextView secondKeyText;
    private View secondKeyView;
    private TextView thirdKeyText;
    private View thirdKeyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(this, Settings.class);
                settingsResultLauncher.launch(settingsIntent);
                return true;

            case R.id.about:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        loadedUsername = sharedPreferences.getString("username", "Select your username");

        firstKeyText = findViewById(R.id.firstKeyText);
        firstKeyView = findViewById(R.id.firstKeyView);
        secondKeyText = findViewById(R.id.secondKeyText);
        secondKeyView = findViewById(R.id.secondKeyView);
        thirdKeyText = findViewById(R.id.thirdKeyText);
        thirdKeyView = findViewById(R.id.thirdKeyView);
        firstKey = sharedPreferences.getString("firstkey", "firstkeydefault");
        secondKey = sharedPreferences.getString("secondkey", "secondkeydefault");
        thirdKey = sharedPreferences.getString("thirdkey", "thirdkeydefault");
        if(!(firstKey.equals("firstkeydefault")))
        {
            firstKeyText.setText(firstKey);
            firstKeyView.setTag(firstKey);
        }
        if(!(secondKey.equals("secondkeydefault")))
        {
            secondKeyText.setText(secondKey);
            secondKeyView.setTag(secondKey);
        }
        if(!(thirdKey.equals("thirdkeydefault")))
        {
            thirdKeyText.setText(thirdKey);
            thirdKeyView.setTag(thirdKey);
        }
    }

    public void sendText(View v)
    {
        if(loadedUsername.equals("Select your username"))
        {
            Toast.makeText(this, "Error: No username set! Please select your username in the settings.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Random rand = new Random();
            WebView webView = findViewById(R.id.webView);
            String url = "http://rc.8u.cz/sendKeyStroke.php";
            String type = "type=1";
            String username = "username=" + loadedUsername;
            String messageKey = "messageKey=" + rand.nextInt(10001);
            String keyStroke = "keyStroke=" + v.getTag().toString();
            String params = type +  "&" + username + "&" + messageKey + "&" + keyStroke;
            webView.postUrl(url,params.getBytes());
        }
    }

    public void onSettingsButton(View v)
    {
        Intent intent = new Intent(this, Settings.class);
        settingsResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> settingsResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        loadSettings();
                    }
                }
            });

    public void onAboutButton(View v)
    {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
}