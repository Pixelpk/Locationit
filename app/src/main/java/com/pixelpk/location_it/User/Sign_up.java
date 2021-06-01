package com.pixelpk.location_it.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.User;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

public class Sign_up extends AppCompatActivity
{
    TextInputLayout password_txt;
    EditText fullname_txt,username_txt;
    String fullname_str,username_str,password_str;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname_txt = findViewById(R.id.full_name_sign_up);
        username_txt = findViewById(R.id.user_name_sign_up);
        password_txt = findViewById(R.id.password_sign_up);
        databaseHelper = new DatabaseHelper(this);
        user = new User();

        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void go_to_login(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Sign_in.class);
        startActivity(intent);
        finish();
    }

    public void perform_sign_up(View view)
    {
        fullname_str = fullname_txt.getText().toString();
        username_str = username_txt.getText().toString();
        password_str = password_txt.getEditText().getText().toString();

        if(!validatePassword()  | !validateUsername() | !validate_full_name())
        {
            return;
        }

        else
        {
            LoadData();
        }


    }

    private void LoadData()
    {
        if (!databaseHelper.checkUser(username_txt.getText().toString()))
        {
            user.setUsername(username_txt.getText().toString());
            user.setFull_name(fullname_str);
            user.setPassword(password_str);
            databaseHelper.addUser(user);
            // Snack Bar to show success message that record saved successfully
            Toast.makeText(this, "User Successfully Registered", Toast.LENGTH_SHORT).show();
            editor.putString("user_sign_in",username_str).apply();
            editor.putString("login_id","1").apply();
            Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
            startActivity(intent);
            finish();
        }
        else
        {
            // Snack Bar to show error message that record already exists
            Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show();
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
            return true;
        }
    }


    private Boolean validate_full_name()
    {
        String val_name = fullname_txt.getText().toString();

        if(val_name.isEmpty())
        {
            fullname_txt.setError("Field is Empty");
            return false;
        }

        else
        {
            fullname_txt.setError(null);
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

    public void back_to_welcome_screen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Welcome_Page.class);
        startActivity(intent);
        finish();
    }
}