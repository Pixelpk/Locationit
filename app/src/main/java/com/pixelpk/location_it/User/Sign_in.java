package com.pixelpk.location_it.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

public class Sign_in extends AppCompatActivity
{
    EditText username_txt;
    TextInputLayout    password_txt;
    String username_str,password_str;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username_txt = findViewById(R.id.username_sign_in);
        password_txt = findViewById(R.id.password_sign_in);

        databaseHelper = new DatabaseHelper(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



    }

    private void LoadData()
    {
        if (databaseHelper.checkUser(username_str,password_str))
        {
            Toast.makeText(this, "User Login Successful", Toast.LENGTH_SHORT).show();

            editor.putString("user_sign_in",username_str).apply();
            editor.putString("login_id","1").apply();
            Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
            startActivity(intent);
            finish();
        }
        else
            {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }

    }

    private Boolean validateUsername()
    {
        String val_name = username_txt.getText().toString();

        if(val_name.isEmpty())
        {
            username_txt.setError("Field is Empty");
            return false;
        }

        else
        {
            username_txt.setError(null);
//            username_txt.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        String val_name = password_txt.getEditText().getText().toString();

        if(val_name.isEmpty())
        {
            password_txt.setError("Field is Empty");
            return false;
        }
        if(val_name.length()<=3)
        {
            password_txt.setError("Password too short");
            return false;
        }

        else if(val_name.length()>6)
        {
            password_txt.setError("Password too Long");
            return false;
        }

        else
        {
            password_txt.setError(null);
            password_txt.setErrorEnabled(false);
            return true;
        }
    }

    public void perform_login(View view)
    {
        username_str = username_txt.getText().toString();
        password_str = password_txt.getEditText().getText().toString();


        if(!validatePassword()  | !validateUsername())
        {
            return;
        }

        else
        {
            LoadData();
        }
    }

    public void go_to_signup(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Sign_up.class);
        startActivity(intent);
        finish();
    }

    public void back_to_welcome_screen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Welcome_Page.class);
        startActivity(intent);
        finish();
    }
}