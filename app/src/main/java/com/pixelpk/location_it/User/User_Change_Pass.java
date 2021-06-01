package com.pixelpk.location_it.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.User;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

public class User_Change_Pass extends AppCompatActivity
{
    TextInputLayout change_pass,confirm_pass;
    String str_change_pass,str_confirm_pass;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button change_pass_btn;
    User user;
    String str_username_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change__pass);



        change_pass = findViewById(R.id.password_change_pass);
        confirm_pass = findViewById(R.id.confirm_password_change_pass);
        change_pass_btn = findViewById(R.id.change_pass_bt_user);

        databaseHelper = new DatabaseHelper(this);
        user = new User();
        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        str_username_drawer = sharedPreferences.getString("user_sign_in","0");
//        Toast.makeText(this, str_username_drawer, Toast.LENGTH_SHORT).show();

        change_pass_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                str_change_pass   =  change_pass.getEditText().getText().toString();
                str_confirm_pass  =  confirm_pass.getEditText().getText().toString();

                if(!validatePassword()  | !validate_confirm_pass())
                {
                    return;
                }

              else if(str_change_pass.equals(str_confirm_pass))
                {
                    user.setPassword(str_confirm_pass);
                    databaseHelper.updateUser(str_username_drawer,str_confirm_pass);

                    Toast.makeText(User_Change_Pass.this, "Password Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    change_pass.setError("Password Does not match");
                    confirm_pass.setError("Password Does not Match");
                }
            }
        });
    }

    private Boolean validatePassword()
    {
        String val_name = change_pass.getEditText().getText().toString();

        if(val_name.isEmpty())
        {
            change_pass.setError("Field is Empty");
            return false;
        }
        if(val_name.length()<=3)
        {
            change_pass.setError("Password too short");
            return false;
        }

        else if(val_name.length()>6)
        {
            change_pass.setError("Password too Long");
            return false;
        }

        else
        {
            change_pass.setError(null);
            change_pass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validate_confirm_pass()
    {
        String val_name = confirm_pass.getEditText().getText().toString();

        if(val_name.isEmpty())
        {
            confirm_pass.setError("Field is Empty");
            return false;
        }
        if(val_name.length()<=3)
        {
            confirm_pass.setError("Password too short");
            return false;
        }

        else if(val_name.length()>6)
        {
            confirm_pass.setError("Password too Long");
            return false;
        }

        else
        {
            confirm_pass.setError(null);
            confirm_pass.setErrorEnabled(false);
            return true;
        }
    }

    public void back_to_home_screen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Home_Dashboard.class);
        startActivity(intent);
        finish();
    }
}