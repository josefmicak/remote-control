package com.example.remotecontrolandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private EditText usernameET;
    private EditText firstKeyET;
    private EditText secondKeyET;
    private EditText thirdKeyET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setValues();
    }

    public void setValues()
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

        firstKeyET = findViewById(R.id.firstKeyET);
        secondKeyET = findViewById(R.id.secondKeyET);
        thirdKeyET = findViewById(R.id.thirdKeyET);
        if(sharedPreferences.getString("firstkey", "firstkeydefault").equals("firstkeydefault"))
        {
            firstKeyET.setHint("Select first key");
        }
        else
        {
            firstKeyET.setText(sharedPreferences.getString("firstkey", "firstkeydefault"));
        }
        if(sharedPreferences.getString("secondkey", "secondkeydefault").equals("secondkeydefault"))
        {
            secondKeyET.setHint("Select second key");
        }
        else
        {
            secondKeyET.setText(sharedPreferences.getString("secondkey", "secondkeydefault"));
        }
        if(sharedPreferences.getString("thirdkey", "thirdkeydefault").equals("thirdkeydefault"))
        {
            thirdKeyET.setHint("Select third key");
        }
        else
        {
            thirdKeyET.setText(sharedPreferences.getString("thirdkey", "thirdkeydefault"));
        }
    }

    public void onSaveButton(View view)
    {
        usernameET = findViewById(R.id.usernameET);
        firstKeyET = findViewById(R.id.firstKeyET);
        secondKeyET = findViewById(R.id.secondKeyET);
        thirdKeyET = findViewById(R.id.thirdKeyET);
        if(usernameET.getText().toString().equals("") || firstKeyET.getText().toString().equals("")
                || secondKeyET.getText().toString().equals("")  || thirdKeyET.getText().toString().equals(""))
        {
            Toast.makeText(this, "Error: Fields can't be empty.", Toast.LENGTH_LONG).show();
        }
        else if(firstKeyET.getText().toString().length() > 1 || secondKeyET.getText().toString().length() > 1
                || thirdKeyET.getText().toString().length() > 1)
        {
            Toast.makeText(this, "Error: Keys can only contain one character.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Settings saved successfully.", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", usernameET.getText().toString());
            myEdit.putString("firstkey", firstKeyET.getText().toString().toUpperCase());
            myEdit.putString("secondkey", secondKeyET.getText().toString().toUpperCase());
            myEdit.putString("thirdkey", thirdKeyET.getText().toString().toUpperCase());
            myEdit.apply();

            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
