package com.example.remotecontrolandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
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
    private ImageView messageIndicatorLight;
    private ImageView responseIndicatorLight;
    int responseKey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                controlIndicatorLight(messageIndicatorLight, R.color.red);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        messageIndicatorLight = findViewById(R.id.messageIndicatorLight);
        responseIndicatorLight = findViewById(R.id.responseIndicatorLight);
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

    public void sendText(View v) throws IOException, InterruptedException {
        if(loadedUsername.equals("Select your username"))
        {
            Toast.makeText(this, "Error: No username set! Please select your username in the settings.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Random rand = new Random();
            String url = "http://rc.8u.cz/sendKeyStroke.php";
            String type = "type=1";
            String username = "username=" + loadedUsername;
            String messageKey = "messageKey=" + rand.nextInt(10001);
            String keyStroke = "keyStroke=" + v.getTag().toString();
            String params = type +  "&" + username + "&" + messageKey + "&" + keyStroke;
            controlIndicatorLight(messageIndicatorLight, R.color.limegreen);
            webView.postUrl(url,params.getBytes());
            int oldResponseKey = checkResponseKey();
            final int[] newResponseKey = {0};

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        checkResponseKey();
                        newResponseKey[0] = checkResponseKey();
                        if(oldResponseKey != newResponseKey[0])
                        {
                            controlIndicatorLight(responseIndicatorLight, R.color.limegreen);
                        }
                        else
                        {
                            controlIndicatorLight(responseIndicatorLight, R.color.red);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 1500);
        }
    }

    public void controlIndicatorLight(ImageView lightId, int colorId)
    {
        lightId.setColorFilter(getResources().getColor(colorId));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lightId.setColorFilter(getResources().getColor(R.color.cyan));
            }
        }, 2000);
    }

    public int checkResponseKey() throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String getResultsUrl = "http://rc.8u.cz/getKeyValues.php?username=" + loadedUsername;
                    URL url = new URL(getResultsUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String downloadedContent = "",data="";

                    while ((data = reader.readLine()) != null){
                        downloadedContent  += data + "\n";
                    }
                    JSONArray jsonArray = new JSONArray(downloadedContent);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        responseKey = jsonObject1.optInt("responseKey");
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        return responseKey;
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