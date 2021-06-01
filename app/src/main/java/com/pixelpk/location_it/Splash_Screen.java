package com.pixelpk.location_it;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixelpk.location_it.User.Home_Dashboard;
import com.pixelpk.location_it.User.Sign_in;

public class Splash_Screen extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView logo;
    String val;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo_splash);

        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        val = sharedPreferences.getString("val_name","0");


        if(val=="0")
        {
//            Toast.makeText(this, "Hate", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(Splash_Screen.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Important Notice")
                    .setMessage("Our App provides user to store his/her location offline by getting longitude and " +
                            "latitude of his/her current location and saving it to internal Database, Our app does not track user " +
                            "location in Background.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    //Calling new Activity
                                    startActivity(new Intent(getApplicationContext(), Welcome_Page.class));
                                    finish();
                                }
                            }, SPLASH_TIME_OUT);
                        }
                    }).show();
            editor.putString("val_name","1").apply();
        }

        else
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    //Calling new Activity
                    startActivity(new Intent(getApplicationContext(), Welcome_Page.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }










    }
}