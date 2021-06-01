package com.pixelpk.location_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pixelpk.location_it.User.Add_Category;
import com.pixelpk.location_it.User.Home_Dashboard;
import com.pixelpk.location_it.User.Sign_in;
import com.pixelpk.location_it.User.Sign_up;

public class Welcome_Page extends AppCompatActivity {

    String login_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__page);

        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_id = sharedPreferences.getString("login_id","0");
//        Toast.makeText(this, login_id, Toast.LENGTH_SHORT).show();

        if(login_id.equals("1"))
        {
            Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public void go_as_guest(View view)
    {
//        editor.putString("go_guest","1").apply();
        Intent intent = new Intent(getApplicationContext(), Home_Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void go_to_signup(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Sign_up.class);
        startActivity(intent);
        finish();
    }

    public void go_to_login_page(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Sign_in.class);
        startActivity(intent);
        finish();
    }
}