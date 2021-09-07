package com.example.remotecontrolandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    EditText usernameET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setUsername();
    }

    public void setUsername()
    {
        usernameET = findViewById(R.id.usernameET);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        if(sharedPreferences.getString("username", "Select your username").equals("Select your username"))
        {
            usernameET.setHint("Select your username");
        }
        else
        {
            usernameET.setText(sharedPreferences.getString("username", "Select your username"));
        }
    }

    public void onSaveButton(View view)
    {
        usernameET = findViewById(R.id.usernameET);
        if(usernameET.getText().toString().equals(""))
        {
            Toast.makeText(this, "Error: Username can't be empty.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Username saved successfully.", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", usernameET.getText().toString());
            myEdit.apply();
            finish();
        }
    }
}
